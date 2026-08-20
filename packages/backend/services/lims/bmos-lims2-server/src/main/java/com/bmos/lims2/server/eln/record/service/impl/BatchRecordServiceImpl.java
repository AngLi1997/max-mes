package com.bmos.lims2.server.eln.record.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.aspose.words.CssStyleSheetType;
import com.aspose.words.Document;
import com.aspose.words.ExportHeadersFootersMode;
import com.aspose.words.HeaderFooter;
import com.aspose.words.HeaderFooterType;
import com.aspose.words.HtmlSaveOptions;
import com.aspose.words.HorizontalAlignment;
import com.aspose.words.Node;
import com.aspose.words.NodeCollection;
import com.aspose.words.NodeType;
import com.aspose.words.Orientation;
import com.aspose.words.PageSetup;
import com.aspose.words.Section;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.cache.redis.lock.DistributedLock;
import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.tree.TreeUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.id.IdUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.file.docx.model.DocxOutlineInfo;
import com.bmos.file.docx.model.DocxFooter;
import com.bmos.file.docx.model.DocxHeader;
import com.bmos.file.docx.model.DocxHeaderFooterItem;
import com.bmos.lims2.common.constants.RecordConstant;
import com.bmos.lims2.common.enums.RecordItemTypeEnum;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.config.minio.MinioFileClient;
import com.bmos.lims2.server.config.minio.constants.MinioBucket;
import com.bmos.lims2.server.eln.record.DocxValidator;
import com.bmos.lims2.server.eln.record.convert.*;
import com.bmos.lims2.server.eln.record.dto.*;
import com.bmos.lims2.server.eln.record.entity.*;
import com.bmos.lims2.server.eln.record.enums.RecordStateEnum;
import com.bmos.lims2.server.eln.record.mapper.BatchRecordExpressionMapper;
import com.bmos.lims2.server.inspect.parameter.mapper.InspectMethodMapper;
import com.bmos.lims2.server.eln.record.mapper.BatchRecordMapper;
import com.bmos.lims2.server.eln.record.service.*;
import com.bmos.lims2.server.eln.record.util.DocxSplitUtil2;
import com.bmos.lims2.server.eln.record.vo.*;
import com.bmos.lims2.server.inspect.parameter.entity.InspectMethod;
import com.bmos.lims2.server.permission.dto.ResourcePermissionSaveDTO;
import com.bmos.lims2.server.permission.service.ResourcePermissionService;
import com.bmos.lims2.server.platform.expression.PlatformExpressionFeignClient;
import com.bmos.lims2.server.platform.expression.vo.ExpressionTreeNodeVO;
import com.bmos.lims2.server.platform.util.FeignUtils;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mybatis.CustomIdGenerator;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.lims2.common.enums.AuditBusinessModule;
import com.bmos.lims2.common.enums.OperationType;
import com.bmos.lims2.server.audit.operationlog.entity.AuditOperationLogEntity;
import com.bmos.lims2.server.audit.operationlog.service.AuditOperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BatchRecordServiceImpl implements BatchRecordService {

    private static final String RECORD_FILE_UPLOAD_LOCK = "record_file_upload_lock";

    /**
     * 批记录源文件保存位置
     */
    private static final String RECORD_FILE_SAVE_PATH = "/records/%s.docx";

    /**
     * 用于区分是否带有批注的后缀
     */
    private static final String COMMENT_SUFFIX = "_with_comment";

    @Autowired
    private BatchRecordMapper recordMapper;

    @Autowired
    private BatchRecordItemService itemService;

    @Autowired
    private BatchRecordParseService parseService;

    @Autowired
    private BatchRecordComponentService componentService;

    @Autowired
    private BatchRecordVersionService versionService;

    @Autowired
    private BatchRecordCategoryService categoryService;

    @Autowired
    private ResourcePermissionService resourcePermissionService;

    @Autowired
    private PlatformApiAdaptor platformApiAdaptor;

    @Resource
    private MinioFileClient minioFileClient;

    @Resource
    private BatchRecordExpressionMapper batchRecordExpressionMapper;

    @Resource
    private PlatformExpressionFeignClient expressionFeignClient;

    @Autowired
    private InspectMethodMapper inspectMethodMapper;

    @Autowired
    private com.bmos.lims2.server.inspect.parameter.service.InspectMethodOperateBindService inspectMethodOperateBindService;

    @Resource
    private AuditOperationLogService auditOperationLogService;


    @Override
    @DistributedLock(key = RECORD_FILE_UPLOAD_LOCK)
    public RecordUploadVo fileUpload(MultipartFile file) {
        List<RecordUploadItemVO> voList = new ArrayList<>();
        RecordUploadVo uploadVo = new RecordUploadVo();
        try {
            String originalFilename = file.getOriginalFilename();
            if (!originalFilename.contains(RecordConstant.FILE_TYPE)) {
                throw new BmosException(LimsResponseCode.FILE_ANALYSIS_FILE_TYPE_ERROR);
            }
            File originFile = File.createTempFile(RecordConstant.TEMPORARY_FOLDER, RecordConstant.FILE_TYPE);
            file.transferTo(originFile);
            long start = System.currentTimeMillis();
            System.out.println("开始切分 " + start);
            List<RecordFormatResult> validate = DocxValidator.validate(originFile);
            uploadVo.setFormatResults(validate);
            File formatFile = DocxValidator.transAndClearComments(originFile);
            long end = System.currentTimeMillis();
            log.info("结束切分: {}", end);
            log.info("切分耗时: {}", (end - start) / 1000);
            String recordKey = IdUtils.getSnowflakeStr() + "_" + System.currentTimeMillis();
            // 保存源文件
            String originFilePath = minioFileClient.uploadFile(MinioBucket.METHOD_TEMPLATE_BUCKET, originFile, String.format(RECORD_FILE_SAVE_PATH, recordKey + COMMENT_SUFFIX));
            // 保存格式化后的文件
            String formatPath = minioFileClient.uploadFile(MinioBucket.METHOD_TEMPLATE_BUCKET, formatFile, String.format(RECORD_FILE_SAVE_PATH, recordKey));
            log.info("批记录文件已保存至: {}", originalFilename);
            log.info("批记录文件(无标注)已保存至: {}", formatPath);
            uploadVo.setOriginFilePath(originFilePath);
            uploadVo.setFilePath(formatPath);
            // 生成单一记录项（不按大纲拆分），并保留转换后的HTML内容用于保存
            RecordUploadItemVO vo = new RecordUploadItemVO();
            // 名称：使用文件名（去后缀）
            String itemName = originalFilename;
            int dotIdx = itemName.lastIndexOf('.');
            if (dotIdx > 0) {
                itemName = itemName.substring(0, dotIdx);
            }
            vo.setName(itemName);
            vo.setItemType(RecordItemTypeEnum.CONTENT.getType());
            // 将整个文档转为HTML（图片Base64、内联CSS、正文不包含页眉页脚）
            try {
                Document fullDoc = new Document(formatFile.getAbsolutePath());
                Section firstSection = fullDoc.getFirstSection();
                if (firstSection != null) {
                    PageSetup ps = firstSection.getPageSetup();
                    vo.setStyle(ps.getOrientation() == Orientation.PORTRAIT);
                    vo.setFirstDifferent(ps.getDifferentFirstPageHeaderFooter());
                    vo.setOddAndEvenDifferent(ps.getOddAndEvenPagesHeaderFooter());
                    vo.setPageNumberStyle(ps.getPageNumberStyle());
                    vo.setPageStartingNumber(ps.getPageStartingNumber());
                    // 提取第一页（第一节）的页眉页脚，填充到VO
                    com.aspose.words.HeaderFooterCollection headersFooters = firstSection.getHeadersFooters();
                    DocxHeader header = new DocxHeader();
                    DocxFooter footer = new DocxFooter();
                    // Header - FIRST/PRIMARY/EVEN
                    HeaderFooter hfFirst = headersFooters.getByHeaderFooterType(HeaderFooterType.HEADER_FIRST);
                    HeaderFooter hfPrimary = headersFooters.getByHeaderFooterType(HeaderFooterType.HEADER_PRIMARY);
                    HeaderFooter hfEven = headersFooters.getByHeaderFooterType(HeaderFooterType.HEADER_EVEN);
                    if (hfFirst != null) {
                        header.setHeaderFirst(buildHeaderFooterItemHtml(hfFirst, HorizontalAlignment.LEFT));
                    }
                    if (hfPrimary != null) {
                        header.setHeaderPrimary(buildHeaderFooterItemHtml(hfPrimary, HorizontalAlignment.LEFT));
                    }
                    if (hfEven != null) {
                        header.setHeaderEven(buildHeaderFooterItemHtml(hfEven, HorizontalAlignment.LEFT));
                    }
                    // Footer - FIRST/PRIMARY/EVEN
                    HeaderFooter ffFirst = headersFooters.getByHeaderFooterType(HeaderFooterType.FOOTER_FIRST);
                    HeaderFooter ffPrimary = headersFooters.getByHeaderFooterType(HeaderFooterType.FOOTER_PRIMARY);
                    HeaderFooter ffEven = headersFooters.getByHeaderFooterType(HeaderFooterType.FOOTER_EVEN);
                    if (ffFirst != null) {
                        footer.setFooterFirst(buildHeaderFooterItemHtml(ffFirst, HorizontalAlignment.LEFT));
                    }
                    if (ffPrimary != null) {
                        footer.setFooterPrimary(buildHeaderFooterItemHtml(ffPrimary, HorizontalAlignment.LEFT));
                    }
                    if (ffEven != null) {
                        footer.setFooterEven(buildHeaderFooterItemHtml(ffEven, HorizontalAlignment.LEFT));
                    }
                    vo.setDocxHeader(header);
                    vo.setDocxFooter(footer);
                } else {
                    vo.setStyle(false);
                }
                HtmlSaveOptions options = new HtmlSaveOptions();
                options.setExportImagesAsBase64(true);
                options.setCssStyleSheetType(CssStyleSheetType.INLINE);
                options.setExportHeadersFootersMode(ExportHeadersFootersMode.NONE);
                java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
                fullDoc.save(out, options);
                String html = out.toString().replace("&#xa0;", " ");
                vo.setFileContent(html);
            } catch (Exception ex) {
                log.warn("整文档HTML转换失败，继续流程：{}", ex.getMessage());
                vo.setStyle(false);
            }
            // 记录项源文件路径（使用去批注后的格式化文件）
            vo.setFilePath(formatPath);
            voList.add(vo);
            uploadVo.setItemVO(voList);
            originFile.delete();
            formatFile.delete();
        } catch (BmosException e) {
            throw e;
        } catch (Exception e){
            log.error("文件上传失败", e);
            throw new BmosException(LimsResponseCode.RECORD_FILE_UPLOAD_ERROR);
        }
        return uploadVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
//    @OperationHistory(module = BusinessModule.BATCH_RECORD, operationType = OperationType.SAVE, remark = "#dto.remark", businessId = "#getVersionId")
    @OperationLog
    public BatchRecordSaveVO saveRecord(BatchRecordSaveDTO dto) {
        validateRecordCodeUnique(dto.getCode(), dto.getRecordId());
        if (ObjectUtil.isEmpty(dto.getRecordId())) {
            dto.setRecordId(CustomIdGenerator.nextId());
            recordMapper.saveOrUpdateRecord(dto);
        }
        boolean isNewVersion = ObjectUtil.isEmpty(dto.getVersionId());
        dto.setVersionId(isNewVersion ? CustomIdGenerator.nextId() : dto.getVersionId());
        BatchRecordVersion version = versionService.saveOrUpdateVersion(dto);
        if (isNewVersion) {
            // 记录新增版本日志
            auditOperationLogService.save(AuditOperationLogEntity.builder()
                    .module(AuditBusinessModule.METHOD_AUDIT.name())
                    .businessId(version.getId())
                    .operationType(OperationType.SAVE.getValue())
                    .remark(dto.getRemark())
                    .createBy(SysUserHolder.getUser().getUserId())
                    .build());
        }
        if (CollUtil.isNotEmpty(dto.getDeptIds())) {
            resourcePermissionService.save(ResourcePermissionSaveDTO.builder()
                    .deptIds(dto.getDeptIds())
                    .resourceId(dto.getRecordId())
                    .build());
        }
//        OperationHistoryContext.putVariable(dto, BatchRecordSaveDTO::getVersionId);
        if (CollUtil.isNotEmpty(dto.getItems())) {
            saveBatchItem(dto.getItems(), dto.getVersionId(), dto.getVersion());
        }
        return new BatchRecordSaveVO(version.getRecordId(), version.getId());
    }

    private void saveBatchItem(List<RecordItemListDTO> itemList, Long versionId, String version) {
        AtomicInteger sort = new AtomicInteger(1);
        itemList.forEach(item->item.setId(IdUtils.getSnowflake()));
        List<BatchRecordItem> recordItemList = RecordItemConvert.INSTANCE.convertToItemList(itemList);
        recordItemList.forEach(item -> {
            item.setPageConfig(JsonUtils.toJsonString(RecordStyleDTO.builder().pattern(item.getStyle() ? 1 : 0).build()));
            item.setSort(sort.getAndIncrement());
            item.setRecordVersionId(versionId);
            item.setVersion(version);
            item.setItemId(CustomIdGenerator.nextId());
        });
        itemService.saveOrUpdateItem(recordItemList);
        //筛选出内容or页眉or页脚不为空的数据
        List<RecordItemListDTO> recordParseList = CollectionUtils.filterList(itemList, dto ->
                StrUtil.isNotBlank(dto.getFileContent()) || ObjectUtil.isNotEmpty(dto.getDocxFooter()) ||
                        ObjectUtil.isNotEmpty(dto.getDocxHeader()));
        parseService.saveOrUpdateParse(RecordParseConvert.INSTANCE.convertToParseList(recordParseList));
    }

    private void validateRecordCodeUnique(String code, Long recordId) {
        if (recordMapper.existsByCode(code, recordId)) {
            throw new BmosException(LimsResponseCode.RECORD_CODE_DUPLICATE);
        }
    }

    public RecordItemDetailVO getRecordItemList(Long versionId) {
        RecordItemDetailVO vo = new RecordItemDetailVO();
        String recordName = recordMapper.queryNameByVersionId(versionId);
        List<RecordItemListVO> list = itemService.selectItemList(versionId);
        if (CollUtil.isNotEmpty(list)) {
            //查询组件
            Map<Long, List<BatchRecordComponent>> map = CollectionUtils.convertMultiMap(componentService.selectByVersionId(versionId),
                    BatchRecordComponent::getRecordItemId);
            //查询记录项大表字段
            List<Long> recordItemId = CollectionUtils.convertList(list, RecordItemListVO::getId);
            Map<Long, BatchRecordParse> parseMap = CollectionUtils.convertMap(parseService.selectByItemId(recordItemId),
                    BatchRecordParse::getId);
            list.forEach(item -> {
                item.setComponentList(ObjectUtil.isNotEmpty(map) ?
                        TreeUtil.buildTree(RecordComponentConvert.INSTANCE.convertToVoList(map.get(item.getItemId())), false) : Collections.emptyList());
                BatchRecordParse parse = parseMap.get(item.getId());
                if (ObjectUtil.isNotEmpty(parse)) {
                    item.setFileContent(Optional.ofNullable(parse.getFileContent()).orElse(null));
                    item.setDocxHeaderJson(Optional.ofNullable(parse.getDocxHeader()).orElse(null));
                    item.setDocxFooterJson(Optional.ofNullable(parse.getDocxFooter()).orElse(null));
                }
            });
            Map<RecordItemTypeEnum, List<RecordItemListVO>> itemMap = CollectionUtils.convertMultiMap(list, RecordItemListVO::getItemType);
            vo.setItemList(itemMap.get(RecordItemTypeEnum.CONTENT));
        }
        vo.setRecordName(recordName);
        return vo;
    }

    @Override
    public CommonPage<RecordListVO> getRecordPage(RecordListQueryDTO dto) {
        List<RecordListVO> list;
        if (ObjectUtil.isNotNull(dto.getRecordId())) {
            list = recordMapper.getRecordPage(dto);
        } else {
            if (ObjectUtil.isNotNull(dto.getCategoryId())) {
                List<Long> categoryList = categoryService.selectCategoryList(dto.getCategoryId());
                dto.setCategoryList(categoryList);
            }
            List<Long> deptIds = platformApiAdaptor.deptIds();
            if (CollUtil.isEmpty(deptIds)) {
                return CommonPage.CommonPage(Collections.emptyList(), 0L, dto);
            }
            dto.setDeptIds(deptIds);
            list = recordMapper.getFirstRecord(dto);
        }
        if (ObjectUtil.isNotEmpty(list)) {
            List<BatchRecordCategory> categories = categoryService.selectCategory();
            BatchRecordCategoryConvert.INSTANCE.covertToMap(list, categories);

            // 追加：通过 lm_inspect_parameter_record 查询并填充分析项id、code（1:N 中每条记录只对应一个分析项）
            List<Long> recordIds = com.bmos.common.util.collection.CollectionUtils.convertList(list, RecordListVO::getRecordId);
            if (cn.hutool.core.collection.CollUtil.isNotEmpty(recordIds)) {
                java.util.List<com.bmos.lims2.server.inspect.parameter.entity.InspectMethod> relations = inspectMethodMapper.selectByRecordIdList(recordIds);
                java.util.Map<Long, java.util.List<com.bmos.lims2.server.inspect.parameter.entity.InspectMethod>> recordIdToRelations =
                        relations.stream().collect(java.util.stream.Collectors.groupingBy(com.bmos.lims2.server.inspect.parameter.entity.InspectMethod::getRecordId));
                list.forEach(item -> {
                    java.util.List<com.bmos.lims2.server.inspect.parameter.entity.InspectMethod> rels = recordIdToRelations.get(item.getRecordId());
                    if (cn.hutool.core.collection.CollUtil.isEmpty(rels)) {
                        item.setParameterId(null);
                        item.setParameterCode(null);
                    } else {
                        com.bmos.lims2.server.inspect.parameter.entity.InspectMethod first = rels.get(0);
                        item.setParameterId(first.getParameterId());
                    }
                    // 填充：记录已绑定的操作规程ID集合
                    item.setOperateIdList(inspectMethodOperateBindService.listOperateIdsByMethod(item.getRecordId()));
                });
            }
        }
        return CommonPage.convertPage(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
//    @OperationHistory(module = BusinessModule.BATCH_RECORD, operationType = OperationType.NULLIFY, remark = "#dto.remark", businessId = "#dto.id")
    @OperationLog
    public Boolean updateVersion(RecordVersionDTO dto) {
        Boolean updated = versionService.updateVersion(RecordVersionConvert.INSTANCE.convertToDo(dto));
        if (Boolean.TRUE.equals(updated)) {
            auditOperationLogService.save(AuditOperationLogEntity.builder()
                    .module(AuditBusinessModule.METHOD_AUDIT.name())
                    .businessId(dto.getId())
                    .operationType(RecordStateEnum.INVALID.getValue().equals(dto.getState())?OperationType.INVALID.getValue():OperationType.NULLIFY.getValue())
                    .remark(dto.getRemark())
                    .createBy(SysUserHolder.getUser().getUserId())
                    .build());
        }
        return updated;
    }

    @Override
    @DistributedLock(key = RECORD_FILE_UPLOAD_LOCK)
    public RecordUploadItemVO recordItemUpload(MultipartFile file) {
        if (!Objects.requireNonNull(file.getOriginalFilename()).contains(RecordConstant.FILE_TYPE)) {
            throw new BmosException(LimsResponseCode.FILE_ANALYSIS_FILE_TYPE_ERROR);
        }
        try {
            File temp = File.createTempFile(RecordConstant.TEMPORARY_FOLDER, RecordConstant.FILE_TYPE);
            file.transferTo(temp);

            List<DocxOutlineInfo> infos = DocxSplitUtil2.splitDocx(temp);
            if (CollectionUtil.isEmpty(infos)){
                return null;
            }
            String recordKey = IdUtils.getSnowflakeStr() + "_" + System.currentTimeMillis();
            // 保存源文件
            String filePath = minioFileClient.uploadFile(MinioBucket.METHOD_TEMPLATE_BUCKET, temp, String.format(RECORD_FILE_SAVE_PATH, recordKey));
            log.info("批记录文件已保存至:{} -> {}", file.getOriginalFilename(), filePath);
            temp.delete();
            DocxOutlineInfo firstItem = infos.get(0);
            RecordUploadItemVO vo = new RecordUploadItemVO();
            vo.setItemType(RecordItemTypeEnum.CONTENT.getType());
            vo.setFirstDifferent(firstItem.getFirstDifferent());
            vo.setOddAndEvenDifferent(firstItem.getOddAndEvenDifferent());
            vo.setPageNumberStyle(firstItem.getPageNumberStyle());
            vo.setDocxFooter(firstItem.getDocxFooter());
            vo.setDocxHeader(firstItem.getDocxHeader());
            vo.setFilePath(filePath);
            vo.setStyle(firstItem.getStyle());
            vo.setFileContent(firstItem.getFileContent());
            return vo;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BmosException(LimsResponseCode.FILE_ANALYSIS_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RecordItemDetailVO copyRecordItem(Long itemId, String itemName) {
        Long id = CustomIdGenerator.nextId();
        BatchRecordItem item = itemService.selectItem(itemId);
        List<BatchRecordComponent> componentList = componentService.selectComponentList(itemId, id);
        item.setId(id);
        item.setName(itemName);
        item.setMaxNumber(RecordConstant.ZERO);
        item.setSort(item.getSort() + RecordConstant.ONE);
        itemService.saveOrUpdateOne(item);
        componentService.saveOrUpdateComponent(componentList);
        return getRecordItemList(item.getRecordVersionId());
    }

    /**
     * 处理业务组件parentId
     *
     * @param businessComponents
     */
    private void recHandleId(List<ComponentListDTO> businessComponents, ComponentListDTO parent, List<ComponentListDTO> flatBusinessComponents) {
        businessComponents.forEach(component -> {
            flatBusinessComponents.add(component);
            component.setId(ObjectUtil.isNull(component.getId()) ? IdUtils.getSnowflake() : component.getId());
            if (ObjectUtil.isNotNull(parent)) {
                component.setParentId(parent.getId());
                component.setRecordItemId(parent.getRecordItemId());
                component.setRecordVersionId(parent.getRecordVersionId());
                component.setRecordVersion(parent.getRecordVersion());
                component.setRecordId(parent.getRecordId());
            }
            if (CollUtil.isNotEmpty(component.getChildren())) {
                recHandleId(component.getChildren(), component, flatBusinessComponents);
            }
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RecordItemDetailVO deleteRecordItem(Long itemId) {
        BatchRecordItem item = itemService.selectItem(itemId);
        if (!itemService.deleteItem(item)) {
            throw new BmosException(LimsResponseCode.RECORD_ITEM_UPDATE_ERROR);
        }
        //删除大表字段
        parseService.deleteParseById(itemId);
        //删除主键
        componentService.deleteCompoenent(item.getItemId(), item.getRecordVersionId());
        return getRecordItemList(item.getRecordVersionId());
    }

    @Override
    public List<SelectRecorVO> queryListRecordByProductId(Long productId) {
        // 已废弃：产品维度查询
        return Collections.emptyList();
    }

    @Override
    public List<SelectRecorVO> queryListRecordByParameterId(Long parameterId) {
        List<InspectMethod> list = inspectMethodMapper.selectList(new com.bmos.mybatis.query.LambdaQueryWrapperX<InspectMethod>()
                .eq(InspectMethod::getParameterId, parameterId));
        List<Long> recordIds = CollectionUtils.convertList(list, InspectMethod::getRecordId);
        List<Long> deptIds = platformApiAdaptor.deptIds();
        if (CollUtil.isEmpty(recordIds) || CollUtil.isEmpty(deptIds)) {
            return Collections.emptyList();
        }
        List<BatchRecord> batchRecordList = recordMapper.selectByRecordIdList(recordIds, deptIds);
        return batchRecordList.stream().map(item -> {
            SelectRecorVO vo = new SelectRecorVO();
            vo.setName(item.getName());
            vo.setValue(item.getId());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public SaveSingleItemVO saveSingleItem(RecordItemSingleSaveDTO dto) {
        BatchRecordItem sort = itemService.selectItemMaxSort(dto.getRecordVersionId());
        //保存记录项基础字段
        BatchRecordItem item = RecordItemConvert.INSTANCE.convertToItemDo(dto);
        item.setItemId(IdUtils.getSnowflake());
        item.setItemType(RecordItemTypeEnum.CONTENT.getType());
        item.setSort(ObjectUtil.isNotEmpty(sort) ? sort.getSort() + RecordConstant.ONE : RecordConstant.ONE);
        itemService.saveOrUpdateOne(item);
        //保存大表字段
        BatchRecordParse parse = RecordParseConvert.INSTANCE.convertToParse(dto);
        parse.setId(item.getId());
        parseService.saveOrUpdateOne(parse);
        return RecordItemConvert.INSTANCE.convert2SaveVO(item);
    }

    @Override
    @DistributedLock(expression = "#dto.id")
    @Transactional(rollbackFor = Exception.class)
    public void editSingleItem(RecordItemSingleEditDTO dto) {
        BatchRecordItem batchRecordItem = itemService.selectItem(dto.getId());
        if (batchRecordItem == null) {
            throw new BmosException(LimsResponseCode.RECORD_ITEM_NOT_EXIST);
        }
        BatchRecordVersion batchRecordVersion = versionService.queryById(batchRecordItem.getRecordVersionId());
        // 保存更新删除组件
        updateItemComponents(dto, batchRecordItem, batchRecordVersion);
        BatchRecordItem items = RecordItemConvert.INSTANCE.convertToItemListDO(dto);
        BatchRecordParse parses = RecordParseConvert.INSTANCE.convertToParseComponent(dto);
        //保存记录项
        itemService.saveOrUpdateOne(items);
        //保存记录项大表字段
        parseService.saveOrUpdateOne(parses);
        auditOperationLogService.save(AuditOperationLogEntity.builder()
                .module(AuditBusinessModule.METHOD_AUDIT.name())
                .businessId(batchRecordItem.getRecordVersionId())
                .operationType(OperationType.REDACT.getValue())
                .remark(null)
                .createBy(SysUserHolder.getUser().getUserId())
                .build());
    }

    @Override
    public List<RecordExpressionBindTreeNodeVO> getExpressionTreeByRecordId(Long id) {
        ResponseInfo<List<ExpressionTreeNodeVO>> listResponseInfo = FeignUtils.handleRequest(data ->
                expressionFeignClient.getFullExpressionAndCategoryList(data), false);
        List<ExpressionTreeNodeVO> data = listResponseInfo.getData();
        if (CollUtil.isEmpty(data)) {
            return new ArrayList<>();
        }
        List<BatchRecordExpression> relations = batchRecordExpressionMapper.selectByRecordId(id);
        List<Long> expressionIdList = CollectionUtils.convertList(relations, BatchRecordExpression::getExpressionId);
        List<RecordExpressionBindTreeNodeVO> result = BatchRecordCategoryConvert.INSTANCE.convertToRecordExpressionBindTreeNodeVO(data, expressionIdList);
        return TreeUtil.buildTree(result, false);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindExpression(RecordBindExpressionDTO dto) {
        BatchRecord batchRecord = recordMapper.selectById(dto.getId());
        if (batchRecord == null) {
            return;
        }
        // 清除旧绑定关系
        batchRecordExpressionMapper.deleteByRecordId(dto.getId());
        if (CollUtil.isEmpty(dto.getExpressionIdList())) {
            return;
        }
        batchRecordExpressionMapper.insertBatch(dto.getExpressionIdList().stream().map(e->{
            BatchRecordExpression relation = new BatchRecordExpression();
            relation.setRecordId(dto.getId());
            relation.setExpressionId(e);
            return relation;
        }).collect(Collectors.toList()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void expressionBindBatchRecord(ExpressionBindRecordDTO dto) {
        batchRecordExpressionMapper.deleteByExpressionId(dto.getId());
        if (CollUtil.isEmpty(dto.getRecordIdList())) {
            return;
        }
        batchRecordExpressionMapper.insertBatch(dto.getRecordIdList().stream().map(e->{
            BatchRecordExpression relation = new BatchRecordExpression();
            relation.setExpressionId(dto.getId());
            relation.setRecordId(e);
            return relation;
        }).collect(Collectors.toList()));
    }

    @Override
    public List<BatchRecordTreeNodeVO> getRecordTreeByExpressionId(Long expressionId) {
        List<BatchRecordCategory> batchRecordCategories = categoryService.selectCategory();
        List<BatchRecord> batchRecords = recordMapper.selectListWithPermission(platformApiAdaptor.deptIds());
        List<BatchRecordExpression> relations = batchRecordExpressionMapper.selectByExpressionId(expressionId);
        Set<Long> boundList = CollectionUtils.convertSet(relations, BatchRecordExpression::getRecordId);
        List<BatchRecordTreeNodeVO> categories = batchRecordCategories.stream().map(e -> {
            BatchRecordTreeNodeVO vo = BatchRecordCategoryConvert.INSTANCE.convertToRecordTreeNodeVO(e);
            vo.setCategoryFlag(true);
            return vo;
        }).collect(Collectors.toList());
        List<BatchRecordTreeNodeVO> collect = batchRecords.stream().map(e -> {
            BatchRecordTreeNodeVO vo = BatchRecordCategoryConvert.INSTANCE.convertToRecordTreeNodeVO(e);
            vo.setCategoryFlag(false);
            vo.setBound(boundList.contains(e.getId()));
            return vo;
        }).collect(Collectors.toList());
        categories.addAll(collect);
        return TreeUtil.buildTree(categories, false);
    }

    @Override
    public List<Long> getBoundRecordIdList(Long expressionId) {
        List<BatchRecordExpression> batchRecordExpressions = batchRecordExpressionMapper.selectByExpressionId(expressionId);
        return CollectionUtils.convertList(batchRecordExpressions, BatchRecordExpression::getRecordId);
    }

    @Override
    public void downloadByUrl(HttpServletResponse response, String url) throws Exception {
        if (StrUtil.isBlank(url)) {
            return;
        }
        minioFileClient.download(MinioBucket.METHOD_TEMPLATE_BUCKET, url, response);
    }

    @Override
    public List<Long> getRecordBoundExpressionIdList(Long id) {
        List<BatchRecordExpression> batchRecordExpressions = batchRecordExpressionMapper.selectByRecordId(id);
        return CollectionUtils.convertList(batchRecordExpressions, BatchRecordExpression::getExpressionId);
    }

    private void updateItemComponents(RecordItemSingleEditDTO dto, BatchRecordItem batchRecordItem, BatchRecordVersion batchRecordVersion) {
        List<ComponentListDTO> componentList = dto.getComponentList();
        List<BatchRecordComponent> components =
                componentService.selectByVersionAndItem(batchRecordItem.getRecordVersionId(), batchRecordItem.getItemId());
        List<Long> inDB = CollectionUtils.convertList(components, BatchRecordComponent::getId);
        if (CollUtil.isNotEmpty(componentList)) {
            componentList.forEach(component -> {
                component.setId(ObjectUtil.isNull(component.getId()) ? IdUtils.getSnowflake() : component.getId());
                component.setRecordItemId(batchRecordItem.getItemId());
                component.setRecordVersionId(batchRecordItem.getRecordVersionId());
                component.setRecordVersion(batchRecordVersion.getVersion());
                component.setRecordId(batchRecordVersion.getRecordId());
            });
        }
        List<ComponentListDTO> list = componentList.stream().filter(e -> CollUtil.isEmpty(e.getChildren())).collect(Collectors.toList());
        List<ComponentListDTO> businessComponents = componentList.stream().filter(e -> CollUtil.isNotEmpty(e.getChildren())).collect(Collectors.toList());
        List<ComponentListDTO> flatBusinessComponents = new ArrayList<>();
        recHandleId(businessComponents, null, flatBusinessComponents);
        list.addAll(flatBusinessComponents);
        inDB.removeAll(CollectionUtils.convertList(list, ComponentListDTO::getId));
        if (CollUtil.isNotEmpty(inDB)) {
            componentService.deleteByIdList(inDB);
        }
        componentService.saveOrUpdateComponent(RecordComponentConvert.INSTANCE.convertToDoList(list));
    }

    /**
     * 将页眉/页脚内容转换为HTML并封装为 DocxHeaderFooterItem
     * 说明：不进行页码占位等替换，保持HTML原样，水平对齐默认使用传入alignment
     */
    private DocxHeaderFooterItem buildHeaderFooterItemHtml(HeaderFooter headerFooter, int alignment) {
        DocxHeaderFooterItem item = new DocxHeaderFooterItem();
        if (headerFooter == null) {
            return item;
        }
        try {
            Document fragment = new Document();
            NodeCollection childNodes = headerFooter.getChildNodes(NodeType.ANY, true);
            for (Object childNode : childNodes) {
                Node node = (Node) childNode;
                fragment.getFirstSection().getBody().appendChild(fragment.importNode(node, true));
            }
            HtmlSaveOptions opts = new HtmlSaveOptions();
            opts.setExportImagesAsBase64(true);
            opts.setCssStyleSheetType(CssStyleSheetType.INLINE);
            java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
            fragment.save(out, opts);
            String html = out.toString().replace("&#xa0;", " ");
            item.setContent(html);
            item.setPageCodeHorizontalAlignment(alignment);
        } catch (Exception e) {
            // 忽略异常，返回空内容
        }
        return item;
    }
}
