package com.bmos.platform.service.factory.mapper;

import cn.hutool.core.collection.CollectionUtil;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.service.factory.model.FactoryModule;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mapper
public interface FactoryModuleMapper extends BaseMapperX<FactoryModule> {

    /**
     * 获取某个类型下的所有模型树
     * @param type
     * @return
     */
    default List<FactoryModule> selectModuleList(Integer type) {
        return selectList(new LambdaQueryWrapperX<FactoryModule>()
                .eq(FactoryModule::getType, type));
    }

    default List<FactoryModule> listProductionLine(Integer type) {
        return selectList(new LambdaQueryWrapperX<FactoryModule>()
                .eq(FactoryModule::getType, type));
    }

    default List<FactoryModule> listProductionRoom(Integer type, List<String> lineIdList){
        return selectList(new LambdaQueryWrapperX<FactoryModule>()
                .eq(FactoryModule::getType, type)
                .in(FactoryModule::getParentId,lineIdList));
    }

    default List<FactoryModule> queryListByParentIds(Collection<Long> parentIds){
        if(CollectionUtil.isEmpty(parentIds)){
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapperX<FactoryModule>()
                .in(FactoryModule::getParentId,parentIds));
    }

    /**
     * 校验编码是否存在
     * @param code
     * @param type
     * @return
     */
    default boolean existsByCode(String code, Integer type){
        return exists(new LambdaQueryWrapperX<FactoryModule>()
                .eq(FactoryModule::getCode, code)
                .eq(FactoryModule::getType, type));
    }

    /**
     * 判断当前节点是否有子节点
     * @param id
     * @return
     */
    default Boolean existChild(Long id){
        return exists(new LambdaQueryWrapperX<FactoryModule>()
                .eq(FactoryModule::getParentId, id));
    }

    /**
     * 校验id是否存在
     * @param moduleId
     * @return
     */
    default boolean existsById(Long moduleId){
        return exists(new LambdaQueryWrapperX<FactoryModule>()
                .eq(FactoryModule::getId, moduleId));
    }
}
