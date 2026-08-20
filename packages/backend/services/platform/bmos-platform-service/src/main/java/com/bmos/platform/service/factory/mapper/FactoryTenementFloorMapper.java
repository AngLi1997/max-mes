package com.bmos.platform.service.factory.mapper;

import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.platform.service.factory.model.FactoryTenementFloor;
import org.apache.ibatis.annotations.Mapper;

/**
 * 楼宇楼层表(BpTenementFloor)表数据库访问层
 *
 * @author makejava
 * @since 2024-12-30 14:09:26
 */
@Mapper
public interface FactoryTenementFloorMapper extends BaseMapperX<FactoryTenementFloor> {

}

