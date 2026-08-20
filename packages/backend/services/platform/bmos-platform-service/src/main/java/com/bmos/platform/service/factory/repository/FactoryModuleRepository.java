package com.bmos.platform.service.factory.repository;


import com.bmos.platform.service.factory.model.FactoryModule;

import java.util.List;

/**
 * 模型对内相关接口
 */
public interface FactoryModuleRepository {


    /**
     * 校验模型是否存在
     * @param moduleId
     * @return
     */
    boolean existsById(Long moduleId);

    /**
     * 寻找当前节点以及其所有孩子节点
     * @param moduleId
     * @param type
     * @return
     */
    List<Long> getAllChildModuleId(Long moduleId, Integer type);

    /**
     * 根据模型id查询模型信息
     * @param moduleId
     * @return
     */
    FactoryModule selectById(Long moduleId);

    /**
     * 根据类型获取模型
     *
     * @param type
     * @return
     */
    List<FactoryModule> selectListByType(Integer type);
}
