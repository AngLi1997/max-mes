package com.bmos.platform.service.equipment.mapper;

import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.platform.service.equipment.model.AcquisitionPoint;
import org.apache.ibatis.annotations.Mapper;

/**
 * 采集点数据表(AcquisitionPoint)表数据库访问层
 *
 * @author makejava
 * @since 2024-04-19 15:17:52
 */
@Mapper
public interface AcquisitionPointMapper extends BaseMapperX<AcquisitionPoint> {

}

