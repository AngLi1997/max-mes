package com.bmos.mes.service.plan.info.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.exception.dto.BatchExceptionQueryDTO;
import com.bmos.mes.service.exception.service.ExceptionManageService;
import com.bmos.mes.service.exception.vo.ExceptionPageVO;
import com.bmos.mes.service.plan.info.convert.PlanConverter;
import com.bmos.mes.service.plan.info.convert.PlanTraceConverter;
import com.bmos.mes.service.plan.info.convert.ProductPlanRelationConverter;
import com.bmos.mes.service.plan.info.dto.PlanRetraceInfoPageDTO;
import com.bmos.mes.service.plan.info.dto.PlanTraceablePageDTO;
import com.bmos.mes.service.plan.info.dto.ProductPlanBatchDTO;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.plan.info.service.PlanRetraceService;
import com.bmos.mes.service.plan.info.service.PlanService;
import com.bmos.mes.service.plan.info.vo.*;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.process.service.task.ProcedureTaskInstanceHistoryService;
import com.bmos.mes.service.process.mapper.ProcedureModelMapper;
import com.bmos.mes.service.process.mapper.ProcedureStepModelMapper;
import com.bmos.mes.service.process.mapper.task.ProcedureTaskInstanceHistoryMapper;
import com.bmos.mes.service.process.model.ProcedureModel;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.process.model.task.ProcedureTaskInstanceHistory;
import com.bmos.mes.service.trace.material.entity.MaterialTraceHistoryDO;
import com.bmos.mes.service.trace.material.mapper.IMaterialTraceHistoryMapper;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.orchestrator.engine.core.query.resp.ExecutionUserTaskResp;
import com.bmos.orchestrator.engine.core.query.service.ExecutionQueryService;
import com.bmos.platform.facade.factory.dto.BatchRoomCleanPageDTO;
import com.bmos.platform.facade.factory.feign.FactoryFeign;
import com.bmos.platform.facade.factory.vo.BatchRoomCleanInfoVO;
import com.bmos.platform.facade.factory.vo.FactoryLineDetailFeignVO;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class PlanRetraceServiceImpl implements PlanRetraceService {
    @Resource
    private ExceptionManageService exceptionManageService;

    @Resource
    private PlanService planService;

    @Resource
    private FactoryFeign factoryFeign;

    @Resource
    private IMaterialTraceHistoryMapper materialTraceHistoryMapper;

    @Resource
    private ProcedureTaskInstanceHistoryService taskInstanceHistoryService;


    @Resource
    private ExecutionQueryService executionQueryService;

    @Resource
    private ProcedureStepModelMapper procedureStepModelMapper;

    @Resource
    private ProcedureModelMapper procedureModelMapper;

    @Resource
    private ProcedureTaskInstanceHistoryMapper taskInstanceHistoryMapper;

    @Override
    public CommonPage<ProductPlanBatchPageVO> planBatchRetracePage(ProductPlanBatchDTO dto) {
        PlanTraceablePageDTO planTraceablePageDTO = PlanConverter.INSTANCE.convert2PlanTraceablePageDTO(dto);
        if (dto.getProductId() != null) {
            planTraceablePageDTO.getProductIds().add(dto.getProductId());
        }
        CommonPage<PlanPageVO> planPageVOCommonPage = planService.pageTraceable(planTraceablePageDTO);
        return PlanConverter.INSTANCE.convert2BatchRetracePage(planPageVOCommonPage);
    }

    @Override
    public PlanRetraceInfoVO detailInfo(PlanRetraceInfoPageDTO dto) {
        Plan plan = planService.getById(dto.getPlanId());
        PlanRetraceInfoVO result = PlanConverter.INSTANCE.convert2PlanRetraceInfoVO(plan);
        // 修改为根据多个产线获取
        List<FactoryLineDetailFeignVO> list =
                FeignUtils.handleRequest(data -> factoryFeign.getLineDetailByLineIds(data, true),
                        Collections.singleton(plan.getProductionLineId())).getData();
        FactoryLineDetailFeignVO line = CollUtil.getFirst(list);
        if (line != null) {
            result.setProductionLineName(line.getCode() + StrUtil.DASHED + line.getName());
        }
        return result;
    }

    @Override
    public CommonPage<PlanRetraceExecutePageVO> executeTracePage(PlanRetraceInfoPageDTO dto) {
        return taskInstanceHistoryService.executeTracePage(dto);
    }

    @Override
    public CommonPage<PlanRetraceMaterialPageVO> materialTracePage(PlanRetraceInfoPageDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), dto.getOrderSql());
        List<MaterialTraceHistoryDO> list = materialTraceHistoryMapper.queryPage(dto);
        CommonPage<MaterialTraceHistoryDO> page = CommonPage.convertPage(list);
        return ProductPlanRelationConverter.INSTANCE.convertToMaterialPage(page);
    }

    @Override
    public CommonPage<PlanRetraceEquipmentPageVO> equipmentTracePage(PlanRetraceInfoPageDTO dto) {
        // TODO 易高辉
        return null;
    }

    @Override
    public CommonPage<PlanRetraceRoomPageVO> roomTracePage(PlanRetraceInfoPageDTO dto) {
        // 张若雨
        Plan plan = planService.getById(dto.getPlanId());
        if (Objects.isNull(plan)){
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        BatchRoomCleanPageDTO pageDTO = PlanTraceConverter.INSTANCE.convert2FeignRoomPageVO(dto, plan.getBatchNo());
        ResponseInfo<CommonPage<BatchRoomCleanInfoVO>> responseInfo = FeignUtils.handleRequest(data -> factoryFeign.getRoomCleanInfoPage(pageDTO), pageDTO);
        CommonPage<BatchRoomCleanInfoVO> data = responseInfo.getData();
        if (Objects.isNull(data)){
            return CommonPage.CommonPage(new ArrayList<>(), 0L, dto);
        }
        List<PlanRetraceRoomPageVO> resPageVOList = PlanTraceConverter.INSTANCE.convertRoomPageVOList(data.getList());
        return CommonPage.CommonPage(resPageVOList, Long.valueOf(data.getTotal()), dto);
    }

    @Override
    public CommonPage<PlanRetraceDeviationPageVO> procedureTracePage(PlanRetraceInfoPageDTO dto) {
        BatchExceptionQueryDTO batchExceptionQueryDTO = PlanConverter.INSTANCE.convert2BatchExceptionQueryDTO(dto);
        batchExceptionQueryDTO.setTraceQuery(true);
        CommonPage<ExceptionPageVO> batchExceptionPage = exceptionManageService.getBatchExceptionPage(batchExceptionQueryDTO);
        return PlanConverter.INSTANCE.convert2DeviationPageVO(batchExceptionPage);
    }

    @Override
    public List<ProcedureStepTaskExecuteVO> getProcedureStepTaskExecuteList(Long id) {
        Plan plan = planService.getById(id);
        List<ExecutionUserTaskResp> taskRespList = executionQueryService.findHistoryUserTaskByBusinessKey(String.valueOf(id));
        List<ProcedureModel> procedureModels = procedureModelMapper.selectByProcessIdAndVersion(plan.getProcessId(), plan.getProcessVersion());
        List<ProcedureStepModel> stepModels =
                procedureStepModelMapper.getStepModelByProcessIdAndVersion(plan.getProcessId(),
                        plan.getProcessVersion());
        List<ProcedureTaskInstanceHistory> taskList = taskInstanceHistoryMapper.selectByProductPlanId(id);
        return PlanTraceConverter.INSTANCE.convert2StepTaskExecuteVO(procedureModels, stepModels, taskRespList, taskList);
    }
}
