package com.bmos.mes.service.exception.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.enums.execute.ExceptionRecordModeEnum;
import com.bmos.mes.common.enums.execute.ExceptionStatusEnum;
import com.bmos.mes.common.enums.execute.ExceptionTypeDictEnum;
import com.bmos.mes.common.enums.plan.ProductPlanStartEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.common.model.execute.ExceptionDescriptionParam;
import com.bmos.mes.service.exception.convert.ExceptionManageConvert;
import com.bmos.mes.service.exception.dto.*;
import com.bmos.mes.service.exception.mapper.ExecuteExceptionMapper;
import com.bmos.mes.service.exception.model.ExecuteException;
import com.bmos.mes.service.exception.service.ExceptionManageService;
import com.bmos.mes.service.exception.vo.ExceptionPageVO;
import com.bmos.mes.service.operation.history.enums.BusinessModule;
import com.bmos.mes.service.operation.history.enums.OperationType;
import com.bmos.mes.service.operation.history.model.OperationLogModel;
import com.bmos.mes.service.operation.history.service.OperationHistoryService;
import com.bmos.mes.service.plan.info.mapper.PlanMapper;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.process.mapper.ProcedureModelMapper;
import com.bmos.mes.service.process.mapper.ProcedureStepModelMapper;
import com.bmos.mes.service.process.mapper.ProcessMapper;
import com.bmos.mes.service.process.model.ProcedureModel;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.process.model.Process;
import com.bmos.mes.service.product.mapper.ProductMaterialMapper;
import com.bmos.mes.service.product.model.ProductMaterial;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.facade.notify.MessageNotifyFeign;
import com.bmos.platform.facade.notify.dto.DataOverLimitBatchMessage;
import com.bmos.platform.facade.notify.dto.DataOverLimitMessage;
import com.bmos.platform.facade.notify.dto.ProductModifyAbnormalMessage;
import com.bmos.platform.facade.notify.dto.ProductModifyBatchMessage;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExceptionManageServiceImpl implements ExceptionManageService {

    @Resource
    private ExecuteExceptionMapper executeExceptionMapper;

    @Resource
    private OperationHistoryService operationHistoryService;

    @Resource
    private ProductMaterialMapper productMaterialMapper;

    @Resource
    private PlanMapper planMapper;

    @Resource
    private ProcedureStepModelMapper procedureStepModelMapper;

    @Resource
    private ProcedureModelMapper procedureModelMapper;

    @Resource
    private ProcessMapper processMapper;

    @Resource
    private MessageNotifyFeign messageNotifyFeign;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void manualRecordException(ExceptionManualRecordDTO dto) {
        ExecuteException model = ExceptionManageConvert.INSTANCE.convert2ExceptionModel(dto);
        initExecuteExceptionInfo(model);
        model.setExceptionStatus(ExceptionStatusEnum.INVESTIGATING);
        model.setRecordMode(ExceptionRecordModeEnum.MANUAL_RECORD);
        BaseUserDO user = UserUtils.getUser(dto.getRecordUserId());
        model.setRecordUserName(user.getUserName() + StrUtil.DASHED + user.getLoginName());
        executeExceptionMapper.insert(model);
        ExceptionOperationLogDTO logDTO = ExceptionManageConvert.INSTANCE.convert2ExceptionLogDTO(model);
        logDTO.setRecordMode(ExceptionRecordModeEnum.MANUAL_RECORD.getName());
        saveOperationHistoryLog(model.getId(), OperationType.SAVE, dto.getRecordUserId(), logDTO);
        // 发送消息
        notifyException(Collections.singleton(model));
    }

    private void notifyException(Collection<ExecuteException> list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        Map<String, List<ExecuteException>> map = CollectionUtils.convertMultiMap(list,
                ExecuteException::getExceptionTypeCode);
        sendModifyMessage(map.get(ExceptionTypeDictEnum.ProductReviseException.getValue()));
        sendOverLimitMessage(map.get(ExceptionTypeDictEnum.OverLimitException.getValue()));
    }

    private void sendOverLimitMessage(List<ExecuteException> executeExceptions) {
        if (CollUtil.isEmpty(executeExceptions)) {
            return;
        }
        DataOverLimitBatchMessage message = new DataOverLimitBatchMessage();
        message.setTime(LocalDateTime.now());
        message.setMessageList(executeExceptions.stream().map(executeException -> {
            DataOverLimitMessage single = new DataOverLimitMessage();
            single.setTime(LocalDateTime.now());
            single.setBatchNo(executeException.getBatchNo());
            single.setProcedureName(executeException.getProcedureName());
            single.setProcessName(executeException.getProcessName());
            single.setProcedureStepName(executeException.getProcedureStepName());
            single.setAbnormalDescription(executeException.getExceptionDescription());
            return single;
        }).collect(Collectors.toList()));
        FeignUtils.handleRequest(messageNotifyFeign::batchSendDataOverLimitMessage, message);
    }

    private void sendModifyMessage(List<ExecuteException> executeExceptions) {
        if (CollUtil.isEmpty(executeExceptions)) {
            return;
        }
        ProductModifyBatchMessage message = new ProductModifyBatchMessage();
        message.setTime(LocalDateTime.now());
        message.setMessageList(executeExceptions.stream().map(executeException -> {
            ProductModifyAbnormalMessage single = new ProductModifyAbnormalMessage();
            single.setTime(LocalDateTime.now());
            single.setBatchNo(executeException.getBatchNo());
            single.setProcedureName(executeException.getProcedureName());
            single.setProcessName(executeException.getProcessName());
            single.setProcedureStepName(executeException.getProcedureStepName());
            single.setAbnormalDescription(executeException.getExceptionDescription());
            return single;
        }).collect(Collectors.toList()));
        FeignUtils.handleRequest(messageNotifyFeign::batchSendProductModifyAbnormalMessage, message);
    }


    private void initExecuteExceptionInfo(ExecuteException model) {
        // 工艺信息
        model.setProcessName(Optional.ofNullable(model.getProcessId()).map(e -> {
            Process process = processMapper.selectById(model.getProcessId());
            return process == null ? null : process.getName();
        }).orElse(null));
        // 产品信息
        model.setProductFullName(Optional.ofNullable(model.getProductId()).map(e -> {
            ProductMaterial productMaterial = productMaterialMapper.selectById(e);
            return productMaterial == null ? null :
                    productMaterial.getMergeCode() + StrUtil.DASHED + productMaterial.getName();
        }).orElse(null));
        // 批次信息
        model.setBatchNo(Optional.ofNullable(model.getProductPlanId()).map(e -> {
            Plan plan = planMapper.selectById(e);
            return plan == null ? null : plan.getBatchNo();
        }).orElse(null));
        // 工序信息
        if (model.getProcedureModelId() != null) {
            ProcedureModel procedureModel =
                    Optional.ofNullable(procedureModelMapper.selectById(model.getProcedureModelId())).orElse(new ProcedureModel());
            model.setProcedureId(procedureModel.getProcedureId());
            model.setProcedureName(procedureModel.getName());
        } else {
            model.setProcedureId(null);
            model.setProcedureName(null);
            model.setProcedureModelId(null);
        }
        // 工步信息
        if (model.getProcedureStepModelId() != null) {
            ProcedureStepModel procedureStepModel =
                    Optional.ofNullable(procedureStepModelMapper.selectById(model.getProcedureStepModelId())).orElse(new ProcedureStepModel());
            model.setProcedureStepId(procedureStepModel.getProcedureStepId());
            model.setProcedureStepName(procedureStepModel.getName());
        } else {
            model.setProcedureStepId(null);
            model.setProcedureStepName(null);
            model.setProcedureStepModelId(null);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editException(ExceptionEditDTO dto) {
        ExecuteException model = executeExceptionMapper.selectById(dto.getId());
        ExecuteException oldModel = BeanUtil.copyProperties(model, ExecuteException.class);
        if (model == null) {
            throw new BmosException(MesResponseCode.EXCEPTION_NOT_EXISTS);
        }
        if (Objects.equals(model.getExceptionStatus(), ExceptionStatusEnum.CANCELED)) {
            throw new BmosException(MesResponseCode.EXCEPTION_CANCELED);
        }
        if (Objects.equals(model.getExceptionStatus(), ExceptionStatusEnum.HANDLED)) {
            throw new BmosException(MesResponseCode.EXCEPTION_ALREADY_HANDLED);
        }
        // 自动录入的异常在编辑后会更改记录为手动录入
        model.setRecordMode(ExceptionRecordModeEnum.MANUAL_RECORD);
        copyProperties(dto, model);
        initExecuteExceptionInfo(model);
        ExceptionOperationLogDTO logDTO = ExceptionManageConvert.INSTANCE.convert2ExceptionLogDTO(model);
        logDTO.compareAndClearSameProperties(oldModel);
        executeExceptionMapper.updateAllInfo(model);
        saveOperationHistoryLog(dto.getId(), OperationType.REDACT, dto.getEditUserId(), logDTO);
    }

    private static void copyProperties(ExceptionEditDTO dto, ExecuteException model) {
        model.setRecordUserId(dto.getEditUserId());
        // 编辑操作会修改记录人
        BaseUserDO user = UserUtils.getUser(dto.getEditUserId());
        model.setRecordUserName(user.getUserName() + StrUtil.DASHED + user.getLoginName());
        model.setRecordTime(dto.getRecordTime());

        model.setProductId(dto.getProductId());
        model.setProcessId(dto.getProcessId());
        model.setProcessVersion(dto.getProcessVersion());
        model.setProductPlanId(dto.getProductPlanId());
        model.setProcedureModelId(dto.getProcedureModelId());
        model.setProcedureStepModelId(dto.getProcedureStepModelId());

        model.setExceptionType(dto.getExceptionType());
        model.setExceptionTypeCode(dto.getExceptionTypeCode());
        model.setExceptionDescription(dto.getExceptionDescription());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelException(ExceptionCancelDTO dto) {
        ExecuteException executeException = executeExceptionMapper.selectById(dto.getId());
        if (executeException == null) {
            throw new BmosException(MesResponseCode.EXCEPTION_NOT_EXISTS);
        }
        if (Objects.equals(executeException.getExceptionStatus(), ExceptionStatusEnum.CANCELED)) {
            throw new BmosException(MesResponseCode.EXCEPTION_CANCELED);
        }
        if (Objects.equals(executeException.getExceptionStatus(), ExceptionStatusEnum.HANDLED)) {
            throw new BmosException(MesResponseCode.EXCEPTION_ALREADY_HANDLED);
        }
        executeException.setExceptionStatus(ExceptionStatusEnum.CANCELED);
        executeException.setCancelReason(dto.getCancelReason());
        executeException.setCancelTime(LocalDateTime.now());
        executeException.setCancelUserId(dto.getCancelUserId());
        BaseUserDO user = UserUtils.getUser(dto.getCancelUserId());
        executeException.setCancelUserName(user.getUserName() + StrUtil.DASHED + user.getLoginName());
        executeExceptionMapper.updateById(executeException);
        // 记录操作历史
        saveOperationHistoryLog(dto.getId(), OperationType.NULLIFY, dto.getCancelUserId(),
                ExceptionOperationLogDTO.builder().cancelReason(dto.getCancelReason()).build());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleException(ExceptionHandleDTO dto) {
        ExecuteException executeException = executeExceptionMapper.selectById(dto.getId());
        if (executeException == null) {
            throw new BmosException(MesResponseCode.EXCEPTION_NOT_EXISTS);
        }
        if (Objects.equals(executeException.getExceptionStatus(), ExceptionStatusEnum.HANDLED)) {
            throw new BmosException(MesResponseCode.EXCEPTION_ALREADY_HANDLED);
        }
        if (Objects.equals(executeException.getExceptionStatus(), ExceptionStatusEnum.CANCELED)) {
            throw new BmosException(MesResponseCode.EXCEPTION_CANCELED);
        }
        executeException.setHandleResult(dto.getHandleResult());
        executeException.setHandleTime(dto.getHandleTime());
        executeException.setHandleUserId(dto.getHandleUserId());
        BaseUserDO user = UserUtils.getUser(dto.getHandleUserId());
        executeException.setHandleUserName(user.getUserName() + StrUtil.DASHED + user.getLoginName());
        executeException.setExceptionStatus(ExceptionStatusEnum.HANDLED);
        executeExceptionMapper.updateById(executeException);
        saveOperationHistoryLog(dto.getId(), OperationType.HANDLE, dto.getHandleUserId(),
                ExceptionOperationLogDTO.builder().handleResult(dto.getHandleResult()).handleTime(dto.getHandleTime()).build());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reInvestigateException(ExceptionReInvestigateDTO dto) {
        ExecuteException executeException = executeExceptionMapper.selectById(dto.getId());
        if (executeException == null) {
            throw new BmosException(MesResponseCode.EXCEPTION_NOT_EXISTS);
        }
        if (Objects.equals(executeException.getExceptionStatus(), ExceptionStatusEnum.CANCELED)) {
            throw new BmosException(MesResponseCode.EXCEPTION_CANCELED);
        }
        if (Objects.equals(executeException.getExceptionStatus(), ExceptionStatusEnum.INVESTIGATING)) {
            throw new BmosException(MesResponseCode.EXCEPTION_INVESTIGATING);
        }
        executeExceptionMapper.reInvestigate(dto.getId());
        saveOperationHistoryLog(dto.getId(), OperationType.RE_INVESTIGATE, dto.getReInvestigateUserId(),
                ExceptionOperationLogDTO.builder().reInvestigateReason(dto.getReInvestigateReason()).build());
    }

    private void saveOperationHistoryLog(Long id, OperationType operationType, String userId,
                                         ExceptionOperationLogDTO detail) {
        JSONConfig jsonConfig = new JSONConfig();
        jsonConfig.setDateFormat(DatePattern.NORM_DATETIME_PATTERN);
        OperationLogModel build =
                OperationLogModel.builder().businessId(id).module(BusinessModule.EXCEPTION.name()).operationType(operationType.getValue()).detail(JSONUtil.toJsonStr(detail, jsonConfig)).build();
        build.setCreateBy(userId);
        operationHistoryService.save(build);
    }

    @Override
    public CommonPage<ExceptionPageVO> queryExceptionPage(ExceptionPageQueryDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        List<ExecuteException> list = executeExceptionMapper.queryExceptionPage(dto);
        return ExceptionManageConvert.INSTANCE.convert2Page(CommonPage.convertPage(list));
    }

    @Override
    public void saveBatch(List<ExecuteException> list) {
        executeExceptionMapper.insertBatch(list);
        saveOperationHistoryLogs(list);
        // 异常消息通知
        notifyException(list);
    }

    @Override
    public CommonPage<ExceptionPageVO> getBatchExceptionPage(BatchExceptionQueryDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        return ExceptionManageConvert.INSTANCE.convert2Page(CommonPage.convertPage(executeExceptionMapper.queryExceptionPage(dto)));
    }


    @Override
    public void recordModifyException(RecordModifyExceptionDTO dto) {
        Plan plan = planMapper.selectById(dto.getProductPlanId());
        // 只有不在进行中的生产才记录修订异常
        if (plan == null || Objects.equals(ProductPlanStartEnum.STARTING, plan.getStart()) || CollUtil.isEmpty(dto.getItemList())) {
            return;
        }
        ProcedureStepModel procedureStepModel =
                procedureStepModelMapper.selectListByProcedureStepIdAndProcessIdAndVersion(dto.getProcedureStepId(),
                        plan.getProcessId(), plan.getProcessVersion());
        if (procedureStepModel == null) {
            return;
        }
        ProcedureModel procedure = procedureModelMapper.selectById(procedureStepModel.getProcedureModelId());
        if (procedure == null) {
            return;
        }
        List<ExecuteException> collect = dto.getItemList().stream().map(e -> {
            ExecuteException executeException = initModifyException(plan, procedure, procedureStepModel);
            executeException.setRecordUserId(e.getUserId());
            executeException.setRecordUserName(UserUtils.getUsername(e.getUserId()));
            executeException.setRecordTime(LocalDateTime.now());
            ExceptionDescriptionParam build =
                    ExceptionDescriptionParam.builder().originalValue(e.getOriginalValue()).value(e.getValue()).operationTime(e.getOperationTime()).userName(UserUtils.getUsername(e.getUserId())).reviewerName(UserUtils.getUsername(e.getReviewerId())).build();
            String description = dto.getModifyException().getBuildDescription().apply(build);
            executeException.setExceptionDescription(description);
            return executeException;
        }).collect(Collectors.toList());
        executeExceptionMapper.insertBatch(collect);
        saveOperationHistoryLogs(collect);
        // 发送消息提醒
        notifyException(collect);
    }

    private void saveOperationHistoryLogs(List<ExecuteException> models) {
        JSONConfig jsonConfig = new JSONConfig();
        jsonConfig.setDateFormat(DatePattern.NORM_DATETIME_PATTERN);
        List<OperationLogModel> collect = models.stream().map(e -> {
            ExceptionOperationLogDTO logDTO = ExceptionManageConvert.INSTANCE.convert2ExceptionLogDTO(e);
            logDTO.setRecordMode(ExceptionRecordModeEnum.AUTO_RECORD.getName());
            OperationLogModel build =
                    OperationLogModel.builder().businessId(e.getId()).module(BusinessModule.EXCEPTION.name()).operationType(OperationType.SAVE.getValue()).detail(JSONUtil.toJsonStr(logDTO, jsonConfig)).build();
            return build;
        }).collect(Collectors.toList());
        operationHistoryService.saveBatch(collect);
    }

    private ExecuteException initModifyException(Plan plan, ProcedureModel procedureModel,
                                                 ProcedureStepModel procedureStepModel) {
        ExecuteException executeException = new ExecuteException();
        executeException.setExceptionTypeCode(ExceptionTypeDictEnum.ProductReviseException.getValue());
        executeException.setExceptionType(ExceptionTypeDictEnum.ProductReviseException.getName());
        executeException.setProductPlanId(plan.getId());
        executeException.setProcedureModelId(procedureModel.getId());
        executeException.setProcedureStepModelId(procedureStepModel.getId());
        executeException.setProductId(plan.getProductId());
        executeException.setRecordMode(ExceptionRecordModeEnum.AUTO_RECORD);
        executeException.setBatchNo(plan.getBatchNo());
        executeException.setProcessId(plan.getProcessId());
        executeException.setProcessName(plan.getProcessName());
        executeException.setProcessVersion(plan.getProcessVersion());
        executeException.setProductFullName(plan.getProductMergeCode() + StrUtil.DASHED + plan.getProductName());
        executeException.setExceptionStatus(ExceptionStatusEnum.INVESTIGATING);
        executeException.setProcedureId(procedureModel.getProcedureId());
        executeException.setProcedureName(procedureModel.getName());
        executeException.setProcedureStepId(procedureStepModel.getProcedureStepId());
        executeException.setProcedureStepName(procedureStepModel.getName());
        return executeException;
    }


}
