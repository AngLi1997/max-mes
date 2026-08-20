package com.bmos.lims2.server.inspect.item.mapper;

import com.bmos.lims2.server.inspect.item.entity.InspectItemParameter;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * 检验项与分析项关联表(BmExperimentInspectAnalyze)表数据库访问层
 *
 * @author makejava
 * @since 2024-03-02 13:22:27
 */
@Mapper
public interface InspectItemParameterMapper extends BaseMapperX<InspectItemParameter> {

    /**
     * 根据检验项id查询当前检验项下的所有分析项关联关系
     * @param inspectId
     * @return
     */
    List<InspectItemParameter> selectByInspectId(Long inspectId);

    /**
     * 验证分析想编码是否存在
     * @param analyzeId
     * @return
     */
    default boolean existByAnalyzeId(Long analyzeId){
        return exists(new LambdaQueryWrapperX<InspectItemParameter>()
                .eq(InspectItemParameter::getInspectParameterId, analyzeId));
    }


    /**
     * 根据检验项id 删除检验项与分析项的关联关系
     * @param inspectId
     */
    default void deleteByInspectId(Long inspectId){
        delete(new LambdaQueryWrapperX<InspectItemParameter>()
                .eq(InspectItemParameter::getInspectItemId, inspectId));
    }

    /**
     * 根据检验id集合查询检验与分析项信息
     * @param inspectIdList
     * @return
     */
    default List<InspectItemParameter> selectByInspectIdList(Collection<Long> inspectIdList){
        return selectList(new LambdaQueryWrapperX<InspectItemParameter>()
                .in(InspectItemParameter::getInspectItemId, inspectIdList));
    }

    /**
     * 根据分析项ID查询已绑定的检验项目名称列表
     * @param parameterId 分析项ID
     * @return 检验项目名称列表
     */
    List<String> selectInspectItemNamesByParameterId(@Param("parameterId") Long parameterId);
}

