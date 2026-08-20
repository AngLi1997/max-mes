package com.bmos.mes.service.record.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.cache.redis.lock.DistributedLock;
import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.tree.TreeUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.id.IdUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.file.docx.model.DocxOutlineInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.common.constant.RecordConstant;
import com.bmos.mes.common.enums.record.RecordItemTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.config.minio.MinioFileClient;
import com.bmos.mes.service.config.minio.constants.MinioBucket;
import com.bmos.mes.service.operation.history.annotation.OperationHistory;
import com.bmos.mes.service.operation.history.aspect.OperationHistoryContext;
import com.bmos.mes.service.operation.history.enums.BusinessModule;
import com.bmos.mes.service.operation.history.enums.OperationType;
import com.bmos.mes.service.permission.dto.ResourcePermissionSaveDTO;
import com.bmos.mes.service.permission.service.ResourcePermissionService;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.platform.expression.PlatformExpressionFeignClient;
import com.bmos.mes.service.platform.expression.vo.ExpressionTreeNodeVO;
import com.bmos.mes.service.record.DocxValidator;
import com.bmos.mes.service.record.convert.*;
import com.bmos.mes.service.record.dto.*;
import com.bmos.mes.service.record.mapper.BatchRecordExpressionMapper;
import com.bmos.mes.service.record.mapper.BatchRecordMapper;
import com.bmos.mes.service.record.model.*;
import com.bmos.mes.service.record.service.*;
import com.bmos.mes.service.record.util.DocxSplitUtil2;
import com.bmos.mes.service.record.vo.*;
import com.bmos.mybatis.CustomIdGenerator;
import com.bmos.mybatis.page.CommonPage;
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
    private BatchRecordProductService productService;

    @Autowired
    private PlatformApiAdaptor platformApiAdaptor;

    @Resource
    private MinioFileClient minioFileClient;

    @Resource
    private BatchRecordExpressionMapper batchRecordExpressionMapper;

    @Resource
    private PlatformExpressionFeignClient expressionFeignClient;


    @Override
    @DistributedLock(key = RECORD_FILE_UPLOAD_LOCK)
    public RecordUploadVo fileUpload(MultipartFile file) {
        List<RecordUploadItemVO> voList = new ArrayList<>();
        RecordUploadVo uploadVo = new RecordUploadVo();
        try {
            String originalFilename = file.getOriginalFilename();
            if (!originalFilename.contains(RecordConstant.FILE_TYPE)) {
                throw new BmosException(MesResponseCode.FILE_ANALYSIS_FILE_TYPE_ERROR);
            }
            File originFile = File.createTempFile(RecordConstant.TEMPORARY_FOLDER, RecordConstant.FILE_TYPE);
            file.transferTo(originFile);
            long start = System.currentTimeMillis();
            System.out.println("开始切分 " + start);
            List<RecordFormatResult> validate = DocxValidator.validate(originFile);
            uploadVo.setFormatResults(validate);
            File formatFile = DocxValidator.transAndClearComments(originFile);
            // 解析
            List<DocxOutlineInfo> docxMarkInfos = DocxSplitUtil2.splitDocx(formatFile);
            long end = System.currentTimeMillis();
            log.info("结束切分: {}", end);
            log.info("切分耗时: {}", (end - start) / 1000);
            String recordKey = IdUtils.getSnowflakeStr() + "_" + System.currentTimeMillis();
            // 保存源文件
            String originFilePath = minioFileClient.uploadFile(MinioBucket.RECORD_BUCKET, originFile, String.format(RECORD_FILE_SAVE_PATH, recordKey + COMMENT_SUFFIX));
            // 保存格式化后的文件
            String formatPath = minioFileClient.uploadFile(MinioBucket.RECORD_BUCKET, formatFile, String.format(RECORD_FILE_SAVE_PATH, recordKey));
            log.info("批记录文件已保存至: {}", originalFilename);
            log.info("批记录文件(无标注)已保存至: {}", formatPath);
            uploadVo.setOriginFilePath(originFilePath);
            uploadVo.setFilePath(formatPath);
            originFile.delete();
            formatFile.delete();
            for (DocxOutlineInfo info : docxMarkInfos) {
                RecordUploadItemVO vo = new RecordUploadItemVO();
                vo.setFileContent(info.getFileContent());
                vo.setName(info.getMarkName());
                vo.setItemType(info.getType());
                vo.setStyle(info.getStyle());
                vo.setFirstDifferent(info.getFirstDifferent());
                vo.setOddAndEvenDifferent(info.getOddAndEvenDifferent());
                vo.setPageNumberStyle(info.getPageNumberStyle());
                vo.setDocxHeader(info.getDocxHeader());
                vo.setDocxFooter(info.getDocxFooter());
                voList.add(vo);
            }
            uploadVo.setItemVO(voList);
        } catch (BmosException e) {
            throw e;
        } catch (Exception e){
            log.error("文件上传失败", e);
            throw new BmosException(MesResponseCode.RECORD_FILE_UPLOAD_ERROR);
        }
        return uploadVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationHistory(module = BusinessModule.BATCH_RECORD, operationType = OperationType.SAVE, remark = "#dto.remark", businessId = "#getVersionId")
    @OperationLog
    public BatchRecordSaveVO saveRecord(BatchRecordSaveDTO dto) {
        if (ObjectUtil.isEmpty(dto.getRecordId())) {
            dto.setRecordId(CustomIdGenerator.nextId());
            recordMapper.saveOrUpdateRecord(dto);
        }
        dto.setVersionId(ObjectUtil.isNotEmpty(dto.getVersionId()) ? dto.getVersionId() : CustomIdGenerator.nextId());
        BatchRecordVersion version = versionService.saveOrUpdateVersion(dto);
        if (CollUtil.isNotEmpty(dto.getDeptIds())) {
            resourcePermissionService.save(ResourcePermissionSaveDTO.builder()
                    .deptIds(dto.getDeptIds())
                    .resourceId(dto.getRecordId())
                    .build());
        }
        OperationHistoryContext.putVariable(dto, BatchRecordSaveDTO::getVersionId);
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
        }
        return CommonPage.convertPage(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationHistory(module = BusinessModule.BATCH_RECORD, operationType = OperationType.NULLIFY, remark = "#dto.remark", businessId = "#dto.id")
    @OperationLog
    public Boolean updateVersion(RecordVersionDTO dto) {
        return versionService.updateVersion(RecordVersionConvert.INSTANCE.convertToDo(dto));
    }

    @Override
    @DistributedLock(key = RECORD_FILE_UPLOAD_LOCK)
    public RecordUploadItemVO recordItemUpload(MultipartFile file) {
        if (!Objects.requireNonNull(file.getOriginalFilename()).contains(RecordConstant.FILE_TYPE)) {
            throw new BmosException(MesResponseCode.FILE_ANALYSIS_FILE_TYPE_ERROR);
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
            String filePath = minioFileClient.uploadFile(MinioBucket.RECORD_BUCKET, temp, String.format(RECORD_FILE_SAVE_PATH, recordKey));
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
            throw new BmosException(MesResponseCode.FILE_ANALYSIS_ERROR);
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
            throw new BmosException(MesResponseCode.RECORD_ITEM_UPDATE_ERROR);
        }
        //删除大表字段
        parseService.deleteParseById(itemId);
        //删除主键
        componentService.deleteCompoenent(item.getItemId(), item.getRecordVersionId());
        return getRecordItemList(item.getRecordVersionId());
    }

    @Override
    public List<SelectRecorVO> queryListRecordByProductId(Long productId) {
        List<Long> recordIdList = productService.getProductBindRecordIds(productId);
        List<Long> deptIds = platformApiAdaptor.deptIds();
        if (CollUtil.isEmpty(recordIdList) || CollUtil.isEmpty(deptIds)) {
            return Collections.emptyList();
        }
        List<BatchRecord> batchRecordList = recordMapper.selectByRecordIdList(recordIdList, deptIds);
        List<SelectRecorVO> voList = batchRecordList.stream().map(item -> {
            SelectRecorVO vo = new SelectRecorVO();
            vo.setName(item.getName());
            vo.setValue(item.getId());
            return vo;
        }).collect(Collectors.toList());
        return voList;
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
            throw new BmosException(MesResponseCode.RECORD_ITEM_NOT_EXIST);
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
        minioFileClient.download(MinioBucket.RECORD_BUCKET, url, response);
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
}
