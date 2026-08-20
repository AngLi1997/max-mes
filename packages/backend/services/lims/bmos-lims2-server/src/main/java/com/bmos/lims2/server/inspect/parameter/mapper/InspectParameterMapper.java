package com.bmos.lims2.server.inspect.parameter.mapper;

import com.bmos.lims2.server.inspect.parameter.dto.ParameterParamDTO;
import com.bmos.lims2.server.inspect.parameter.entity.InspectParameter;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分析项(BmExperimentAnalyze)表数据库访问层
 *
 * @author makejava
 * @since 2024-03-02 12:52:07
 */
@Mapper
public interface InspectParameterMapper extends BaseMapperX<InspectParameter> {

    /**
     * 验证分析想编码是否存在
     *
     * @param id
     * @return
     */
    default boolean existById(Long id) {
        return exists(new LambdaQueryWrapperX<InspectParameter>()
                .eq(InspectParameter::getId, id));
    }

    /**
     * 验证分析想编码是否存在
     *
     * @param code
     * @return
     */
    default boolean existByCode(String code) {
        return exists(new LambdaQueryWrapperX<InspectParameter>()
                .eq(InspectParameter::getCode, code));
    }

    /**
     * 根据参数查询分析项
     *
     * @param param
     * @return
     */
    List<InspectParameter> selectParam(@Param("param") ParameterParamDTO param);
}

