package com.bmos.mes.service.preparation.produce.service;

import com.bmos.mes.service.preparation.produce.controller.vo.*;
import com.bmos.mes.service.preparation.produce.service.dto.*;
import com.bmos.mes.service.tag.vo.ScanCargoPositionVO;
import com.bmos.mes.service.tag.vo.ScanDeviceVO;

import java.util.List;

/**
 * 配液产出
 */
public interface PreparationProduceService {

    /**
     * 获取当前选择的产出组件选择的配液单信息以及产出批次信息
     * @param dto
     * @return
     */
    PreparationProduceProgressVO getPreparationProduceProgress(PreparationProduceProgressDTO dto);

    /**
     * 获取当前生产批次下的配液单列表
     * @param productPlanId
     * @return
     */
    List<PreparationProducePlanVO> getProducePlanList(Long productPlanId);

    /**
     * 通过配液单查询当前配液单的配液计划组件中的产出中间品
     * @param preparationPlanId
     * @return
     */
    PreparationProduceMaterialVO queryMaterial(Long preparationPlanId);

    /**
     * 根据输入的物料批次编号以及物料编号查询物料批次信息
     * @param dto
     * @return
     */
    PreparationProduceMaterialBatchVO queryMaterialBatch(PreparationMaterialBatchDTO dto);

    /**
     * 获取配液产出确认的复核人员列表
     *  在配液产出组件绑定工位（通过生产批次的产线过滤）下且拥有对应权限码配置角色的人员；
     *  配液产出组件未绑定工位（通过生产批次的产线过滤），则“复核人”可选范围为工艺绑定的产线的所有工位下且拥有对应权限码配置角色的人员；
     * @param dto
     * @return
     */
    List<PreparationProduceUserVO> queryCheckUserList(PreparationProduceCheckUserDTO dto);

    /**
     * 配液产出确认
     * 生成对应的配液产出组件执行流程数据
     * 签名日志：记录2条签名日志，签名动作：配液产出-操作、配液产出-复核；
     * @param dto
     * @return
     */
    Long produceConfirm(ProduceConfirmUserDTO dto);

    /**
     * 配液产出（批记录回填）
     *
     * @param dto
     * @return
     */
    String produceHandle(PreparationProduceDTO dto);

    /**
     * 查看当前配液产出组件产出的物料件信息
     * @param progressId
     * @return
     */
    ProduceVO queryProduce(Long progressId);

    /**
     * 产出签名
     * @param dto
     */
    void sign(ProducerSignDTO dto);

    /**
     * 更换产出人员
     * @param dto
     * @return
     */
    Long changeProducer(ProduceChangeUserDTO dto);

    /**
     * 配液产出作废
     * @param dto
     */
    void scrap(ProduceScrapDTO dto);

    /**
     * 【配液产出】通过设备编码查询设备信息（附带校验）
     * 校验容器是否已有物料件
     * 校验容器是否可用
     * @param code
     * @return
     */
    ScanDeviceVO scanPreparationProduceContainer(String code);

    /**
     * 【配液产出】通过货位编码查询货位信息（附带校验）
     * 校验当前登录用户是否有此货位的数据权限
     * @param code
     * @return
     */
    ScanCargoPositionVO scanPreparationCargoCode(String code);
}
