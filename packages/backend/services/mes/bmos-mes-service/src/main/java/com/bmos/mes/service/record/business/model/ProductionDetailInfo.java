package com.bmos.mes.service.record.business.model;

import com.bmos.mes.common.model.component.CustomFieldDetailInfo;
import com.bmos.mes.service.equipment.mapper.entity.ProcedureEquipmentAcquisition;
import com.bmos.mes.service.equipment.service.dto.EquipmentAcquisitionPointDTO;
import com.bmos.mes.service.execute.dto.BusinessComponentBatchSaveDTO;
import com.bmos.mes.service.execute.vo.FormDataItemVO;
import com.bmos.mes.service.facotry.service.data.FactoryRoomInfo;
import com.bmos.mes.service.ingredient.input.model.IngredientInputRecordDetail;
import com.bmos.mes.service.ingredient.plan.model.IngredientMaterialBatchDetailInfo;
import com.bmos.mes.service.ingredient.weigh.vo.IngredientWeighRecordComponentView;
import com.bmos.mes.service.output.finished.model.FinishedProductOutputResult;
import com.bmos.mes.service.output.weigh.vo.OutputWeighRecordComponentView;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.preparation.measure.service.vo.MeasuredBatchDetailVO;
import com.bmos.mes.service.preparation.measure.vo.MeasureResultRecordVO;
import com.bmos.mes.service.preparation.plan.model.LiquidPreparationMaterialBatchDetailInfo;
import com.bmos.mes.service.product.model.ProductMaterial;
import com.bmos.mes.service.requisition.model.RequisitionMaterialReserved;
import com.bmos.mes.service.requisition.vo.RequisitionReceivedBatchInfo;
import com.bmos.mes.service.storage.manage.model.StorageMaterialChargeRecycle;
import com.bmos.platform.facade.equipment.vo.EquipmentInfoFeignVO;
import com.bmos.unit.service.UnitCache;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Accessors(chain = true)
public class ProductionDetailInfo {

    /**
     * 生产计划
     */
    private Plan plan;

    /**
     * 产品信息
     */
    private ProductMaterial product;

    /**
     * 工艺信息
     */
    private ProcessDetailInfo process;

    /**
     * 产品配方信息
     */
    private ProductFormulaInfo formulaInfo;

    /**
     * 基础信息
     */
    private BusinessComponentBatchSaveDTO dto;

    /**
     * 已有值组件id列表
     */
    private Collection<FormDataItemVO> formDataCollection;

    /**
     * 领料信息
     */
    private List<RequisitionMaterialReserved> repositoryReservedList;

    /**
     * 单位缓存
     */
    private UnitCache unitCache;

    /**
     * 配料信息
     */
    private List<IngredientMaterialBatchDetailInfo> ingredientMaterialBatchList;

    private Long ingredientStorageBatchId;

    List<Long> finishedStorageBatchIdSummaryList = new ArrayList<>();

    List<Long> finishedMaterialIdSummaryList = new ArrayList<>();

    /**
     * 领料信息
     */
    private List<RequisitionReceivedBatchInfo> requisitionReceivedBatchList;


    private EquipmentInfoFeignVO equipmentInfo;


    private List<EquipmentAcquisitionPointDTO> equipmentAcquisitionPointList;


    private LocalDateTime acquisitionTime;

    /**
     * 投料回收列表
     */
    private List<StorageMaterialChargeRecycle> chargeRecycleList;

    /**
     * 成品产出列表
     */
    private List<FinishedProductOutputResult> outputResults;

    /**
     * 配料投入详情列表
     */
    private List<IngredientInputRecordDetail> ingredientInputRecordDetailList;

    /**
     * 产出称量记录列表
     */
    private List<OutputWeighRecordComponentView> outputWeighRecords;

    /**
     * 配料称量记录列表
     */
    private List<IngredientWeighRecordComponentView> ingredientWeighRecords;

    /**
     * 房间信息
     */
    private FactoryRoomInfo factoryRoomInfo;

    /**
     * 当次称量物料id
     */
    private Long ingredientWeighMaterialId;
    /**
     * 物料预定记录列表
     */
    private List<StorageMaterialDetailInfo> storageMaterialDetailInfoList;

    /**
     * 签名url
     */
    private HandleSignInfo signInfo;

    /**
     * 配液计划批次信息
     */
    private List<LiquidPreparationMaterialBatchDetailInfo> liquidPreparationBatchList;

    /**
     * 配液量取记录信息
     */
    private List<MeasureResultRecordVO> measureResultRecordList;

    private List<MeasuredBatchDetailVO> measuredBatchDetailVOS;

    /**
     * 当前量取批次配方物料id
     */
    private Long currentMeasureFormulaMaterialId;

    /**
     * 自定义字段信息列表
     */
    private List<CustomFieldDetailInfo> customFieldList;

    /**
     * 设备数采组件数采数据
     */
    private List<ProcedureEquipmentAcquisition> equipmentAcquisitionList;

}
