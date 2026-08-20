package com.bmos.lims2.server.inspect.parameter.mapper;

import com.bmos.lims2.server.inspect.parameter.dto.InspectMethodEffectiveDTO;
import com.bmos.lims2.server.inspect.parameter.entity.InspectMethod;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 分析项-方法关联记录Mapper
 * @Author: yigaohui
 * @Date: 2025/10/27 00:00
 */
@Mapper
public interface InspectMethodMapper extends BaseMapperX<InspectMethod> {

    default List<InspectMethod> listByParameterId(Long parameterId) {
        return selectList(new LambdaQueryWrapperX<InspectMethod>()
                .eq(InspectMethod::getParameterId, parameterId));
    }

    default long countByParameterAndCodeExcludeId(Long parameterId, Long excludeId) {
        return selectCount(new LambdaQueryWrapperX<InspectMethod>()
                .eq(InspectMethod::getParameterId, parameterId)
                .ne(excludeId != null, InspectMethod::getId, excludeId));
    }

    default long countByParameterAndCode(Long parameterId) {
        return selectCount(new LambdaQueryWrapperX<InspectMethod>()
                .eq(InspectMethod::getParameterId, parameterId));
    }

    default java.util.List<InspectMethod> selectByRecordIdList(java.util.List<Long> recordIds) {
        return selectList(new LambdaQueryWrapperX<InspectMethod>()
                .in(InspectMethod::getRecordId, recordIds));
    }

    default void deleteByRecordId(Long recordId) {
        delete(new LambdaQueryWrapperX<InspectMethod>().eq(InspectMethod::getRecordId, recordId));
    }

    default void deleteByParameterId(Long parameterId) {
        delete(new LambdaQueryWrapperX<InspectMethod>().eq(InspectMethod::getParameterId, parameterId));
    }

    default boolean saveBatch(java.util.List<InspectMethod> list) {
        return Db.saveBatch(list);
    }

    /**
     * 根据分析项ID查询【有生效版本】的方法集合
     * @param parameterId 分析项ID
     * @return 方法与其生效版本信息
     */
    List<InspectMethodEffectiveDTO> selectEffectiveMethodsByParameterId(@Param("parameterId") Long parameterId);

    default void clearByParameterId(Long parameterId){
        delete(new LambdaQueryWrapperX<InspectMethod>().eq(InspectMethod::getParameterId, parameterId));
    }
}


