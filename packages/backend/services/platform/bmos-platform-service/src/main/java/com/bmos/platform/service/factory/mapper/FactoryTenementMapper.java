package com.bmos.platform.service.factory.mapper;

import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.platform.service.factory.model.FactoryTenement;
import org.apache.ibatis.annotations.Mapper;

/**
 * 楼宇(BpFactoryTenement)表数据库访问层
 *
 * @author makejava
 * @since 2024-12-30 11:54:55
 */
@Mapper
public interface FactoryTenementMapper extends BaseMapperX<FactoryTenement> {

}

