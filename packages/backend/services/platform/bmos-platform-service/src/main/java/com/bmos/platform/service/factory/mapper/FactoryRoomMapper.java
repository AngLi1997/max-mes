package com.bmos.platform.service.factory.mapper;

import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.facade.factory.vo.FactoryRoomFeignVO;
import com.bmos.platform.service.factory.mapper.param.RoomParam;
import com.bmos.platform.service.factory.model.FactoryLineRoom;
import com.bmos.platform.service.factory.model.FactoryLineStation;
import com.bmos.platform.service.factory.model.FactoryRoom;
import com.bmos.platform.service.factory.model.FactoryRoomStation;
import com.bmos.platform.service.factory.service.dto.FactoryRoomDTO;
import com.bmos.platform.service.factory.service.dto.RoomListQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * 房间(BpFactoryRoom)表数据库访问层
 *
 * @author makejava
 * @since 2024-05-21 10:15:53
 */
@Mapper
public interface FactoryRoomMapper extends BaseMapperX<FactoryRoom> {

    /**
     * 判断当前模型下是否存在房间信息
     *
     * @param moduleId
     * @return
     */
    default boolean existsByModuleId(Long moduleId) {
        return exists(new LambdaQueryWrapperX<FactoryRoom>()
                .eq(FactoryRoom::getModuleId, moduleId));
    }

    /**
     * 校验房间编码是否存在
     *
     * @param code
     * @return
     */
    default boolean existsByCode(String code) {
        return exists(new LambdaQueryWrapperX<FactoryRoom>()
                .eq(FactoryRoom::getCode, code));
    }

    /**
     * 根据参数查询房间
     *
     * @param roomParam
     * @return
     */
    List<FactoryRoom> selectByParam(@Param("param") RoomParam roomParam);

    /**
     * 查询模型下房间信息
     *
     * @param moduleIdList
     * @return
     */
    default List<FactoryRoom> selectByModuleIdList(List<Long> moduleIdList) {
        return selectList(new LambdaQueryWrapperX<FactoryRoom>()
                .in(FactoryRoom::getModuleId, moduleIdList));
    }

    /**
     * 根据房间状态查询房间信息
     *
     * @param code
     * @return
     */
    default List<FactoryRoom> selectByStatus(Integer code) {
        return selectList(new LambdaQueryWrapperX<FactoryRoom>()
                .eq(FactoryRoom::getStatus, code));
    }


    /**
     * 根据房间编码查询房间信息
     *
     * @param code
     * @return
     */
    default FactoryRoom selectByCode(String code) {
        return selectOne(new LambdaQueryWrapperX<FactoryRoom>()
                .eq(FactoryRoom::getCode, code)
                .last("limit 1"));
    }

    List<FactoryRoomFeignVO> queryRoomListByRoomIds(@Param("roomIds") List<Long> roomIds);

    /**
     * 查询列表
     * @param roomListQueryDTO
     * @return
     */
    List<FactoryRoomDTO> list(@Param("roomListQueryDTO") RoomListQueryDTO roomListQueryDTO);
}

