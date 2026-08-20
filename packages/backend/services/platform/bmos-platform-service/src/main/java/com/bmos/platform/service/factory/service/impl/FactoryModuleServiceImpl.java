package com.bmos.platform.service.factory.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.tree.TreeUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mybatis.CustomIdGenerator;
import com.bmos.platform.common.enums.factory.FactoryModuleEnum;
import com.bmos.platform.common.exception.PlatformResponseCode;
import com.bmos.platform.service.equipment.controller.vo.ChoiceBoxVO;
import com.bmos.platform.service.factory.controller.vo.StationModuleTreeNodeVO;
import com.bmos.platform.service.factory.controller.vo.ModuleVO;
import com.bmos.platform.service.factory.convert.FactoryModuleConverter;
import com.bmos.platform.service.factory.mapper.FactoryModuleMapper;
import com.bmos.platform.service.factory.model.FactoryModule;
import com.bmos.platform.service.factory.model.EquipmentStation;
import com.bmos.platform.service.factory.repository.FactoryStationRepository;
import com.bmos.platform.service.factory.repository.LineRepository;
import com.bmos.platform.service.factory.repository.RoomRepository;
import com.bmos.platform.service.factory.service.FactoryModuleService;
import com.bmos.platform.service.factory.service.dto.ModuleSaveDTO;
import com.bmos.platform.service.factory.service.dto.ModuleUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FactoryModuleServiceImpl implements FactoryModuleService {

    @Autowired
    private FactoryModuleMapper factoryModuleMapper;

    @Autowired
    private FactoryStationRepository factoryStationRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    LineRepository lineRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveModule(ModuleSaveDTO dto, FactoryModuleEnum typeEnum) {
        // 校验模型编码与type是否重复
        if (factoryModuleMapper.existsByCode(dto.getCode(), typeEnum.getType())){
            throw new BmosException(PlatformResponseCode.FACTORY_MODULE_CODE_EXIST);
        }
        // 新增模型
        FactoryModule factoryModule = FactoryModuleConverter.INSTANCE.convertToModel(dto);
        factoryModule.setType(typeEnum.getType());
        Long id = CustomIdGenerator.nextId();
        factoryModule.setId(id);
        factoryModuleMapper.insert(factoryModule);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateModule(ModuleUpdateDTO dto) {
        FactoryModule factoryModule = factoryModuleMapper.selectById(dto.getId());
        if (Objects.isNull(factoryModule)){
            throw new BmosException(PlatformResponseCode.FACTORY_MODULE_NOT_EXIST);
        }
        factoryModule.setName(dto.getName());
        factoryModuleMapper.updateById(factoryModule);
    }

    @Override
    public void deleteModule(Long id) {
        validDeleteModule(id);
        factoryModuleMapper.deleteById(id);
    }

    /**
     * 校验模型id下是否有孩子节点
     * @param id
     */
    private void validDeleteModule(Long id) {
        FactoryModule factoryModule = factoryModuleMapper.selectById(id);
        if (Objects.isNull(factoryModule)){
            throw new BmosException(PlatformResponseCode.FACTORY_MODULE_NOT_EXIST);
        }
        // 查询当前模型id下是否有孩子节点
        if (factoryModuleMapper.existChild(id)){
            throw new BmosException(PlatformResponseCode.FACTORY_MODULE_HAS_CHILD);
        }
        // 查询当前模型是否绑定了工位或者房间
        if (Objects.equals(FactoryModuleEnum.STATION.getType(), factoryModule.getType())
        && factoryStationRepository.existStation(id)){
            // 查询当前模型下是否有工位
            throw new BmosException(PlatformResponseCode.FACTORY_MODULE_HAS_STATION);
        }
        if (Objects.equals(FactoryModuleEnum.ROOM.getType(), factoryModule.getType())
        && roomRepository.existRoom(id)){
            // 查询当前模型下是否有房间
            throw new BmosException(PlatformResponseCode.FACTORY_MODULE_HAS_ROOM);
        }

        if (Objects.equals(FactoryModuleEnum.LINE.getType(), factoryModule.getType())
                && lineRepository.existLine(id)){
            // 查询当前模型下是否有房间
            throw new BmosException(PlatformResponseCode.FACTORY_MODULE_HAS_LINE);
        }
    }

    @Override
    public List<StationModuleTreeNodeVO> getModuleTree(FactoryModuleEnum typeEnum) {
        List<FactoryModule> factoryModules = factoryModuleMapper.selectModuleList(typeEnum.getType());
        if (CollUtil.isEmpty(factoryModules)) {
            return Collections.emptyList();
        }
        return TreeUtil.buildTree(FactoryModuleConverter.INSTANCE.convertToTreeListVo(factoryModules), false);
    }

    @Override
    public ModuleVO getModuleTreeInfo(Long id) {
        FactoryModule factoryModule = factoryModuleMapper.selectById(id);
        if (Objects.isNull(factoryModule)){
            throw new BmosException(PlatformResponseCode.FACTORY_MODULE_NOT_EXIST);
        }
        return FactoryModuleConverter.INSTANCE.convertToModuleVo(factoryModule);
    }

    @Override
    public List<ChoiceBoxVO> listProductionLine() {
        return handleBoxVo(factoryModuleMapper.listProductionLine(Integer.valueOf(FactoryModuleEnum.LINE.getName())));
    }

    @Override
    public List<ChoiceBoxVO> listProductionRoom(List<String> lineIdList) {
        return handleBoxVo(factoryModuleMapper.listProductionRoom(Integer.valueOf(FactoryModuleEnum.ROOM.getName()), lineIdList));
    }

    @Override
    public List<EquipmentStation> getStationByProductionId(Long productionLineId) {
        List<FactoryModule> factoryModules = factoryModuleMapper.listProductionRoom(Integer.valueOf(FactoryModuleEnum.ROOM.getName()),
                Collections.singletonList(String.valueOf(productionLineId)));
        return factoryStationRepository.getStationByRoomIds(CollectionUtils.convertList(factoryModules, FactoryModule::getId));
    }

    private List<ChoiceBoxVO> handleBoxVo(List<FactoryModule> factoryModules) {
        if (CollUtil.isEmpty(factoryModules)) {
            return Collections.emptyList();
        }
        return factoryModules.stream().map(item -> {
            ChoiceBoxVO vo = new ChoiceBoxVO();
            vo.setLabel(item.getName());
            vo.setValue(item.getId());
            return vo;
        }).collect(Collectors.toList());
    }
}
