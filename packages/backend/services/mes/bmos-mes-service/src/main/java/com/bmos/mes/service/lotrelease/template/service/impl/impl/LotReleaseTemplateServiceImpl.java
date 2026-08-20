package com.bmos.mes.service.lotrelease.template.service.impl.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.common.exception.BmosException;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.config.minio.MinioFileClient;
import com.bmos.mes.service.config.minio.constants.MinioBucket;
import com.bmos.mes.service.dataset.mapper.IDatasetPointTemplateRelationMapper;
import com.bmos.mes.service.dataset.util.XlsxRenderUtil;
import com.bmos.mes.service.lotrelease.manage.convert.LotReleaseConvert;
import com.bmos.mes.service.lotrelease.template.convert.LotReleaseTemplateCategoryConvert;
import com.bmos.mes.service.lotrelease.template.dto.*;
import com.bmos.mes.service.lotrelease.template.enums.LotReleaseTemplateOperateType;
import com.bmos.mes.service.lotrelease.template.enums.LotReleaseTemplateVersionStatus;
import com.bmos.mes.service.lotrelease.template.mapper.*;
import com.bmos.mes.service.lotrelease.template.model.LotReleaseTemplate;
import com.bmos.mes.service.lotrelease.template.model.LotReleaseTemplateCategory;
import com.bmos.mes.service.lotrelease.template.model.LotReleaseTemplateHistory;
import com.bmos.mes.service.lotrelease.template.model.LotReleaseTemplateVersion;
import com.bmos.mes.service.lotrelease.template.service.ILotReleaseTemplateService;
import com.bmos.mes.service.lotrelease.template.vo.*;
import com.bmos.mes.service.permission.dto.ResourcePermissionSaveDTO;
import com.bmos.mes.service.permission.service.ResourcePermissionService;
import com.bmos.mybatis.page.CommonPage;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/8/26 18:45
 */
@Service
@Slf4j
public class LotReleaseTemplateServiceImpl implements ILotReleaseTemplateService {

    private static final String LOG_PREFIX = "[批签发模板]";

    private static final String[] TEMPLATE_SUFFIXES = {"xls", "xlsx", "xlsm"};

    @Resource
    private ILotReleaseTemplateCategoryMapper lotReleaseTemplateCategoryMapper;

    @Resource
    private ILotReleaseTemplateMapper lotReleaseTemplateMapper;

    @Resource
    private ILotReleaseTemplateVersionMapper lotReleaseTemplateVersionMapper;

    @Resource
    private ILotReleaseTemplateProcessRelationMapper lotReleaseTemplateProcessRelationMapper;

    @Resource
    private MinioFileClient minioFileClient;

    @Resource
    private PlatformApiAdaptor platformApiAdaptor;

    @Resource
    private ResourcePermissionService resourcePermissionService;

    @Resource
    private ILotReleaseTemplateHistoryMapper lotReleaseTemplateHistoryMapper;

    @Resource
    private IDatasetPointTemplateRelationMapper datasetPointTemplateRelationMapper;

    @Override
    public CommonPage<LotReleaseTemplatePageVO> queryPage(@Validated LotReleaseTemplatePageQuery pageQuery) {
        List<Long> lotReleaseTemplateCategoryIds = lotReleaseTemplateCategoryMapper.listAllChildren(pageQuery.getCategoryId())
                .stream()
                .map(LotReleaseTemplateCategory::getId)
                .collect(Collectors.toList());
        lotReleaseTemplateCategoryIds.add(pageQuery.getCategoryId());
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize(), pageQuery.getOrderSql());
        List<Long> deptIds = platformApiAdaptor.deptIds();
        List<LotReleaseTemplate> list = lotReleaseTemplateMapper.queryPage(pageQuery, lotReleaseTemplateCategoryIds, deptIds);
        if (CollectionUtil.isEmpty(list)) {
            return CommonPage.convertPage(new ArrayList<>());
        }
        List<Long> categoryIds = list.stream().map(LotReleaseTemplate::getCategoryId).collect(Collectors.toList());
        Map<Long, String> pathMap = lotReleaseTemplateCategoryMapper.getNamePath(categoryIds)
                .stream()
                .collect(Collectors.toMap(LotReleaseTemplateCategoryPath::getId, LotReleaseTemplateCategoryPath::getNamePath, (v1, v2) -> v1));
        CommonPage<LotReleaseTemplate> page = CommonPage.convertPage(list);
        CommonPage<LotReleaseTemplatePageVO> result = LotReleaseTemplateCategoryConvert.INSTANCE.convertToVO(page);
        result.getList().forEach(item -> {
            item.setCategoryNamePath(pathMap.get(item.getCategoryId()));
        });
        return result;
    }

    @Override
    public CommonPage<LotReleaseTemplateVersionPageVO> queryVersionPage(LotReleaseTemplateVersionPageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize());
        List<LotReleaseTemplateVersion> lotReleaseTemplateVersions = lotReleaseTemplateVersionMapper.queryVersionPage(pageQuery);
        CommonPage<LotReleaseTemplateVersion> lotReleaseTemplateVersionCommonPage = CommonPage.convertPage(lotReleaseTemplateVersions);
        return LotReleaseTemplateCategoryConvert.INSTANCE.convertToPageVO(lotReleaseTemplateVersionCommonPage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindProcess(LotReleaseTemplateBindProcessDTO dto) {
        LotReleaseTemplate lotReleaseTemplate = lotReleaseTemplateMapper.selectById(dto.getLotReleaseTemplateId());
        if (lotReleaseTemplate == null){
            throw new BmosException(MesResponseCode.LOT_RELEASE_TEMPLATE_NOT_EXIST);
        }
        lotReleaseTemplateProcessRelationMapper.bindProcess(dto.getProcessIds(), dto.getLotReleaseTemplateId());
    }

    @Override
    public String uploadTemplate(MultipartFile file) throws Exception {
        String originalFilename = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(originalFilename);
        if (!ArrayUtil.contains(TEMPLATE_SUFFIXES, extension)){
            throw new BmosException(MesResponseCode.LOT_RELEASE_TEMPLATE_FILE_TYPE_ERROR);
        }
        File tempFile = File.createTempFile(UUID.randomUUID() + "_" + System.currentTimeMillis(), "." + extension);
        file.transferTo(tempFile);
        String fileUrl = minioFileClient.uploadFile(MinioBucket.BMOS_LOT_RELEASE, tempFile, "/template/" + tempFile.getName());

        // 解析数据点
        Map<String, List<XlsxRenderUtil.CellPosition>> map = XlsxRenderUtil.scanPlaceHolder(tempFile);
        datasetPointTemplateRelationMapper.saveRelations(map.keySet(), fileUrl);
        return fileUrl;
    }

    @Override
    public void downloadTemplate(HttpServletResponse response, Long id) throws Exception {
        LotReleaseTemplateVersion lotReleaseTemplateVersion = lotReleaseTemplateVersionMapper.selectById(id);
        if (lotReleaseTemplateVersion == null){
            throw new BmosException(MesResponseCode.LOT_RELEASE_TEMPLATE_VERSION_NOT_EXIST);
        }
        String templateUrl = lotReleaseTemplateVersion.getTemplateUrl();
        if (StrUtil.isBlank(templateUrl)){
            return;
        }
        minioFileClient.download(MinioBucket.BMOS_LOT_RELEASE, templateUrl, response);

        // 保存历史
        lotReleaseTemplateHistoryMapper.insert(LotReleaseTemplateHistory.create(
                lotReleaseTemplateVersion.getId(),
                LotReleaseTemplateOperateType.DOWNLOAD,
                null,
                templateUrl
        ));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void makeSure(Long id) {
        log.info("{}确认模板", LOG_PREFIX);
        LotReleaseTemplateVersion lotReleaseTemplateVersion = lotReleaseTemplateVersionMapper.selectById(id);
        if (lotReleaseTemplateVersion == null){
            throw new BmosException(MesResponseCode.LOT_RELEASE_TEMPLATE_VERSION_NOT_EXIST);
        }
        if (!Objects.equals(lotReleaseTemplateVersion.getStatus(), LotReleaseTemplateVersionStatus.EDIT)){
            throw new BmosException(MesResponseCode.LOT_RELEASE_TEMPLATE_VERSION_CANT_MAKE_SURE);
        }
        lotReleaseTemplateVersion.setStatus(LotReleaseTemplateVersionStatus.MAKE_SURE);
        lotReleaseTemplateVersionMapper.updateById(lotReleaseTemplateVersion);
        lotReleaseTemplateHistoryMapper.insert(LotReleaseTemplateHistory.create(
                lotReleaseTemplateVersion.getId(),
                LotReleaseTemplateOperateType.MAKE_SURE,
                null,
                lotReleaseTemplateVersion.getTemplateUrl()
        ));

        // 确认的同时设为默认
        this.makeDefault(lotReleaseTemplateVersion.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void makeDefault(Long id) {
        log.info("{}设为默认模板", LOG_PREFIX);
        LotReleaseTemplateVersion lotReleaseTemplateVersion = lotReleaseTemplateVersionMapper.selectById(id);
        if (lotReleaseTemplateVersion == null){
            throw new BmosException(MesResponseCode.LOT_RELEASE_TEMPLATE_VERSION_NOT_EXIST);
        }
        if (lotReleaseTemplateVersion.getStatus() != LotReleaseTemplateVersionStatus.MAKE_SURE){
            throw new BmosException(MesResponseCode.LOT_RELEASE_TEMPLATE_VERSION_CANT_MAKE_DEFAULT);
        }
        if (lotReleaseTemplateVersion.getIsDefault()){
            return;
        }
        List<LotReleaseTemplateVersion> list = new ArrayList<>();
        LotReleaseTemplateVersion defaultTemplateVersion = lotReleaseTemplateVersionMapper.selectDefaultByTemplateId(lotReleaseTemplateVersion.getTemplateId());
        if (defaultTemplateVersion != null){
            defaultTemplateVersion.setIsDefault(false);
            list.add(defaultTemplateVersion);
        }
        lotReleaseTemplateVersion.setIsDefault(true);
        list.add(lotReleaseTemplateVersion);
        lotReleaseTemplateVersionMapper.updateBatch(list);

        lotReleaseTemplateHistoryMapper.insert(LotReleaseTemplateHistory.create(
                lotReleaseTemplateVersion.getId(),
                LotReleaseTemplateOperateType.MAKE_DEFAULT,
                null,
                lotReleaseTemplateVersion.getTemplateUrl()
        ));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void scrap(Long id) {
        log.info("{}作废模板", LOG_PREFIX);
        LotReleaseTemplateVersion lotReleaseTemplateVersion = lotReleaseTemplateVersionMapper.selectById(id);
        if (lotReleaseTemplateVersion == null){
            throw new BmosException(MesResponseCode.LOT_RELEASE_TEMPLATE_VERSION_NOT_EXIST);
        }
        if (lotReleaseTemplateVersion.getIsDefault()){
            lotReleaseTemplateVersion.setIsDefault(false);
        }
        lotReleaseTemplateVersion.setStatus(LotReleaseTemplateVersionStatus.SCRAP);
        lotReleaseTemplateVersionMapper.updateById(lotReleaseTemplateVersion);
        lotReleaseTemplateHistoryMapper.insert(LotReleaseTemplateHistory.create(
                lotReleaseTemplateVersion.getId(),
                LotReleaseTemplateOperateType.SCRAP,
                null,
                lotReleaseTemplateVersion.getTemplateUrl()
        ));
    }

    @Override
    public List<LogReleaseTemplateVersionHistoryVO> showHistory(Long id) {
        List<LotReleaseTemplateHistory> lotReleaseTemplateHistories = lotReleaseTemplateHistoryMapper.queryHistoryByTemplateVersionId(id);
        return LotReleaseConvert.INSTANCE.convertToTemplateHistoryVO(lotReleaseTemplateHistories);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createTemplate(LotReleaseTemplateCreateDTO dto) {
        log.info("{}创建模板", LOG_PREFIX);
        Long categoryId = dto.getCategoryId();
        if (categoryId != null){
            LotReleaseTemplateCategory lotReleaseTemplateCategory = lotReleaseTemplateCategoryMapper.selectById(categoryId);
            if (lotReleaseTemplateCategory == null){
                throw new BmosException(MesResponseCode.LOT_RELEASE_TEMPLATE_CATEGORY_NOT_EXIST);
            }
        }
        LotReleaseTemplate template = lotReleaseTemplateMapper.selectByName(dto.getName());
        if (template != null){
            throw new BmosException(MesResponseCode.LOT_RELEASE_TEMPLATE_NAME_EXIST);

        }
        log.info("{}模板不存在,新增模板和版本:{}{}", LOG_PREFIX, dto.getName(), dto.getVersion());
        LotReleaseTemplate lotReleaseTemplate = new LotReleaseTemplate();
        lotReleaseTemplate.setCategoryId(dto.getCategoryId());
        lotReleaseTemplate.setName(dto.getName());
        lotReleaseTemplateMapper.insert(lotReleaseTemplate);
        LotReleaseTemplateVersion lotReleaseTemplateVersion = createLotReleaseTemplateVersion(dto, lotReleaseTemplate);
        lotReleaseTemplateVersionMapper.insert(lotReleaseTemplateVersion);

        // 保存数据权限
        resourcePermissionService.save(ResourcePermissionSaveDTO.builder()
                .resourceId(lotReleaseTemplate.getId())
                .deptIds(dto.getDeptIds())
                .build());

        // 保存历史
        lotReleaseTemplateHistoryMapper.insert(LotReleaseTemplateHistory.create(
                lotReleaseTemplateVersion.getId(),
                LotReleaseTemplateOperateType.CREATE,
                dto.getRemark(),
                dto.getTemplateUrl()
        ));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createTemplateVersion(LotReleaseTemplateVersionCreateDTO dto) {
        log.info("{}创建模板版本", LOG_PREFIX);
        LotReleaseTemplate template = lotReleaseTemplateMapper.selectById(dto.getTemplateId());
        if (template == null){
            throw new BmosException(MesResponseCode.LOT_RELEASE_TEMPLATE_NOT_EXIST);
        }
        LotReleaseTemplateVersion version = lotReleaseTemplateVersionMapper.selectByTemplateIdAndVersion(template.getId(), dto.getVersion());
        if (version != null){
            throw new BmosException(MesResponseCode.LOT_RELEASE_TEMPLATE_VERSION_EXIST);
        }
        LotReleaseTemplateVersion lotReleaseTemplateVersion = new LotReleaseTemplateVersion();
        lotReleaseTemplateVersion.setTemplateId(dto.getTemplateId());
        lotReleaseTemplateVersion.setName(template.getName());
        lotReleaseTemplateVersion.setVersion(dto.getVersion());
        lotReleaseTemplateVersion.setTemplateUrl(dto.getTemplateUrl());
        lotReleaseTemplateVersion.setRemark(dto.getRemark());
        lotReleaseTemplateVersion.setStatus(LotReleaseTemplateVersionStatus.EDIT);
        lotReleaseTemplateVersion.setIsDefault(false);
        lotReleaseTemplateVersionMapper.insert(lotReleaseTemplateVersion);
        // 保存历史
        lotReleaseTemplateHistoryMapper.insert(LotReleaseTemplateHistory.create(
                lotReleaseTemplateVersion.getId(),
                LotReleaseTemplateOperateType.CREATE_VERSION,
                dto.getRemark(),
                dto.getTemplateUrl()
        ));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTemplateFile(LotReleaseTemplateEditDTO dto) {
        LotReleaseTemplateVersion lotReleaseTemplateVersion = lotReleaseTemplateVersionMapper.selectById(dto.getLotReleaseTemplateVersionId());
        if (lotReleaseTemplateVersion == null){
            throw new BmosException(MesResponseCode.LOT_RELEASE_TEMPLATE_VERSION_NOT_EXIST);
        }
        if (lotReleaseTemplateVersion.getStatus() != LotReleaseTemplateVersionStatus.EDIT){
            throw new BmosException(MesResponseCode.LOT_RELEASE_TEMPLATE_VERSION_CANT_EDIT);
        }
        lotReleaseTemplateVersion.setTemplateUrl(dto.getTemplateUrl());
        lotReleaseTemplateVersion.setRemark(dto.getRemark());
        lotReleaseTemplateVersionMapper.updateById(lotReleaseTemplateVersion);

        // 保存历史
        lotReleaseTemplateHistoryMapper.insert(LotReleaseTemplateHistory.create(
                lotReleaseTemplateVersion.getId(),
                LotReleaseTemplateOperateType.UPLOAD,
                dto.getRemark(),
                dto.getTemplateUrl()
        ));
    }

    @Override
    public List<LotReleaseTemplateLinkVO> listByProcessId(Long processId) {
        if (processId == null){
            return new ArrayList<>();
        }
        List<Long> templateIds = lotReleaseTemplateProcessRelationMapper.selectTemplateIdsByProcessId(processId);
        if (CollectionUtil.isEmpty(templateIds)){
            return new ArrayList<>();
        }
        List<LotReleaseTemplate> lotReleaseTemplates = lotReleaseTemplateMapper.selectBatchIds(templateIds);
        List<LotReleaseTemplateLinkVO> list = LotReleaseConvert.INSTANCE.convertToLinkVO(lotReleaseTemplates);
        Map<Long, List<LotReleaseTemplateVersion>> templateVersionMap = lotReleaseTemplateVersionMapper.selectByTemplateIds(templateIds)
                .stream()
                .collect(Collectors.groupingBy(LotReleaseTemplateVersion::getTemplateId));
        list.forEach(item -> {
            item.setList(templateVersionMap.getOrDefault(item.getId(), new ArrayList<>())
                    .stream()
                    .map(LotReleaseConvert.INSTANCE::convertToVersionLinkVO)
                    .collect(Collectors.toList()));
        });
        return list;
    }

    @Override
    public List<Long> listProcessIdByTemplateId(Long templateId) {
        if (templateId == null){
            return new ArrayList<>();
        }
        return lotReleaseTemplateProcessRelationMapper.selectProcessIdsByTemplateId(templateId);
    }

    @Override
    public List<LotReleaseTemplateVersionItemVO> listVersionByTemplateId(Long templateId) {
        if (templateId == null){
            return new ArrayList<>();
        }
        List<LotReleaseTemplateVersion> list = lotReleaseTemplateVersionMapper.selectByTemplateId(templateId);
        return LotReleaseConvert.INSTANCE.convertToVersionItemVO(list);
    }

    private @NotNull LotReleaseTemplateVersion createLotReleaseTemplateVersion(LotReleaseTemplateCreateDTO dto, LotReleaseTemplate lotReleaseTemplate) {
        LotReleaseTemplateVersion lotReleaseTemplateVersion = new LotReleaseTemplateVersion();
        lotReleaseTemplateVersion.setTemplateId(lotReleaseTemplate.getId());
        lotReleaseTemplateVersion.setName(lotReleaseTemplate.getName());
        lotReleaseTemplateVersion.setVersion(dto.getVersion());
        lotReleaseTemplateVersion.setTemplateUrl(dto.getTemplateUrl());
        lotReleaseTemplateVersion.setRemark(dto.getRemark());
        lotReleaseTemplateVersion.setStatus(LotReleaseTemplateVersionStatus.EDIT);
        lotReleaseTemplateVersion.setIsDefault(false);
        return lotReleaseTemplateVersion;
    }
}
