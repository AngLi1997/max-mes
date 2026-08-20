package com.bmos.lims2.server.inspect.parameter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bmos.lims2.server.inspect.parameter.entity.InspectParameterOption;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分析项数据点选项Mapper接口
 *
 * @author makejava
 * @since 2024-03-02 12:43:37
 */
@Mapper
public interface InspectParameterOptionMapper extends BaseMapperX<InspectParameterOption> {

    /**
     * 根据数据点id查询选项列表
     */
    List<InspectParameterOption> selectByDataPointId(@Param("dataPointId") Long dataPointId);

    /**
     * 根据数据点id删除选项
     */
    int deleteByDataPointId(@Param("dataPointId") Long dataPointId);

    /**
     * 检查选项值是否存在
     */
    boolean existByDataPointIdAndValue(@Param("dataPointId") Long dataPointId, @Param("optionValue") String optionValue);
} 