package com.bmos.mes.service.operate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.audit.engine.core.query.resp.PageQueryResp;
import com.bmos.audit.engine.core.query.resp.TaskListResp;
import com.bmos.common.base.user.SysUser;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.id.IdUtils;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.common.enums.audit.AuditCategoryCodeEnum;
import com.bmos.mes.common.enums.audit.AuditTypeEnum;
import com.bmos.mes.common.enums.operate.OperateRuleVersionStateEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.audit.dto.FlowAuditTaskDTO;
import com.bmos.mes.service.audit.dto.FlowStartDTO;
import com.bmos.mes.service.audit.service.FlowAuditService;
import com.bmos.mes.service.config.minio.MinioFileClient;
import com.bmos.mes.service.config.minio.constants.MinioBucket;
import com.bmos.mes.service.operate.convert.OperateRuleVersionConvert;
import com.bmos.mes.service.operate.dto.version.*;
import com.bmos.mes.service.operate.mapper.OperateRuleMapper;
import com.bmos.mes.service.operate.mapper.OperateRuleVersionMapper;
import com.bmos.mes.service.operate.model.OperateRule;
import com.bmos.mes.service.operate.model.OperateRuleVersion;
import com.bmos.mes.service.operate.service.OperateRuleVersionService;
import com.bmos.mes.service.operate.vo.OperateRuleAuditVO;
import com.bmos.mes.service.operate.vo.OperateRuleVersionDetailsVO;
import com.bmos.mes.service.operate.vo.OperateRuleVersionPageVO;
import com.bmos.mes.service.operation.history.annotation.OperationHistory;
import com.bmos.mes.service.operation.history.aspect.OperationHistoryContext;
import com.bmos.mes.service.operation.history.enums.BusinessModule;
import com.bmos.mes.service.operation.history.enums.OperationType;
import com.bmos.mes.service.operation.history.model.OperationLogModel;
import com.bmos.mes.service.operation.history.service.OperationHistoryService;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.process.model.ProcedureStepSop;
import com.bmos.mes.service.process.service.ProcedureStepSopService;
import com.bmos.mes.service.utils.PdfWatermark;
import com.bmos.mybatis.page.BasePage;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.facade.system.execute.parameter.constants.BusinessParameterCodeConstants;
import com.bmos.platform.facade.system.execute.parameter.feign.BusinessParameterFeign;
import com.bmos.platform.facade.system.execute.parameter.vo.BusinessParameterDetailFeignVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author renjinguang
 */
@Service
public class OperateRuleVersionServiceImpl implements OperateRuleVersionService {

    private static final Logger log = LoggerFactory.getLogger(OperateRuleVersionServiceImpl.class);
    @Value("${minio.appDownloadUrl}")
    private String downloadUrl;

    @Autowired
    private OperateRuleVersionMapper versionMapper;

    @Autowired
    private FlowAuditService auditService;

    @Autowired
    private OperateRuleMapper ruleMapper;

    @Autowired
    private OperationHistoryService logService;

    @Autowired
    private MinioFileClient client;

    @Autowired
    private PlatformApiAdaptor platformApiAdaptor;

    @Resource
    private ProcedureStepSopService sopService;


    @Autowired
    private MinioFileClient minioFileClient;
    @Autowired
    private BusinessParameterFeign businessParameterFeign;


    @Override
    public CommonPage<OperateRuleVersionPageVO> getRecordPage(VersionPageDTO dto) {
        List<OperateRuleVersionPageVO> listByParentId = versionMapper.getListByParentId(dto);
        if (CollUtil.isEmpty(listByParentId)){
            return CommonPage.CommonPage(Collections.emptyList(), 0L, dto);
        }
        for (OperateRuleVersionPageVO operateRuleVersionPageVO : listByParentId) {
            if (Objects.isNull(operateRuleVersionPageVO.getUploadTime())){
                continue;
            }
            operateRuleVersionPageVO.setUploadDate(operateRuleVersionPageVO.getUploadTime().toLocalDate());
        }
        return CommonPage.convertPage(listByParentId);
    }

    @Override
    public List<OperateRuleVersion> getListByParentIdAndState(List<Long> parentId, String code) {
        return versionMapper.getListByParentIdAndState(parentId, code);
    }

    @Override
    public void save(OperateRuleVersion version) {
        versionMapper.saveOrUpdate(version);
    }

    @Override
    public OperateRuleVersionDetailsVO getDetailsById(Long id) {
        return versionMapper.getDetailsById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationHistory(module = BusinessModule.OPERATE_RULE, operationType = OperationType.SAVE, remark = "#dto.remark", businessId = "#getId")
    @OperationLog
    public void saveVersion(SaveVersionDTO dto) {
        List<OperateRuleVersion> versionList = versionMapper.getVersionListByOperateId(dto.getOperateId());
        if (CollectionUtils.convertList(versionList, OperateRuleVersion::getVersion).contains(dto.getVersion())) {
            throw new BmosException(MesResponseCode.OPERATE_VERSION_ERROR);
        }
        OperateRuleVersion version = BeanUtil.toBean(dto, OperateRuleVersion.class);
        version.setId(IdUtils.getSnowflake());
        OperationHistoryContext.putVariable(version, OperateRuleVersion::getId);
        versionMapper.saveOrUpdate(version);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationHistory(module = BusinessModule.OPERATE_RULE, operationType = OperationType.REDACT, remark = "#dto.remark", businessId = "#dto.id")
    @OperationLog
    public void update(UpdateVersionDTO dto) {
        try {
            List<OperateRuleVersion> versionList = versionMapper.getVersionListByOperateId(dto.getOperateId());
            Map<String, OperateRuleVersion> versionMap = CollectionUtils.convertMap(versionList, OperateRuleVersion::getVersion);
            OperateRuleVersion version = versionMap.get(dto.getVersion());
            if (ObjectUtil.isNotEmpty(version) && !dto.getId().equals(version.getId())) {
                throw new BmosException(MesResponseCode.OPERATE_VERSION_ERROR);
            }
            OperateRuleVersion operateRuleVersion = versionMapper.selectById(dto.getId());
            if (!StrUtil.equals(operateRuleVersion.getUrl(), dto.getUrl())) {
                client.delete(MinioBucket.OPERATE_RULE_SOP, operateRuleVersion.getUrl());
            }
            operateRuleVersion.setRemark(dto.getRemark());
            operateRuleVersion.setUrl(dto.getUrl());
            operateRuleVersion.setVersion(dto.getVersion());
            operateRuleVersion.setUploadTime(dto.getUploadTime());
            operateRuleVersion.setFileEffectDate(dto.getFileEffectDate());
            versionMapper.saveOrUpdate(operateRuleVersion);
        } catch (Exception e) {
            throw new BmosException(MesResponseCode.OPERATE_VERSION_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationLog
    public void updateState(UpdateStateDTO dto) {
        OperateRuleVersion operateRuleVersion = versionMapper.selectById(dto.getId());
        operateRuleVersion.setHistoryState(operateRuleVersion.getState());
        operateRuleVersion.setState(dto.getState());
        versionMapper.saveOrUpdate(operateRuleVersion);
        if (StrUtil.equals(dto.getState(),OperateRuleVersionStateEnum.CONFIRM.getCode())){
            saveOperateRuleHistoryLog(OperationType.CONFIRM.getValue(),dto.getId(), SysUserHolder.getUser().getUserId());
        }
        if (StrUtil.equals(dto.getState(),OperateRuleVersionStateEnum.INVALID.getCode())){
            saveOperateRuleHistoryLog(OperationType.INVALID.getValue(),dto.getId(), SysUserHolder.getUser().getUserId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationHistory(module = BusinessModule.OPERATE_RULE, operationType = OperationType.START_AUDIT, businessId = "#dto.versionId")
    @OperationLog
    public void startFlow(VersionStartFlowDTO dto) {
        OperateRuleVersion version = versionMapper.selectById(dto.getVersionId());
        List<OperateRuleVersion> versionList = versionMapper.getVersionListByOperateId(version.getOperateId());
        String ruleName = ruleMapper.selectNameById(version.getOperateId());
        FlowStartDTO startDto = new FlowStartDTO();
        startDto.setBusinessKey(String.valueOf(version.getId()));
        startDto.setName(ruleName);
        startDto.setExtField(version.getVersion());
        if (dto.getAuditType()) {
            startAuditFlowVerify(versionList, AuditTypeEnum.OPERATE_RULE_START.getAuditType());
            startDto.setCode(AuditCategoryCodeEnum.OPERATE_RULE_START.getCode());
            startDto.setCategoryCode(AuditCategoryCodeEnum.OPERATE_RULE_START.getCode());
            version.setAuditType(AuditTypeEnum.OPERATE_RULE_START.getAuditType());
        } else {
            startAuditFlowVerify(versionList, AuditTypeEnum.OPERATE_RULE_BLOCK.getAuditType());
            startDto.setCode(AuditCategoryCodeEnum.OPERATE_RULE_BLOCK.getCode());
            startDto.setCategoryCode(AuditCategoryCodeEnum.OPERATE_RULE_BLOCK.getCode());
            version.setAuditType(AuditTypeEnum.OPERATE_RULE_BLOCK.getAuditType());
        }
        String instanceId = auditService.flowAuditStart(startDto);
        version.setInstanceId(instanceId);
        version.setHistoryState(version.getState());
        version.setEffectDate(dto.getEffectDate());
        version.setState(OperateRuleVersionStateEnum.AUDIT.getCode());
        versionMapper.saveOrUpdate(version);
    }

    @Override
    public CommonPage<OperateRuleAuditVO> pageTodoFlow(OperateVersionAuditDTO dto) {
        FlowAuditTaskDTO taskDTO = dto.convertAuditTaskDTO();
        taskDTO.setCategoryList(Arrays.asList(AuditTypeEnum.OPERATE_RULE_BLOCK.getCode(), AuditTypeEnum.OPERATE_RULE_START.getCode()));
        BasePage page = dto.convertBasePage();
        List<OperateRuleAuditVO> voList = versionMapper.pageTodoFlow(dto);
        List<Long> versionId = CollectionUtils.convertList(voList, OperateRuleAuditVO::getVersionId);
        if (CollUtil.isEmpty(versionId)) {
            return CommonPage.CommonPage(Collections.emptyList(), 0L, page);
        }
        taskDTO.setBusinessKeyList(CollectionUtils.convertList(versionId,String::valueOf));
        PageQueryResp<List<TaskListResp>> listPageQueryResp = auditService.queryToDoListByCategory(taskDTO);
        List<String> businessIdList = CollectionUtils.convertList(listPageQueryResp.getData(), TaskListResp::getBusinessKey);
        if (CollUtil.isEmpty(listPageQueryResp.getData()) || CollUtil.isEmpty(businessIdList)) {
            return CommonPage.CommonPage(Collections.emptyList(), 0L, page);
        }
        Map<String, TaskListResp> taskListRespMap = CollectionUtils.convertMap(listPageQueryResp.getData(), TaskListResp::getBusinessKey);
        List<OperateRuleAuditVO> auditVOS = OperateRuleVersionConvert.INSTANCE.handleTodoFlowData(voList, taskListRespMap);
        return CommonPage.CommonPage(auditVOS,listPageQueryResp.getTotal(),page);
    }

    @Override
    public OperateRuleVersion selectById(Long id) {
        return versionMapper.selectById(id);
    }

    @Override
    public void flowUpdateVersion(OperateRuleVersion version) {
        versionMapper.saveOrUpdate(version);
    }

    @Override
    public List<OperateRuleVersion> selectList() {
        return versionMapper.selectList();
    }

    @Override
    public List<OperateRuleVersion> getWaitValidVersionByDateAndState(String date, String code) {
        return versionMapper.getWaitValidVersionByDateAndState(date, code);
    }

    @Override
    public void updateOperateRuleVersion(List<OperateRuleVersion> list) {
        versionMapper.updateOperateRuleVersion(list);
    }

    @Override
    public void auditOperateRuleNodeLog(String businessKey, String remark, String userId, String name, String comment) {
        saveAuditOperateRuleNodeLog(remark, userId, Long.valueOf(businessKey), name, comment, OperationType.APPROVE_AUDIT.getValue());
    }

    @Override
    public void rejectOperateRuleHistoryLog(String businessId, String comment, String remark, String userId, String nodeName) {
        saveAuditOperateRuleNodeLog(remark, userId, Long.valueOf(businessId), nodeName, comment, OperationType.REJECT_AUDIT.getValue());
    }

    @Override
    public List<OperateRule> selectByListByState(String code, List<Long> deptIds) {
        return versionMapper.selectByListByState(code, deptIds);
    }

    @Override
    public OperateRuleVersion download(Long versionId) {
        OperateRuleVersion version = versionMapper.selectById(versionId);
        if (ObjectUtil.isEmpty(version)) {
            version = versionMapper.selectStateByRuleId(versionId);
        }
        return version;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateEffect(Long id) {
        OperateRuleVersion version = versionMapper.selectById(id);
        if (ObjectUtil.isEmpty(version) || !StrUtil.equals(version.getState(), OperateRuleVersionStateEnum.WAIT_VALID.getCode())) {
            throw new BmosException(MesResponseCode.OPERATE_VERSION_FLOW_ERROR);
        }
        version.setEffectDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        version.setHistoryState(version.getState());
        version.setState(OperateRuleVersionStateEnum.VALID.getCode());
        List<OperateRuleVersion> versions = versionMapper.getVersionListByOperateId(version.getOperateId());
        List<OperateRuleVersion> versionList = CollectionUtils.filterList(versions, item ->
                StrUtil.equals(item.getState(), OperateRuleVersionStateEnum.VALID.getCode()));
        if (CollUtil.isNotEmpty(versionList)) {
            OperateRuleVersion operateRuleVersion = CollectionUtils.getFirst(versionList);
            operateRuleVersion.setState(OperateRuleVersionStateEnum.INVALID.getCode());
            versionMapper.saveOrUpdate(operateRuleVersion);
        }
        versionMapper.saveOrUpdate(version);
        saveOperateRuleHistoryLog(OperationType.VALID.getValue(),id,SysUserHolder.getUser().getUserId());
        return true;
    }

    @Override
    public List<OperateRule> getAppDetail(Long stepModelId) {
        //部门权限
        List<Long> deptIds = platformApiAdaptor.deptIds();
        List<ProcedureStepSop> stepSops = sopService.queryListByStepModelId(Collections.singleton(stepModelId));
        if (CollUtil.isEmpty(deptIds) || CollUtil.isEmpty(stepSops)) {
            return new ArrayList<>();
        }
        List<OperateRule> operateRule = ruleMapper.queryListByIdsAndDeptIds(CollectionUtils.convertList(stepSops,ProcedureStepSop::getOperationSopId),
                OperateRuleVersionStateEnum.VALID.getCode(),deptIds);
        if (ObjectUtil.isEmpty(operateRule)) {
            return new ArrayList<>();
        }
        operateRule.forEach(item->{
            String bucketName = client.getBucketName(MinioBucket.OPERATE_RULE_SOP);
            String url = downloadUrl + StrUtil.SLASH + bucketName + item.getUrl();
            item.setUrl(url);
        });
        return operateRule;
    }

    @Override
    public List<OperateRuleVersion> selectByOperateRuleId(Long operateId) {
        return versionMapper.getVersionListByOperateId(operateId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateValid(Long versionId) {
        OperateRuleVersion operateRuleVersion = versionMapper.selectById(versionId);
        if (ObjectUtil.isEmpty(operateRuleVersion)){
            throw new BmosException(MesResponseCode.OPERATE_VERSION_FLOW_ERROR);
        }
        if (!operateRuleVersion.getState().equals(OperateRuleVersionStateEnum.CONFIRM.getCode()) &&
            !operateRuleVersion.getState().equals(OperateRuleVersionStateEnum.EDIT.getCode())){
            throw new BmosException(MesResponseCode.OPERATE_UPDATE_VALID_ERROR);
        }
        List<OperateRuleVersion> versionList = versionMapper.getVersionListByOperateId(operateRuleVersion.getOperateId());
        List<OperateRuleVersion> versions = CollectionUtils.filterList(versionList, item -> item.getState().equals(OperateRuleVersionStateEnum.VALID.getCode()));
        if (CollUtil.isNotEmpty(versions)){
            OperateRuleVersion ruleVersion = CollectionUtils.getFirst(versions);
            ruleVersion.setState(OperateRuleVersionStateEnum.INVALID.getCode());
            ruleVersion.setHistoryState(OperateRuleVersionStateEnum.INVALID.getCode());
            versionMapper.updateById(ruleVersion);
        }
        operateRuleVersion.setEffectDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        operateRuleVersion.setHistoryState(operateRuleVersion.getState());
        operateRuleVersion.setState(OperateRuleVersionStateEnum.VALID.getCode());
        versionMapper.updateById(operateRuleVersion);
        saveOperateRuleHistoryLog(OperationType.DIRECT_VALID.getValue(),versionId, SysUserHolder.getUser().getUserId());
    }

    @Override
    public void downloadSop(Long versionId, HttpServletResponse response) {
        OperateRuleVersion version = this.download(versionId);
        if (ObjectUtil.isEmpty(version)) {
            throw new BmosException(MesResponseCode.OPERATE_UPLOAD_ERROR);
        }
        try {
            String filePath = minioFileClient.downLoadFileToTemp(MinioBucket.OPERATE_RULE_SOP, version.getUrl(), ".pdf");
            SysUser user = SysUserHolder.getUser();
            // 为pdf添加水印
            String watermark = StrUtil.format("{}-{} {}", user.getUserName(), user.getLoginName(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            // 获取字体路径
            ResponseInfo<BusinessParameterDetailFeignVO> mesWatermarksTtfPath = FeignUtils.handleRequest(data -> businessParameterFeign.detailByCode(data), BusinessParameterCodeConstants.PLATFORM_SYS_WATERMARK_FONT_PATH);
            if (ObjectUtil.isNotEmpty(mesWatermarksTtfPath.getData()) && StrUtil.isNotBlank(mesWatermarksTtfPath.getData().getValue())) {
                PdfWatermark.addRandomWatermarks(filePath, response.getOutputStream(), watermark, mesWatermarksTtfPath.getData().getValue(), 10, 8);
            }
        } catch (Exception e) {
            log.error("添加水印失败", e);
        }
        try {
            minioFileClient.download(MinioBucket.OPERATE_RULE_SOP, version.getUrl(), response);
        } catch (Exception e) {
            log.error("文件下载失败", e);
        }

    }

    private void saveAuditOperateRuleNodeLog(String remark, String userId, Long businessId, String nodeName, String comment, String auditType) {
        logService.save(OperationLogModel.builder()
                .module(BusinessModule.OPERATE_RULE.name())
                .businessId(businessId)
                .operationType(auditType)
                .remark(remark)
                .comment(comment)
                .nodeName(nodeName)
                .createBy(userId)
                .build());
    }

    private void saveOperateRuleHistoryLog(String operationType, Long businessId, String userId) {
        logService.save(OperationLogModel.builder()
                .module(BusinessModule.OPERATE_RULE.name())
                .businessId(businessId)
                .operationType(operationType)
                .createBy(userId)
                .build());
    }

    /**
     * 启用审核校验
     *
     * @param versionList
     */
    private void startAuditFlowVerify(List<OperateRuleVersion> versionList, String flowType) {
        //待生效版本
        List<OperateRuleVersion> waitValidVersion = CollectionUtils.filterList(versionList,
                item -> StrUtil.equals(item.getState(), OperateRuleVersionStateEnum.WAIT_VALID.getCode()));
        //审核中并且是启用审核
        List<OperateRuleVersion> auditAndStartVersion = CollectionUtils.filterList(versionList,
                item -> StrUtil.equals(item.getState(), OperateRuleVersionStateEnum.AUDIT.getCode()) &&
                        StrUtil.equals(item.getAuditType(), AuditTypeEnum.OPERATE_RULE_START.getAuditType()));
        if (StrUtil.equals(flowType, AuditTypeEnum.OPERATE_RULE_BLOCK.getAuditType())) {
            if (CollUtil.isNotEmpty(auditAndStartVersion)) {
                throw new BmosException(MesResponseCode.OPERATE_VERSION_FLOW_START_ERROR);
            }
        }
        if (StrUtil.equals(flowType, AuditTypeEnum.OPERATE_RULE_START.getAuditType()) &&
                (CollUtil.isNotEmpty(waitValidVersion) || CollUtil.isNotEmpty(auditAndStartVersion))) {
            throw new BmosException(MesResponseCode.OPERATE_VERSION_FLOW_START_ERROR);
        }
    }
}
