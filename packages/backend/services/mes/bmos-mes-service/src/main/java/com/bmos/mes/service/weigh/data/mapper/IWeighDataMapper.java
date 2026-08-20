package com.bmos.mes.service.weigh.data.mapper;

import com.bmos.mes.service.weigh.data.entity.WeighDataDO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/11/12 17:59
 */
@Mapper
public interface IWeighDataMapper extends BaseMapperX<WeighDataDO> {

    default List<WeighDataDO> selectListByComponentInstanceId(Long componentInstanceId){
        if (componentInstanceId == null){
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapperX<WeighDataDO>()
                .eq(WeighDataDO::getComponentInstanceId, componentInstanceId)
        );
    }
}
