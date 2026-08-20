package com.bmos.platform.service.factory.repository.impl;

import cn.hutool.core.collection.CollUtil;
import com.bmos.platform.service.factory.mapper.FactoryModuleMapper;
import com.bmos.platform.service.factory.model.FactoryModule;
import com.bmos.platform.service.factory.repository.FactoryModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class FactoryModuleRepositoryImpl implements FactoryModuleRepository {

    @Autowired
    FactoryModuleMapper factoryModuleMapper;


    @Override
    public boolean existsById(Long moduleId) {
        return factoryModuleMapper.existsById(moduleId);
    }

    @Override
    public List<Long> getAllChildModuleId(Long moduleId, Integer type) {
        List<FactoryModule> factoryModules = factoryModuleMapper.selectModuleList(type);
        List<Long> moduleIdList = new ArrayList<>();
        moduleIdList.add(moduleId);
        // 在factoryModules中寻找其祖先节点为moduleId的对象
        helpGetAllChildModuleId(moduleId, factoryModules, moduleIdList);
        return moduleIdList;
    }

    @Override
    public FactoryModule selectById(Long moduleId) {
        return factoryModuleMapper.selectById(moduleId);
    }

    @Override
    public List<FactoryModule> selectListByType(Integer type) {
        return factoryModuleMapper.selectModuleList(type);
    }

    private void helpGetAllChildModuleId(Long moduleId, List<FactoryModule> factoryModules, List<Long> moduleIdList){
        // 寻找孩子节点
        List<FactoryModule> childList = factoryModules.stream().filter(item -> item.getParentId().equals(moduleId)).collect(Collectors.toList());
        if (CollUtil.isEmpty(childList)) {
            return ;
        }
        factoryModules.removeAll(childList);
        childList.forEach(child -> {
            moduleIdList.add(child.getId());
            helpGetAllChildModuleId(child.getId(), factoryModules, moduleIdList);
        });
    }
}
