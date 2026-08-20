package com.bmos.mes.service.preparation.measure.service;

import com.bmos.mes.service.preparation.measure.dto.*;
import com.bmos.mes.service.preparation.measure.vo.*;

public interface LiquidPreparationMeasureService {


    /**
     * 获取量取组件实例
     * @param dto
     * @return
     */
    LiquidMeasureInstanceVO getMeasureInstance(LiquidMeasureInstanceQueryDTO dto);

    /**
     * 查询配液单及配液批次详情
     * @param dto
     * @return
     */
    LiquidPreparationDetailVO queryLiquidPreparationPlanDetail(LiquidPreparationPlanDetailQueryDTO dto);

    /**
     * 扫描量取物料件
     * @param dto
     * @return
     */
    LiquidMeasureMaterialPieceVO scanLiquidMeasureMaterialPiece(LiquidMeasureMaterialPieceQueryDTO dto);

    /**
     * 确认量取
     * @param dto
     * @return 确认量取产生的量取批次id
     */
    Long confirmMeasure(LiquidPreparationConfirmMeasureDTO dto);

    /**
     * 添加消耗物料到量取批次
     * @param dto
     */
    void addConsumeStorageMaterial(LiquidPreparationAddMaterialDTO dto);


    /**
     * 根据量取批次id查询量取批次详细信息
     * @param measureBatchId
     * @return
     */
    LiquidPreparationMeasureBatchDetailVO queryMeasureBatchDetailInfo(Long measureBatchId);

    /**
     * 查询量取结果
     * @param dto
     * @return
     */
    LiquidMeasureResultVO queryMeasureResult(LiquidMeasureResultQueryDTO dto);

    /**
     * 量取打码
     * @param dto
     * @return
     */
    MeasurePrintResultVO measureAndPrint(LiquidMeasureAndPrintDTO dto);

    /**
     * 完成量取
     * @param dto
     */
    void completeMeasure(LiquidMeasureCompleteDTO dto);

    /**
     * 更换量取人
     * @param dto
     */
    void changeMeasurer(LiquidMeasureChangeMeasurerDTO dto);

    /**
     * 量取签名
     * @param dto
     */
    void sign(LiquidMeasureSignDTO dto);
}
