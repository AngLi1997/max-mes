package com.bmos.mes.service.ingredient.weigh.service;

import com.bmos.mes.service.ingredient.weigh.dto.*;
import com.bmos.mes.service.ingredient.weigh.vo.*;
import com.bmos.mes.service.tag.dto.ScanDeviceCodeDTO;
import com.bmos.mes.service.tag.dto.ScanWeighMaterialCodeDTO;
import com.bmos.mes.service.tag.vo.ScanCargoPositionVO;
import com.bmos.mes.service.tag.vo.ScanDeviceVO;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/19 09:20
 */
public interface IIngredientWeighService {

    /**
     * 扫描物料件号查询物料件信息
     *
     * @param scanQuery 查询参数
     * @return 物料件信息
     */
    @Nullable
    IngredientWeighStorageMaterialVO queryWeighStorageMaterial(ScanWeighMaterialCodeDTO scanQuery);

    /**
     * 查询未完成的配料单列表
     *
     * @param productPlanId 生产计划id
     * @param batchNo       生产批号
     * @return
     */
    List<IngredientPlanItemVO> queryPendingIngredientPlanList(Long productPlanId, String batchNo);

    /**
     * 查询配料单详情
     *
     * @param id 配料单id
     * @param componentId 组件id
     * @param procedureStepModelId 工序步骤模型id
     * @return
     */
    @Nullable
    IngredientPlanDetailVO queryIngredientPlanById(Long id, Long componentId, Long procedureStepModelId);

    /**
     * 确认称量人员
     *
     * @param dto
     */
    void makeSureWeigh(IngredientMakeSureWeighDTO dto);

    /**
     * 添加称量消耗物料件
     *
     * @param dto
     */
    void addConsumeStorageMaterial(IngredientWeighConsumeStorageMaterialDTO dto);

    /**
     * 称量打码
     *
     * @param dto
     * @return 是否称量完成
     */
    WeighResult weighAndPrint(IngredientWeighAndPrintDTO dto);

    /**
     * 批量签名
     *
     * @param ingredientWeighSignDTO
     */
    void sign(IngredientWeighSignDTO ingredientWeighSignDTO);

    /**
     * 根据称量计划id和批次id查询称量批次详情
     *
     * @param ingredientPlanId       称量计划id
     * @param storageMaterialBatchId 称量批次id
     * @return
     */
    @Nullable
    WeighStorageMaterialBatchVO queryWeighDetailByPlanIdAndBatchId(Long ingredientPlanId, Long storageMaterialBatchId);

    /**
     * 查询详情
     *
     * @param inputWeighProcessQuery
     * @return
     */
    IngredientWeighProcessVO getIngredientWeighProcess(InputWeighProcessQuery inputWeighProcessQuery);

    ScanDeviceVO scanDeviceCode(ScanDeviceCodeDTO scanQuery);

    List<WeighBalanceEquipment> getBalanceListByStationIds(List<Long> stationIds);

    /**
     * 完成称量
     *
     * @param weighFinishDTO
     */
    void finish(IngredientWeighFinishDTO weighFinishDTO);

    /**
     * 根据称量计划单id查询称量列表
     *
     * @param query 称量计划单查询
     * @return
     */
    IngredientWeighStorageMaterialListVO queryResult(IngredientWeighResultQuery query);

    /**
     * 更新称量人员
     *
     * @param dto
     */
    void changeWeigher(IngredientChangeWeigherDTO dto);

    /**
     * 扫描称量组件容器信息(带校验)
     * @param code 设备编码
     * @return
     */
    ScanDeviceVO scanWeighContainerCode(String code);

    /**
     * 扫描称量组件货位信息(带校验)
     * @param code 货位编码
     * @return
     */
    ScanCargoPositionVO scanWeighPositionCode(String code);

    /**
     * 校验组件签名
     * @param validateSignList 校验列表
     * @return true 签名完成 false 存在未签名的物料件
     */
    Boolean validateComponentSign(List<IngredientWeighValidateSignDTO> validateSignList);
}
