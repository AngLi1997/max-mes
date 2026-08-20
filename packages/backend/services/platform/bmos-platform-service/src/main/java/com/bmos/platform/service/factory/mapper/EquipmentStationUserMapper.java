package com.bmos.platform.service.factory.mapper;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.service.factory.model.EquipmentStationInfo;
import com.bmos.platform.service.factory.model.EquipmentStationUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EquipmentStationUserMapper extends BaseMapperX<EquipmentStationUser> {

    default Boolean saveUserList(List<EquipmentStationUser> stationUserList) {
        return Db.saveOrUpdateBatch(stationUserList);
    }

    List<String> queryStationNameListByStationId(@Param("id") Long id);

    default List<EquipmentStationUser> selectByStationId(Long stationId){
        return selectList(new LambdaQueryWrapperX<EquipmentStationUser>()
                .eq(EquipmentStationUser::getStationId, stationId));
    }

    default Boolean deleteById(List<Long> idList){
        return Db.removeByIds(idList,EquipmentStationUser.class);
    }

    default List<EquipmentStationUser> queryStationUserByStationIdList(List<Long> stationIdList){
        return selectList(new LambdaQueryWrapperX<EquipmentStationUser>()
                .in(EquipmentStationUser::getStationId, stationIdList));
    }

    /**
     * 根据用户id查询用户所拥有的站点
     * @param userId
     * @return
     */
    default List<EquipmentStationUser> getStationByUserId(String userId){
        return selectList(new LambdaQueryWrapperX<EquipmentStationUser>()
                .eq(EquipmentStationUser::getUserId, userId));
    }

    /**
     * 判断当前工位是否绑定人员
     * @param stationId
     * @return
     */
    default boolean existByStationId(Long stationId){
        return exists(new LambdaQueryWrapperX<EquipmentStationUser>()
                .eq(EquipmentStationUser::getStationId, stationId));
    }

    /**
     * 根据用户id删除工位绑定人员
     * @param userId
     */
    default void deleteByUserIdAndStationIdList(String userId, List<Long> allStationIdList){
        delete(new LambdaQueryWrapperX<EquipmentStationUser>()
                .eq(EquipmentStationUser::getUserId, userId)
                .in(EquipmentStationUser::getStationId, allStationIdList));
    }

    default void deleteByStationId(Long stationId){
        delete(new LambdaQueryWrapperX<EquipmentStationUser>()
                .eq(EquipmentStationUser::getStationId, stationId));
    }
}
