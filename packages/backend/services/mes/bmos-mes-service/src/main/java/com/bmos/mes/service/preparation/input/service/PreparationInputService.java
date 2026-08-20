package com.bmos.mes.service.preparation.input.service;

import com.bmos.mes.service.preparation.input.controller.vo.PreparationInputComponentInstanceVO;
import com.bmos.mes.service.preparation.input.controller.vo.PreparationInputPlanVO;
import com.bmos.mes.service.preparation.input.controller.vo.PreparationPlanItemVO;
import com.bmos.mes.service.preparation.input.service.dto.PreparationCompleteDTO;
import com.bmos.mes.service.preparation.input.service.dto.PreparationInputBindPlanDTO;
import com.bmos.mes.service.preparation.input.service.dto.PreparationInputComponentInstanceDTO;
import com.bmos.mes.service.preparation.input.service.dto.PreparationInputDTO;
import com.bmos.mes.service.tag.dto.ScanPreparationInputContainerDTO;
import com.bmos.mes.service.tag.dto.ScanPreparationInputMaterialDTO;
import com.bmos.mes.service.tag.vo.ScanDeviceVO;
import com.bmos.mes.service.tag.vo.ScanInputMaterialVO;

import java.util.List;

public interface PreparationInputService {
    /**
     * 获取配液投入组件实例(获取当前组件绑定的配液单)
     * @param dto
     * @return
     */
    PreparationInputComponentInstanceVO getInputComponentInstance(PreparationInputComponentInstanceDTO dto);

    /**
     * 获取未投入的配液单列表
     * @param productPlanId
     * @return
     */
    List<PreparationPlanItemVO> queryPendingInputPlanList(Long productPlanId);

    /**
     * 绑定配液单
     * @param dto
     * @return 组件实例id
     */
    Long bindPreparationPlan(PreparationInputBindPlanDTO dto);

    /**
     * 根据配液投入组件实例id查询当前配液投入组件绑定的配液单下的投料列表
     * @param componentInstanceId
     * @return
     */
    PreparationInputPlanVO queryInputListByPlanId(Long componentInstanceId);

    /**
     * 配液投入操作
     *
     * @param dto
     */
    void input(PreparationInputDTO dto);

    /**
     * 完成配液投入
     *
     * @param dto
     */
    void complete(PreparationCompleteDTO dto);

    /**
     * 扫描配液投入确认的设备信息(附带校验)
     * 配液投入组件配置工位，须扫描该工位（通过生产批次的产线过滤）下绑定的设备标签，否则提示不可投入该设备；
     * 配液投入组件未配置工位，可扫描工艺绑定的产线的所有工位（通过生产批次的产线过滤）下的设备，否则提示不可投入该设备；
     * @param dto
     * @return
     */
    ScanDeviceVO scanPreparationInputContainer(ScanPreparationInputContainerDTO dto);

    /**
     * 扫描配液投入确认的物料件信息（附带校验）
     * 校验物料件是否为配液投入列表中“待投入”状态的物料件，若不是，提示请扫描待投入的物料件； 此要求由前端提示
     * 校验物料件已预定到当前生产批次，若未预定，提示请扫描预定的物料件标签；
     * 校验物料件有效，否则提示物料件未生效；
     * 校验物料件所在批次是否超出有效期至，通过状态校验，若超过，提示物料件已超过有效期；
     * 校验物料件已出暂存货位，若物料件仍在暂存货位中，提示物料件未出库；
     * @param dto
     */
    ScanInputMaterialVO scanPreparationInputMaterial(ScanPreparationInputMaterialDTO dto);
}
