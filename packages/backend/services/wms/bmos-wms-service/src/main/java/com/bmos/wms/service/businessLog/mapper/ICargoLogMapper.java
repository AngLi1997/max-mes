package com.bmos.wms.service.businessLog.mapper;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.wms.service.businessLog.dto.CargoLogPageQuery;
import com.bmos.wms.service.businessLog.model.CargoLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/7 18:18
 */
@Mapper
public interface ICargoLogMapper extends BaseMapperX<CargoLog> {

    default List<CargoLog> queryPage(CargoLogPageQuery pageQuery) {
        return selectList(Wrappers.lambdaQuery(CargoLog.class)
                .eq(pageQuery.getCargoId() != null, CargoLog::getCargoId, pageQuery.getCargoId())
                .eq(StrUtil.isNotBlank(pageQuery.getInventoryBatchNo()), CargoLog::getInventoryBatchNo, pageQuery.getInventoryBatchNo())
                .eq(StrUtil.isNotBlank(pageQuery.getInventoryNo()), CargoLog::getInventoryNo, pageQuery.getInventoryNo())
                .eq(pageQuery.getOperateType() != null, CargoLog::getOperateType, pageQuery.getOperateType())
                .between(pageQuery.getStartDate() != null && pageQuery.getEndDate() != null, CargoLog::getOperateTime, pageQuery.getStartDate(), pageQuery.getEndDate())
                .orderByDesc(CargoLog::getOperateTime)
        );
    }
}
