package com.bmos.mes.service.plan.document.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.cache.redis.lock.DistributedLock;
import com.bmos.common.exception.BmosException;
import com.bmos.common.tree.TreeUtil;
import com.bmos.common.util.id.IdUtils;
import com.bmos.mes.common.constant.RecordConstant;
import com.bmos.mes.common.enums.plan.ProductPlanStartEnum;
import com.bmos.mes.common.enums.plan.TemplateVersionOperateTypeEnum;
import com.bmos.mes.common.enums.plan.TemplateVersionStatusEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.config.minio.MinioFileClient;
import com.bmos.mes.service.config.minio.constants.MinioBucket;
import com.bmos.mes.service.dataset.mapper.IDatasetPointTemplateRelationMapper;
import com.bmos.mes.service.dataset.util.DocxRenderUtil;
import com.bmos.mes.service.permission.dto.ResourcePermissionSaveDTO;
import com.bmos.mes.service.permission.service.ResourcePermissionService;
import com.bmos.mes.service.plan.document.controller.vo.*;
import com.bmos.mes.service.plan.document.convert.BatchTemplateConverter;
import com.bmos.mes.service.plan.document.mapper.BatchTemplateInfoMapper;
import com.bmos.mes.service.plan.document.mapper.BatchTemplateInfoProcessMapper;
import com.bmos.mes.service.plan.document.mapper.BatchTemplateVersionMapper;
import com.bmos.mes.service.plan.document.mapper.param.TemplateInfoParam;
import com.bmos.mes.service.plan.document.model.BatchTemplateInfo;
import com.bmos.mes.service.plan.document.model.BatchTemplateInfoProcess;
import com.bmos.mes.service.plan.document.model.BatchTemplateVersion;
import com.bmos.mes.service.plan.document.service.BatchTemplateCategoryService;
import com.bmos.mes.service.plan.document.service.BatchTemplateLogService;
import com.bmos.mes.service.plan.document.service.BatchTemplateService;
import com.bmos.mes.service.plan.document.service.dto.*;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.plan.info.service.PlanService;
import com.bmos.mybatis.page.CommonPage;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BatchTemplateServiceImpl implements BatchTemplateService {

    @Autowired
    private BatchTemplateInfoMapper templateInfoMapper;

    @Autowired
    BatchTemplateInfoProcessMapper batchTemplateInfoProcessMapper;

    @Autowired
    BatchTemplateVersionMapper batchTemplateVersionMapper;

    @Autowired
    MinioFileClient minioFileClient;

    @Autowired
    ResourcePermissionService resourcePermissionService;

    @Autowired
    BatchTemplateLogService batchTemplateLogService;

    @Autowired
    private BatchTemplateCategoryService batchTemplateCategoryService;

    @Autowired
    PlatformApiAdaptor platformApiAdaptor;

    @Autowired
    PlanService planService;

    @Resource
    private IDatasetPointTemplateRelationMapper datasetPointTemplateRelationMapper;

    @Override
    public String fileUpload(MultipartFile file) {
        try {
            File files = File.createTempFile(RecordConstant.TEMPORARY_FOLDER, RecordConstant.FILE_TYPE);
            file.transferTo(files);
            String path = IdUtils.getSnowflakeStr() + "_" + System.currentTimeMillis() + RecordConstant.FILE_TYPE;
            String fileUrl = minioFileClient.uploadFile(MinioBucket.BMOS_BATCH_TEMPLATE, files, path);
            // 解析占位符
            List<String> placeHolders = DocxRenderUtil.scanPlaceHolder(files);
            datasetPointTemplateRelationMapper.saveRelations(placeHolders, fileUrl);
            return fileUrl;
        }catch (Exception e){
            log.error("上传失败:", e);
            throw new BmosException(MesResponseCode.OPERATE_UPLOAD_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @DistributedLock(expression = "#dto.name")
    public void saveTemplate(TemplateSaveDTO dto) {
        // 上传文件
        // 模板信息名称需要唯一
        if (templateInfoMapper.existsByName(dto.getName())){
            throw new BmosException(MesResponseCode.PLAN_ARCHIVE_TEMPLATE_EXIST, dto.getName());
        }
        // 2. 插入模板信息
        BatchTemplateInfo templateInfo = BatchTemplateConverter.INSTANCE.convert2TemplateInfo(dto);
        templateInfoMapper.insert(templateInfo);
        // 3. 在模板信息下新增模板版本
        BatchTemplateVersion batchTemplateVersion = BatchTemplateConverter.INSTANCE.convert2TemplateVersion(dto, templateInfo.getId());
        batchTemplateVersion.setStatus(TemplateVersionStatusEnum.EDIT.getValue());
        batchTemplateVersion.setNormal(false);
        batchTemplateVersionMapper.insert(batchTemplateVersion);
        // 4.绑定数据权限
        this.templateInfoBindDataAuth(new TemplateInfoBindAuthDTO(templateInfo.getId(), dto.getDeptIds()));
        // 记录日志
        batchTemplateLogService.saveTemplateLog(new BatchTemplateLogSaveDTO(TemplateVersionOperateTypeEnum.ADD, dto.getRemark(),
                batchTemplateVersion.getId(), batchTemplateVersion.getPath()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTemplateVersion(TemplateVersionSaveDTO dto) {
        BatchTemplateInfo batchTemplateInfo = templateInfoMapper.selectById(dto.getTemplateInfoId());
            if (Objects.isNull(batchTemplateInfo)){
            throw new BmosException(MesResponseCode.PLAN_ARCHIVE_TEMPLATE_NOT_EXIST);
        }
        // 版本是否存在
        if (batchTemplateVersionMapper.existsByVersion(dto.getVersion(), dto.getTemplateInfoId())){
            throw new BmosException(MesResponseCode.PLAN_ARCHIVE_TEMPLATE_VERSION_EXIST, batchTemplateInfo.getName(), dto.getVersion());
        }
        BatchTemplateVersion batchTemplateVersion = BatchTemplateConverter.INSTANCE.convert2TemplateVersion(dto, batchTemplateInfo.getId());
        batchTemplateVersion.setStatus(TemplateVersionStatusEnum.EDIT.getValue());
        batchTemplateVersion.setNormal(false);
        batchTemplateVersionMapper.insert(batchTemplateVersion);
        // 记录日志
        batchTemplateLogService.saveTemplateLog(new BatchTemplateLogSaveDTO(TemplateVersionOperateTypeEnum.ADD, dto.getRemark(),
                batchTemplateVersion.getId(), batchTemplateVersion.getPath()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void uploadTemplateVersion(TemplateVersionUpdateDTO dto) {
        BatchTemplateVersion batchTemplateVersion = batchTemplateVersionMapper.selectById(dto.getTemplateVersionId());
        if (Objects.isNull(batchTemplateVersion)){
            // 当前版本不存在
            throw new BmosException(MesResponseCode.PLAN_ARCHIVE_TEMPLATE_VERSION_NOT_EXIST);
        }
        // 批记录只能在编辑状态下进行修改
        if (!TemplateVersionStatusEnum.EDIT.getValue().equals(batchTemplateVersion.getStatus())){
            throw new BmosException(MesResponseCode.PLAN_ARCHIVE_TEMPLATE_VERSION_NOT_EDIT, batchTemplateVersion.getVersion());
        }
        batchTemplateVersion.setRemark(dto.getRemark());
        batchTemplateVersion.setPath(dto.getPath());
        batchTemplateVersionMapper.updateById(batchTemplateVersion);
        // 记录日志
        batchTemplateLogService.saveTemplateLog(new BatchTemplateLogSaveDTO(TemplateVersionOperateTypeEnum.UPLOAD, dto.getRemark(),
                batchTemplateVersion.getId(), batchTemplateVersion.getPath()));
    }

    @Override
    public void downloadTemplateVersion(TemplateVersionOperateDTO dto, HttpServletResponse response) {
        BatchTemplateVersion batchTemplateVersion = batchTemplateVersionMapper.selectById(dto.getTemplateVersionId());
        if (Objects.isNull(batchTemplateVersion)){
            throw new BmosException(MesResponseCode.PLAN_ARCHIVE_TEMPLATE_VERSION_NOT_EXIST);
        }
        // 下载批记录模板
        doDownload(batchTemplateVersion, response);
        // 记录操作日志
        batchTemplateLogService.saveTemplateLog(new BatchTemplateLogSaveDTO(TemplateVersionOperateTypeEnum.DOWNLOAD, dto.getRemark(),
                batchTemplateVersion.getId(), batchTemplateVersion.getPath()));
    }

    @Override
    public void scrapTemplateVersion(TemplateVersionOperateDTO dto) {
        BatchTemplateVersion batchTemplateVersion = batchTemplateVersionMapper.selectById(dto.getTemplateVersionId());
        if (Objects.isNull(batchTemplateVersion)){
            // 当前版本不存在
            throw new BmosException(MesResponseCode.PLAN_ARCHIVE_TEMPLATE_VERSION_NOT_EXIST);
        }
        batchTemplateVersion.setStatus(TemplateVersionStatusEnum.SCRAP.getValue());
        batchTemplateVersion.setNormal(false);
        batchTemplateVersionMapper.updateById(batchTemplateVersion);
        // 记录日志
        batchTemplateLogService.saveTemplateLog(new BatchTemplateLogSaveDTO(TemplateVersionOperateTypeEnum.SCRAP, dto.getRemark(),
                batchTemplateVersion.getId(), batchTemplateVersion.getPath()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmTemplateVersion(TemplateVersionOperateDTO dto) {
        BatchTemplateVersion batchTemplateVersion = batchTemplateVersionMapper.selectById(dto.getTemplateVersionId());
        if (Objects.isNull(batchTemplateVersion)){
            // 当前版本不存在
            throw new BmosException(MesResponseCode.PLAN_ARCHIVE_TEMPLATE_VERSION_NOT_EXIST);
        }
        if (!TemplateVersionStatusEnum.EDIT.getValue().equals(batchTemplateVersion.getStatus())){
            throw new BmosException(MesResponseCode.PLAN_ARCHIVE_TEMPLATE_VERSION_NOT_EDIT, batchTemplateVersion.getVersion());
        }
        batchTemplateVersion.setStatus(TemplateVersionStatusEnum.CONFIRM.getValue());
        // 当前模板若没有生效的版本，则模板确认完成后便直接为生效状态
        BatchTemplateVersion effectiveVersion = batchTemplateVersionMapper.selectEffectiveByVersionId(batchTemplateVersion.getBatchTemplateInfoId(),
                true);
        batchTemplateVersion.setNormal(Boolean.TRUE);
        List<BatchTemplateVersion> batchTemplateVersions = Lists.newArrayList(batchTemplateVersion);
        if (Objects.nonNull(effectiveVersion)){
            // 若当前已经有默认版本，则模板的确认版本自动切换
            effectiveVersion.setNormal(Boolean.FALSE);
            batchTemplateVersions.add(effectiveVersion);
        }
        // 记录日志
        batchTemplateLogService.saveTemplateLog(new BatchTemplateLogSaveDTO(TemplateVersionOperateTypeEnum.CONFIRM,
                dto.getRemark(), batchTemplateVersion.getId(), batchTemplateVersion.getPath()));
        batchTemplateVersionMapper.updateBatch(batchTemplateVersions);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void templateInfoBindProcess(TemplateInfoBindDTO dto) {
        BatchTemplateInfo batchTemplateInfo = templateInfoMapper.selectById(dto.getTemplateInfoId());
        if (Objects.isNull(batchTemplateInfo)){
            throw new BmosException(MesResponseCode.BATCH_TEMPLATE_INFO_NOT_EXISTS);
        }
        batchTemplateInfoProcessMapper.deleteByTemplateInfoId(dto.getTemplateInfoId());
        List<BatchTemplateInfoProcess> batchTemplateInfoProcessList = BatchTemplateConverter.INSTANCE.convert2TemplateInfoProcessList(dto);
        try {
            batchTemplateInfoProcessMapper.insertBatch(batchTemplateInfoProcessList);
        } catch (Exception e){
            log.error("批量插入模板信息工艺失败", e);
            throw new BmosException(MesResponseCode.PLAN_ARCHIVE_TEMPLATE_INFO_PROCESS_HAS_PROCESS);
        }
    }

    @Override
    public void templateInfoBindDataAuth(TemplateInfoBindAuthDTO dto) {
        BatchTemplateInfo batchTemplateInfo = templateInfoMapper.selectById(dto.getTemplateInfoId());
        if (Objects.isNull(batchTemplateInfo)){
            throw new BmosException(MesResponseCode.BATCH_TEMPLATE_INFO_NOT_EXISTS);
        }
        resourcePermissionService.deleteByResourceId(dto.getTemplateInfoId());
        resourcePermissionService.save(ResourcePermissionSaveDTO.builder().resourceId(dto.getTemplateInfoId())
                .deptIds(dto.getDeptIds()).build());
    }

    @Override
    public CommonPage<TemplateInfoPageVO> templateInfoPage(TemplateInfoPageDTO dto) {
        // 数据权限 获取当前人所在的部门
        List<TemplateInfoPageVO> pageVOS = new ArrayList<>();
        List<Long> deptIds = platformApiAdaptor.deptIds();
        if (CollUtil.isEmpty(deptIds)){
            return CommonPage.CommonPage(pageVOS,  0L, dto);
        }
        List<TemplateCategoryTreeVO> categoryTreeVOS = batchTemplateCategoryService.categoryTree();
        List<Long> categoryIdList = findTargetCategoryIdList(categoryTreeVOS, dto.getCategoryId());
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        List<BatchTemplateInfo> list = templateInfoMapper.selectByParam(TemplateInfoParam.builder()
                .name(dto.getName()).categoryIdList(categoryIdList)
                .deptIds(deptIds).build());
        if (CollUtil.isEmpty(list)){
            return CommonPage.CommonPage(pageVOS,  0L, dto);
        }
        CommonPage<BatchTemplateInfo> page = CommonPage.convertPage(list);
        // 获取模板信息绑定的工艺id
        List<Long> templateInfoIdList = page.getList().stream().map(BatchTemplateInfo::getId).collect(Collectors.toList());
        List<BatchTemplateInfoProcess> batchTemplateInfoProcessList = new ArrayList<>();
        if (CollUtil.isNotEmpty(templateInfoIdList)){
            batchTemplateInfoProcessList = batchTemplateInfoProcessMapper.selectByTemplateInfoIdList(templateInfoIdList);
        }
        Map<Long, List<BatchTemplateInfoProcess>> batchTemplateInfoProcessMap = batchTemplateInfoProcessList.stream().collect(Collectors.groupingBy(BatchTemplateInfoProcess::getBatchTemplateInfoId));
        // 获取list中所有分类的分类名称
        Map<Long, String> treeNameMap = new HashMap<>();
        List<BatchTemplateInfo> pageList = page.getList();
        for (BatchTemplateInfo batchTemplateInfo : pageList) {
            if (treeNameMap.containsKey(batchTemplateInfo.getCategoryId())){
                continue;
            }
            treeNameMap.put(batchTemplateInfo.getCategoryId(), findCategoryName(batchTemplateInfo.getCategoryId(), categoryTreeVOS));
        }
        pageVOS = BatchTemplateConverter.INSTANCE.convert2PageVO(page.getList(), treeNameMap, batchTemplateInfoProcessMap);
        return CommonPage.CommonPage(pageVOS, Long.valueOf(page.getTotal()), dto);
    }

    @Override
    public CommonPage<TemplateVersionPageVO> templateVersionPage(TemplateVersionPageDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        List<BatchTemplateVersion> templateVersions = batchTemplateVersionMapper.selectByTemplateInfoId(dto.getTemplateInfoId());
        return CommonPage.convertPage(templateVersions, BatchTemplateConverter.INSTANCE::convert2TemPlateVersionVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void normalTemplateVersion(TemplateVersionOperateDTO dto) {
        BatchTemplateVersion batchTemplateVersion = batchTemplateVersionMapper.selectById(dto.getTemplateVersionId());
        if (Objects.isNull(batchTemplateVersion)){
            // 当前版本不存在
            throw new BmosException(MesResponseCode.PLAN_ARCHIVE_TEMPLATE_VERSION_NOT_EXIST);
        }
        if (!TemplateVersionStatusEnum.CONFIRM.getValue().equals(batchTemplateVersion.getStatus())){
            throw new BmosException(MesResponseCode.PLAN_ARCHIVE_TEMPLATE_VERSION_NOT_CONFIRM, batchTemplateVersion.getVersion(), TemplateVersionOperateTypeEnum.NORMAL.getName());
        }
        BatchTemplateVersion preEffectiveVersion = batchTemplateVersionMapper.selectEffectiveByVersionId(batchTemplateVersion.getBatchTemplateInfoId(),
                true);
        // 设置为默认
        batchTemplateVersion.setNormal(true);
        ArrayList<BatchTemplateVersion> batchTemplateVersions = Lists.newArrayList(batchTemplateVersion);
        if (Objects.nonNull(preEffectiveVersion) && !Objects.equals(preEffectiveVersion.getId(), batchTemplateVersion.getId())){
            preEffectiveVersion.setNormal(false);
            batchTemplateVersions.add(preEffectiveVersion);
        }
        batchTemplateVersionMapper.updateBatch(batchTemplateVersions);
        // 记录日志
        batchTemplateLogService.saveTemplateLog(new BatchTemplateLogSaveDTO(TemplateVersionOperateTypeEnum.NORMAL, dto.getRemark(),
                batchTemplateVersion.getId(), batchTemplateVersion.getPath()));
    }

    @Override
    public List<PlanEasyVO> templatePlan(Long templateInfoId) {
        // 查询当前绑定的所有工艺的信息
        List<BatchTemplateInfoProcess> batchTemplateInfoProcessList = batchTemplateInfoProcessMapper.selectByTemplateInfoId(templateInfoId);
        if (CollUtil.isEmpty(batchTemplateInfoProcessList)){
            return Collections.emptyList();
        }
        List<Long> processIdList = batchTemplateInfoProcessList.stream().map(BatchTemplateInfoProcess::getProcessId).collect(Collectors.toList());
        // 根据工艺id查询所有生产计划
        List<Plan> planList = planService.selectByProcessIdList(processIdList, Lists.newArrayList(ProductPlanStartEnum.STARTING.getValue(), ProductPlanStartEnum.END.getValue()));
        return BatchTemplateConverter.INSTANCE.convert2PlanEasyVO(planList);
    }

    @Override
    public List<BatchTemplateInfoProcess> selectTemplateProcessByProcessId(Long processId) {
        return batchTemplateInfoProcessMapper.selectTemplateProcessByProcessId(processId);
    }

    @Override
    public List<BatchTemplateInfo> selectAuthByIdList(List<Long> templateInfoIdList, List<Long> deptIds) {
        return templateInfoMapper.selectAuthByIdList(templateInfoIdList, deptIds);
    }

    @Override
    public BatchTemplateInfo selectById(Long templateInfoId) {
        return templateInfoMapper.selectById(templateInfoId);
    }

    @Override
    public BatchTemplateVersion selectByVersionId(Long templateVersionId) {
        return batchTemplateVersionMapper.selectById(templateVersionId);
    }

    @Override
    public List<Long> selectProcessIdListByTemplateId(Long templateId) {
        return batchTemplateInfoProcessMapper.selectProcessIdListByTemplateId(templateId);
    }

    @Override
    public List<BatchTemplateVersion> selectByVersionIdList(Collection<Long> templateVersionIdList) {
        return batchTemplateVersionMapper.selectBatchIds(templateVersionIdList);
    }

    @Override
    public List<BatchTemplateInfo> selectByIdList(Collection<Long> templateInfoIdSet) {
        return templateInfoMapper.selectBatchIds(templateInfoIdSet);
    }

    @Override
    public List<BatchTemplateVersion> selectByNormalProcessId(Long processId) {
        // 查询工艺绑定的模板
        List<BatchTemplateInfoProcess> processList = batchTemplateInfoProcessMapper.selectByProcessId(processId);
        if (CollUtil.isEmpty(processList)){
            return new ArrayList<>();
        }
        List<Long> templateInfoIdList = processList.stream().map(BatchTemplateInfoProcess::getBatchTemplateInfoId).collect(Collectors.toList());
        return batchTemplateVersionMapper.selectNormalByTemplateInfoIdList(templateInfoIdList);
    }

    @Override
    public List<Long> selectTemplateVersionByInfoId(Long templateInfoId) {
        List<BatchTemplateVersion> templateVersions = batchTemplateVersionMapper.selectByTemplateInfoId(templateInfoId);
        if (CollUtil.isEmpty(templateVersions)) {
            return new ArrayList<>();
        }
        return templateVersions.stream().map(BatchTemplateVersion::getId).collect(Collectors.toList());
    }

    @Override
    public List<TemplateVersionEasyVO> templateNormalVersionInfo(Long templateInfoId) {
        List<BatchTemplateVersion> batchTemplateVersions = batchTemplateVersionMapper.selectByConfirmTemplateInfoId(templateInfoId);
        if (CollUtil.isEmpty(batchTemplateVersions)){
            return new ArrayList<>();
        }
        return BatchTemplateConverter.INSTANCE.convert2TemplateVersionEasyVOList(batchTemplateVersions);
    }

    @Override
    public void downloadPath(String path, HttpServletResponse response) {
        try {
            minioFileClient.download(MinioBucket.BMOS_BATCH_TEMPLATE, path, response);
        } catch (Exception e) {
            log.error("下载批记录模板失败", e);
        }
    }

    @Override
    public List<Long> selectAllProcessIds() {
        return batchTemplateInfoProcessMapper.selectAllProcessIds();
    }

    /**
     * 下载模板版本对应的批记录模板world
     * @param batchTemplateVersion
     * @param response
     */
    private void doDownload(BatchTemplateVersion batchTemplateVersion, HttpServletResponse response) {
        try {
            minioFileClient.download(MinioBucket.BMOS_BATCH_TEMPLATE, batchTemplateVersion.getPath(), response);
        } catch (Exception e) {
            log.error("下载批记录模板失败", e);
            throw new BmosException(MesResponseCode.PLAN_ARCHIVE_TEMPLATE_VERSION_DOWNLOAD_FAIL, batchTemplateVersion.getVersion());
        }
    }

    private List<Long> findTargetCategoryIdList(List<TemplateCategoryTreeVO> categoryTreeVOS, Long categoryId) {
        if (Objects.isNull(categoryId) || TreeUtil.parentId.equals(categoryId)){
            // 所有categoryId
            return findAllCategoryIdList(categoryTreeVOS);
        }
        List<Long> categoryIdList = new ArrayList<>();
        for (TemplateCategoryTreeVO categoryTreeVO : categoryTreeVOS) {
            if (Objects.equals(categoryTreeVO.getId(), categoryId)){
                categoryIdList.add(categoryTreeVO.getId());
                categoryIdList.addAll(findAllCategoryIdList(categoryTreeVO.getChildren()));
                return categoryIdList;
            }
            if (CollUtil.isNotEmpty(categoryTreeVO.getChildren())){
                categoryIdList.addAll(findTargetCategoryIdList(categoryTreeVO.getChildren(), categoryId));
            }
        }
        return categoryIdList;
    }

    private List<Long> findAllCategoryIdList(List<TemplateCategoryTreeVO> categoryTreeVOS) {
        List<Long> categoryIdList = new ArrayList<>();
        if (CollUtil.isEmpty(categoryTreeVOS)){
            return categoryIdList;
        }
        for (TemplateCategoryTreeVO categoryTreeVO : categoryTreeVOS) {
            categoryIdList.add(categoryTreeVO.getId());
            categoryIdList.addAll(findAllCategoryIdList(categoryTreeVO.getChildren()));
        }
        return categoryIdList;
    }

    private String findCategoryName(Long categoryId, List<TemplateCategoryTreeVO> categoryTreeVOS) {
        List<String> names = new ArrayList<>();
        helpFindCategoryName(categoryId, categoryTreeVOS, names);
        return StrUtil.join(StrUtil.SLASH, names);
    }

    private boolean helpFindCategoryName(Long categoryId, List<TemplateCategoryTreeVO> categoryTreeVOS, List<String> names) {
        if (CollUtil.isEmpty(categoryTreeVOS)){
            return false;
        }
        for (TemplateCategoryTreeVO categoryTreeVO : categoryTreeVOS) {
            names.add(categoryTreeVO.getName());
            if (categoryId.equals(categoryTreeVO.getId())){
                return true;
            }
            boolean flg = false;
            if (CollUtil.isNotEmpty(categoryTreeVO.getChildren())){
                flg = helpFindCategoryName(categoryId, categoryTreeVO.getChildren(), names);
            }
            if (!flg){
                names.remove(names.size() - 1);
            } else {
                return true;
            }
        }
        return false;
    }
}
