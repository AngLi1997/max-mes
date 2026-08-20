package com.bmos.platform.service.factory.mapper;

import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.platform.service.factory.model.FactoryRoomEnvProperty;
import com.bmos.platform.service.factory.service.dto.RoomEnvPropertyWithAcquitPointDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * (BpFactoryRoomEnvProperty)表数据库访问层
 *
 * @author makejava
 * @since 2024-12-30 10:04:51
 */
@Mapper
public interface BpFactoryRoomEnvPropertyMapper extends BaseMapperX<FactoryRoomEnvProperty> {
    /**
     * 通过房间id获取配置集合
     *
     * @param roomIds 房间id
     * @return 查询结果
     */
    List<RoomEnvPropertyWithAcquitPointDTO> selectByRoomIds(@Param("roomIds") Collection<Long> roomIds);
}

