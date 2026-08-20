package com.bmos.mes.service.output.weigh.service;

import com.bmos.mes.service.ingredient.weigh.vo.WeighBalanceEquipment;
import com.bmos.mes.service.ingredient.weigh.vo.WeighResult;
import com.bmos.mes.service.output.weigh.dto.*;
import com.bmos.mes.service.output.weigh.vo.OutputMaterialItem;
import com.bmos.mes.service.output.weigh.vo.OutputWeighProcessVO;
import com.bmos.mes.service.storage.manage.vo.StorageMaterialSimpleBatchVO;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/28 15:26
 */
public interface IOutputWeighService {

    /**
     * 根据工位id查询秤具信息
     *
     * @param stationIds
     * @return
     */
    List<WeighBalanceEquipment> getBalanceListByStationIds(List<Long> stationIds);

    /**
     * 查询称量流程
     * @param query
     * @return
     */
    @Nullable
    OutputWeighProcessVO getOutputWeighProcess(OutputWeighProcessQuery query);

    /**
     * 去人称量人员
     * @param dto
     * @return
     */
    Long makeSureWeigher(OutputMakeSureWeigherDTO dto);

    /**
     * 确认称量批次
     *
     * @param dto
     * @return
     */
    void makeSureBatch(OutputMakeSureBatchDTO dto);

    /**
     * 称量打码
     * @param dto
     * @return 物料件号
     */
    List<WeighResult.WeighResultItem> weighAndPrint(OutputWeighAndPrintDTO dto);

    /**
     * 签名
     * @param dto
     */
    void sign(OutputWeighSignDTO dto);

    /**
     * 切换称量人员
     *
     * @param dto
     * @return
     */
    void changeWeigher(OutputChangeWeigherDTO dto);

    /**
     * 称量作废
     *
     * @param dto
     */
    void scrap(OutputScrapDTO dto);

    /**
     * 获取产出批次中的中间品物料列表
     * @param outputWeighProcessId 产出称量流程id
     * @return
     */
    List<OutputMaterialItem> getMiddleMaterialList(Long outputWeighProcessId);

    /**
     * 获取关联批次中的原辅包物料列表
     * @param outputWeighProcessId 产出称量流程id
     * @return
     */
    List<OutputMaterialItem> getUnionOriginMaterialList(Long outputWeighProcessId);

    /**
     * 根据物料id和批次编号查询批次信息
     * @param materialId 物料id
     * @param batchNo 物料批次编号
     * @return
     */
    StorageMaterialSimpleBatchVO queryBatchInfo(Long materialId, String batchNo);

    /**
     * 校验产出称量组件物料件签名
     * @param validateSignList 校验列表
     * @return true 签名完成 false 存在未签名的物料件
     */
    Boolean validateComponentSign(List<OutputWeighValidateSignDTO> validateSignList);
}
