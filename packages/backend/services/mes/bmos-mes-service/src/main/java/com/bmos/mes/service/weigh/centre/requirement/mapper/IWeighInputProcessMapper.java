package com.bmos.mes.service.weigh.centre.requirement.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.mes.service.weigh.centre.requirement.model.WeighInputProcess;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/8/2 16:20
 */
@Mapper
public interface IWeighInputProcessMapper extends BaseMapperX<WeighInputProcess> {

    default WeighInputProcess getWeighProcessByComponentInstanceId(@Param("componentInstanceId") Long componentInstanceId) {
        if (componentInstanceId == null) {
            return null;
        }
        return selectOne(new LambdaQueryWrapper<WeighInputProcess>()
                .eq(WeighInputProcess::getComponentInstanceId, componentInstanceId)
        );
    }
}
