package com.bmos.platform.service.factory.service;

import com.bmos.platform.common.enums.factory.FactoryModuleEnum;
import com.bmos.platform.service.equipment.controller.vo.ChoiceBoxVO;
import com.bmos.platform.service.factory.controller.vo.StationModuleTreeNodeVO;
import com.bmos.platform.service.factory.controller.vo.ModuleVO;
import com.bmos.platform.service.factory.model.EquipmentStation;
import com.bmos.platform.service.factory.service.dto.ModuleSaveDTO;
import com.bmos.platform.service.factory.service.dto.ModuleUpdateDTO;

import java.util.List;

public interface FactoryModuleService {

    /**
     * 保存模型
     * @param dto
     * @param typeEnum
     */
    void saveModule(ModuleSaveDTO dto, FactoryModuleEnum typeEnum);

    /**
     * 更新模型
     * @param dto
     */
    void updateModule(ModuleUpdateDTO dto);

    /**
     * 删除模型
     *
     * @param id
     */
    void deleteModule(Long id);

    /**
     * 获取模型树
     * @param typeEnum
     * @return
     */
    List<StationModuleTreeNodeVO> getModuleTree(FactoryModuleEnum typeEnum);

    /**
     * 根据id获取业务模型信息
     * @param id
     * @return
     */
    ModuleVO getModuleTreeInfo(Long id);

    List<ChoiceBoxVO> listProductionLine();

    List<ChoiceBoxVO> listProductionRoom(List<String> lineIdList);

    List<EquipmentStation> getStationByProductionId(Long productionLineId);
}
