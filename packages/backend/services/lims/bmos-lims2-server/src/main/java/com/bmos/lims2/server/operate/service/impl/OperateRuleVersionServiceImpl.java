package com.bmos.lims2.server.operate.service.impl;

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
import com.bmos.lims2.common.enums.AuditBusinessModule;
import com.bmos.lims2.common.enums.AuditCategoryCodeEnum;
import com.bmos.lims2.common.enums.AuditTypeEnum;
import com.bmos.lims2.common.enums.OperationType;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.audit.FlowAuditService;
import com.bmos.lims2.server.audit.dto.FlowAuditTaskDTO;
import com.bmos.lims2.server.audit.dto.FlowStartDTO;
import com.bmos.lims2.server.audit.operationlog.entity.AuditOperationLogEntity;
import com.bmos.lims2.server.audit.operationlog.service.AuditOperationLogService;
import com.bmos.lims2.server.config.minio.MinioFileClient;
import com.bmos.lims2.server.config.minio.constants.MinioBucket;
import com.bmos.lims2.server.eln.record.entity.BatchRecord;
import com.bmos.lims2.server.eln.record.mapper.BatchRecordMapper;
import com.bmos.lims2.server.inspect.parameter.mapper.InspectMethodOperateBindMapper;
import com.bmos.lims2.server.operate.convert.OperateRuleVersionConvert;
import com.bmos.lims2.server.operate.dto.version.*;
import com.bmos.lims2.server.operate.enums.OperateRuleVersionStateEnum;
import com.bmos.lims2.server.operate.mapper.OperateRuleMapper;
import com.bmos.lims2.server.operate.mapper.OperateRuleVersionMapper;
import com.bmos.lims2.server.operate.model.OperateRule;
import com.bmos.lims2.server.operate.model.OperateRuleVersion;
import com.bmos.lims2.server.operate.service.OperateRuleVersionService;
import com.bmos.lims2.server.operate.vo.OperateRuleAuditVO;
import com.bmos.lims2.server.operate.vo.OperateRuleVersionDetailsVO;
import com.bmos.lims2.server.operate.vo.OperateRuleVersionPageVO;
import com.bmos.lims2.server.platform.util.FeignUtils;
import com.bmos.lims2.server.util.PdfWatermark;
import com.bmos.logging.annotation.OperationLog;
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
    private AuditOperationLogService logService;

    @Autowired
    private MinioFileClient client;

    @Autowired
    private PlatformApiAdaptor platformApiAdaptor;

    @Autowired
    private BusinessParameterFeign businessParameterFeign;

    @Autowired
    private InspectMethodOperateBindMapper bindMapper;
    @Autowired
    private BatchRecordMapper batchRecordMapper;


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
    @OperationLog
    public void saveVersion(SaveVersionDTO dto) {
        List<OperateRuleVersion> versionList = versionMapper.getVersionListByOperateId(dto.getOperateId());
        if (CollectionUtils.convertList(versionList, OperateRuleVersion::getVersion).contains(dto.getVersion())) {
            throw new BmosException(LimsResponseCode.OPERATE_VERSION_ERROR);
        }
        OperateRuleVersion version = BeanUtil.toBean(dto, OperateRuleVersion.class);
        version.setId(IdUtils.getSnowflake());
        versionMapper.saveOrUpdate(version);
        saveOperateRuleHistoryLog(OperationType.SAVE, version.getId(), null, null, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationLog
    public void update(UpdateVersionDTO dto) {
        try {
            List<OperateRuleVersion> versionList = versionMapper.getVersionListByOperateId(dto.getOperateId());
            Map<String, OperateRuleVersion> versionMap = CollectionUtils.convertMap(versionList, OperateRuleVersion::getVersion);
            OperateRuleVersion version = versionMap.get(dto.getVersion());
            if (ObjectUtil.isNotEmpty(version) && !dto.getId().equals(version.getId())) {
                throw new BmosException(LimsResponseCode.OPERATE_VERSION_ERROR);
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
            saveOperateRuleHistoryLog(OperationType.REDACT, operateRuleVersion.getId(), null, null, null);
        } catch (Exception e) {
            throw new BmosException(LimsResponseCode.OPERATE_VERSION_ERROR);
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
        if (StrUtil.equals(dto.getState(), OperateRuleVersionStateEnum.INVALID.getCode())) {
            saveOperateRuleHistoryLog(OperationType.INVALID, operateRuleVersion.getId(),
                    null, null, null);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationLog
    public void startFlow(VersionStartFlowDTO dto) {
        OperateRuleVersion version = versionMapper.selectById(dto.getVersionId());
        List<OperateRuleVersion> versionList = versionMapper.getVersionListByOperateId(version.getOperateId());
        String ruleName = ruleMapper.selectNameById(version.getOperateId());
        FlowStartDTO startDto = new FlowStartDTO();
        startDto.setBusinessKey(String.valueOf(version.getId()));
        startDto.setName(ruleName);
        startDto.setExtField(version.getVersion());
        startAuditFlowVerify(versionList, AuditTypeEnum.OPERATE_RULE_START.getAuditType());
        startDto.setCode(AuditCategoryCodeEnum.OPERATE_RULE_START.getCode());
        startDto.setCategoryCode(AuditCategoryCodeEnum.OPERATE_RULE_START.getCode());
        version.setAuditType(AuditTypeEnum.OPERATE_RULE_START.getAuditType());
        String instanceId = auditService.flowAuditStart(startDto);
        version.setInstanceId(instanceId);
        version.setHistoryState(version.getState());
        version.setEffectDate(dto.getEffectDate());
        version.setState(OperateRuleVersionStateEnum.AUDIT.getCode());
        versionMapper.saveOrUpdate(version);
        saveOperateRuleHistoryLog(OperationType.START_AUDIT, version.getId(),
                null, null, null);
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
        saveOperateRuleHistoryLog(OperationType.APPROVE_AUDIT, Long.valueOf(businessKey), remark, name, comment);
    }

    @Override
    public void rejectOperateRuleHistoryLog(String businessId, String comment, String remark, String userId, String nodeName) {
        saveOperateRuleHistoryLog(OperationType.REJECT_AUDIT, Long.valueOf(businessId), remark, nodeName, comment);
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
    public List<OperateRule> getAppDetail(Long recordId) {
//        部门权限
        List<Long> deptIds = platformApiAdaptor.deptIds();
        BatchRecord batchRecord = batchRecordMapper.selectById(recordId);
        if (CollUtil.isEmpty(deptIds) || batchRecord==null) {
            return new ArrayList<>();
        }
        List<Long> operateIdsByRecordId = bindMapper.listOperateIdsByRecordId(recordId);


        List<OperateRule> operateRule = ruleMapper.queryListByIdsAndDeptIds(operateIdsByRecordId,
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
            throw new BmosException(LimsResponseCode.OPERATE_VERSION_FLOW_ERROR);
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
    }

    @Override
    public void downloadSop(Long versionId, HttpServletResponse response) {
        OperateRuleVersion version = this.download(versionId);
        if (ObjectUtil.isEmpty(version)) {
            throw new BmosException(LimsResponseCode.FILE_DOWNLOAD_FAILED);
        }
        try {
            String filePath = client.downLoadFileToTemp(MinioBucket.OPERATE_RULE_SOP, version.getUrl(), ".pdf");
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
            client.download(MinioBucket.OPERATE_RULE_SOP, version.getUrl(), response);
        } catch (Exception e) {
            log.error("文件下载失败", e);
        }

    }

    private void saveOperateRuleHistoryLog(OperationType operationType, Long businessId, String remark, String nodeName, String comment) {
        logService.save(AuditOperationLogEntity.builder()
                .module(AuditBusinessModule.OPERATE_RULE.name())
                .businessId(businessId)
                .operationType(operationType.getValue())
                .remark(remark)
                .nodeName(nodeName)
                .comment(comment)
                .createBy(SysUserHolder.getUser().getUserId())
                .build());
    }

    /**
     * 启用审核校验
     *
     * @param versionList
     */
    private void startAuditFlowVerify(List<OperateRuleVersion> versionList, String flowType) {
        //审核中并且是启用审核
        List<OperateRuleVersion> auditAndStartVersion = CollectionUtils.filterList(versionList,
                item -> StrUtil.equals(item.getState(), OperateRuleVersionStateEnum.AUDIT.getCode()) &&
                        StrUtil.equals(item.getAuditType(), AuditTypeEnum.OPERATE_RULE_START.getAuditType()));
        if (StrUtil.equals(flowType, AuditTypeEnum.OPERATE_RULE_START.getAuditType()) && CollUtil.isNotEmpty(auditAndStartVersion)) {
            throw new BmosException(LimsResponseCode.OPERATE_VERSION_FLOW_START_ERROR);
        }
    }
}
