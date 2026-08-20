package com.bmos.mes.service.output.finished.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.execute.dto.BusinessDataHandleBaseDTO;
import com.bmos.mes.service.execute.dto.RecordItemLatestDataQueryDTO;
import com.bmos.mes.service.execute.dto.UniqueComponentQueryDTO;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.service.ExecuteFormDataService;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.execute.vo.FormDataItemVO;
import com.bmos.mes.service.formula.service.ProductFormulaConfigureService;
import com.bmos.mes.service.output.finished.convert.FinishedProductOutputConverter;
import com.bmos.mes.service.output.finished.dto.SaveFinishedProductOutputDTO;
import com.bmos.mes.service.output.finished.dto.ValidateFinishedProductComponentDTO;
import com.bmos.mes.service.output.finished.mapper.FinishedProductOutputMapper;
import com.bmos.mes.service.output.finished.mapper.FinishedProductOutputResultMapper;
import com.bmos.mes.service.output.finished.model.FinishedProductOutput;
import com.bmos.mes.service.output.finished.model.FinishedProductOutputResult;
import com.bmos.mes.service.output.finished.service.FinishedProductOutputService;
import com.bmos.mes.service.output.finished.vo.FinishedProductComponentDetailVO;
import com.bmos.mes.service.output.finished.vo.FinishedProductOutputListVO;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.plan.info.service.PlanService;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.process.model.ProcessVersion;
import com.bmos.mes.service.process.service.ProcedureStepConfigService;
import com.bmos.mes.service.process.service.ProcedureStepModelService;
import com.bmos.mes.service.process.service.ProcessVersionService;
import com.bmos.mes.service.product.model.ProductMaterial;
import com.bmos.mes.service.product.service.ProductMaterialService;
import com.bmos.mes.service.record.business.model.ProductFormulaInfo;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.business.strategy.FinishedProductOutputComponentStrategy;
import com.bmos.mes.service.record.convert.RecordComponentConvert;
import com.bmos.mes.service.record.service.BatchRecordComponentService;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.mes.service.trace.material.service.IMaterialTraceHistoryService;
import com.bmos.unit.service.UnitCache;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FinishedProductOutputServiceImpl implements FinishedProductOutputService {

    @Resource
    private PlanService planService;

    @Resource
    private ProcedureStepModelService procedureStepModelService;

    @Resource
    private ProcedureStepConfigService procedureStepConfigService;

    @Resource
    private ProductMaterialService productMaterialService;

    @Resource
    private FinishedProductOutputMapper finishedProductOutputMapper;

    @Resource
    private FinishedProductOutputResultMapper resultMapper;

    @Resource
    private UnitCache unitCache;

    @Resource
    private ExecuteFormDataService executeFormDataService;

    @Resource
    private BatchRecordComponentService batchRecordComponentService;

    @Resource
    private ProcessVersionService processVersionService;

    @Resource
    private ProductFormulaConfigureService productFormulaConfigureService;

    @Resource
    private FinishedProductOutputComponentStrategy outputComponentStrategy;

    @Resource
    private IMaterialTraceHistoryService materialTraceHistoryService;

    @Override
    public FinishedProductComponentDetailVO getComponentDetail(ValidateFinishedProductComponentDTO dto) {
        Plan plan = planService.getById(dto.getProductPlanId());
        if (plan == null) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        ProcedureStepModel procedureStepModel = procedureStepModelService.getById(dto.getProcedureStepModelId());
        if (procedureStepModel == null) {
            throw new BmosException(MesResponseCode.PROCEDURE_NOT_EXIST);
        }
        ProductMaterial product = productMaterialService.selectById(plan.getProductId());
        if (BooleanUtil.isFalse(product.getFinishProduct())) {
            throw new BmosException(MesResponseCode.NOT_FINISHED_PRODUCT_PROCESS);
        }
        FinishedProductOutput finishedProductOutput =
                finishedProductOutputMapper.selectUnique(UniqueComponentQueryDTO.builder()
                        .componentId(dto.getComponentId())
                        .copyVersion(dto.getCopyVersion())
                        .productPlanId(dto.getProductPlanId())
                        .reuse(procedureStepModel.getReusable())
                        .recordItemId(procedureStepModel.getRecordItemId())
                        .recordVersionId(procedureStepModel.getRecordVersionId())
                        .procedureStepModelId(dto.getProcedureStepModelId())
                        .build());
        if (finishedProductOutput == null) {
            finishedProductOutput = new FinishedProductOutput();
            finishedProductOutput.setComponentId(dto.getComponentId());
            finishedProductOutput.setProductId(plan.getProductId());
            finishedProductOutput.setProductPlanId(plan.getId());
            finishedProductOutput.setProductName(plan.getProductName());
            finishedProductOutput.setProductMergeCode(plan.getProductMergeCode());
            finishedProductOutput.setCopyVersion(dto.getCopyVersion());
            finishedProductOutput.setReuse(procedureStepModel.getReusable());
            finishedProductOutput.setRecordItemId(procedureStepModel.getRecordItemId());
            finishedProductOutput.setRecordVersionId(procedureStepModel.getRecordVersionId());
            finishedProductOutput.setProductBatchNo(plan.getBatchNo());
            finishedProductOutput.setSpecification(plan.getProductSpecification());
            finishedProductOutput.setProcedureStepModelId(BooleanUtil.isTrue(procedureStepModel.getReusable()) ? 0 :
                    dto.getProcedureStepModelId());
            finishedProductOutputMapper.insert(finishedProductOutput);
        }
        FinishedProductComponentDetailVO detail = new FinishedProductComponentDetailVO();
        detail.setId(finishedProductOutput.getId());
        detail.setProductName(plan.getProductName());
        detail.setProductMergeCode(plan.getProductMergeCode());
        detail.setProductBatchNo(plan.getBatchNo());
        detail.setSpecification(plan.getProductSpecification());
        detail.setUnitId(product.getUnitId());
        detail.setUnitName(unitCache.getGlobalUnitName(detail.getUnitId()));
        detail.setProductId(finishedProductOutput.getProductId());
        return detail;
    }

    @Override
    public List<FinishedProductOutputListVO> getFinishedProductOutputList(Long id) {
        List<FinishedProductOutputResult> results = resultMapper.selectByFinishedOutputId(id);
        List<FinishedProductOutputListVO> list =
                FinishedProductOutputConverter.INSTANCE.convertToFinishedProductOutputListVO(results);
        list.forEach(e -> e.setUnitName(unitCache.getGlobalUnitName(e.getUnitId())));
        return list;
    }

    @Override
    public void saveFinishedProductOutputList(SaveFinishedProductOutputDTO dto) {
        Long id = dto.getId();
        FinishedProductOutput finishedProductOutput = finishedProductOutputMapper.selectById(id);
        if (finishedProductOutput == null) {
            throw new BmosException(MesResponseCode.FINISHED_PRODUCT_OUTPUT_INFO_ERROR);
        }
        List<SaveFinishedProductOutputDTO.FinishedProductOutputInfo> outputList = dto.getOutputList();
        List<FinishedProductOutputResult> collect = outputList.stream().map(e -> {
            FinishedProductOutputResult result = new FinishedProductOutputResult();
            result.setOutputFinishedProductId(id);
            result.setNumber(e.getNumber());
            result.setSingleQuantity(e.getSingleQuantity());
            result.setUnitId(e.getUnitId());
            result.setProductName(finishedProductOutput.getProductName());
            result.setProductId(finishedProductOutput.getProductId());
            result.setProductBatchNo(finishedProductOutput.getProductBatchNo());
            result.setSpecification(finishedProductOutput.getSpecification());
            result.setProductMergeCode(finishedProductOutput.getProductMergeCode());
            result.setOperatorId(dto.getOperatorId());
            return result;

        }).collect(Collectors.toList());
        resultMapper.insertBatch(collect);
        // 处理组件数据回填
        List<ExecuteFormData> results = generateExecuteFormData(dto, dto.getId());
        executeFormDataService.saveResultsAndHandleRelationComponentData(results, dto);

        // 保存成品产出的物料追溯记录
        materialTraceHistoryService.saveTraceHistory(dto.getProcedureStepModelId(), finishedProductOutput, collect);
    }

    private List<ExecuteFormData> generateExecuteFormData(SaveFinishedProductOutputDTO dto, Long id) {
        ComponentListVO component = batchRecordComponentService.selectUsedComponentDetail(dto.getRecordVersionId(),
                dto.getRecordItemId(), dto.getComponentId());
        ProductionDetailInfo info = new ProductionDetailInfo();
        info.setDto(RecordComponentConvert.INSTANCE.convertToBusinessComponentBatchSaveDTO(dto));
        RecordItemLatestDataQueryDTO queryDTO = getRecordItemLatestDataQueryDTO(dto, component);
        List<FormDataItemVO> recordItemLatestData = executeFormDataService.getRecordItemLatestData(queryDTO);
        info.setFormDataCollection(recordItemLatestData);
        List<ExecuteFormData> results = new ArrayList<>();
        List<FinishedProductOutputResult> outputResults = resultMapper.selectByFinishedOutputId(id);
        info.setOutputResults(outputResults);
        ProcessVersion processVersion = processVersionService.getByProcessIdAndVersion(dto.getProcessId(),
                dto.getProcessVersion());
        ProductFormulaInfo formulaInfo =
                productFormulaConfigureService.getProductFormulaInfo(processVersion.getProductFormulaVersionId());
        info.setFormulaInfo(formulaInfo);
        ProcedureStepModel procedureStepModel = procedureStepModelService.getById(dto.getProcedureStepModelId());
        List<BusinessComponentConfigDetailVO> configs =
                procedureStepConfigService.getComponentConfigByProcedureStepModel(procedureStepModel);
        Map<Long, BusinessComponentConfigDetailVO> configMap = CollectionUtils.convertMap(configs,
                BusinessComponentConfigDetailVO::getComponentId);
        outputComponentStrategy.handleBusinessComponent(results, component, info, configMap, null);
        return results;
    }

    private RecordItemLatestDataQueryDTO getRecordItemLatestDataQueryDTO(BusinessDataHandleBaseDTO dto, ComponentListVO component) {
        List<Long> fieldIds = new ArrayList<>();
        recGetComponentFieldList(component, fieldIds);
        RecordItemLatestDataQueryDTO queryDTO = new RecordItemLatestDataQueryDTO();
        queryDTO.setReuse(dto.getReuse());
        queryDTO.setDiscard(false);
        queryDTO.setCopyVersion(dto.getCopyVersion());
        queryDTO.setProductPlanId(dto.getProductPlanId());
        queryDTO.setProcedureStepId(dto.getProcedureStepId());
        queryDTO.setFieldIdList(fieldIds);
        queryDTO.setRecordItemId(dto.getRecordItemId());
        return queryDTO;
    }

    private void recGetComponentFieldList(ComponentListVO vo, List<Long> result){
        result.add(vo.getFieldId());
        if(CollUtil.isNotEmpty(vo.getChildren())){
            vo.getChildren().forEach(e->{
                recGetComponentFieldList(e, result);
            });
        }
    }
}

