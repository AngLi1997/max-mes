package com.bmos.mes.service.storage.config.mapper;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmos.mes.common.enums.BooleanEnum;
import com.bmos.mes.service.storage.config.dto.CargoPositionPageQuery;
import com.bmos.mes.service.storage.config.model.CargoPosition;
import com.bmos.mes.service.storage.config.vo.CargoPathVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/2/18 14:12
 */
@Mapper
public interface ICargoPositionMapper extends BaseMapper<CargoPosition> {

    /**
     * 根据暂存间查询货位信息
     *
     * @param storageId 暂存间id
     * @return
     */
    default List<CargoPosition> queryEnabledListByStorageId(Long storageId) {
        if (storageId == null) {
            return new ArrayList<>();
        }
        return selectList(Wrappers.lambdaQuery(CargoPosition.class)
                .eq(CargoPosition::getStorageId, storageId)
                .eq(CargoPosition::getEnable, BooleanEnum.TRUE.getValue())
        );
    }

    /**
     * 根据暂存间查询货位信息
     *
     * @param storageId 暂存间id
     * @return
     */
    default List<CargoPosition> queryListByStorageId(Long storageId) {
        if (storageId == null) {
            return new ArrayList<>();
        }
        return selectList(Wrappers.lambdaQuery(CargoPosition.class)
                .eq(CargoPosition::getStorageId, storageId)
        );
    }

    /**
     * 校验同一个暂存间下暂存货位是否存在
     *
     * @param storageId 暂存间id
     * @param position  暂存货位
     * @return true 存在 false 不存在
     */
    default boolean existStorageIdAndPosition(Long storageId, String position) {
        return exists(Wrappers.lambdaQuery(CargoPosition.class)
                .eq(CargoPosition::getStorageId, storageId)
                .eq(CargoPosition::getPosition, position)
        );
    }

    /**
     * 校验全局暂存货位编码是否存在
     *
     * @param code 暂存货位编码
     * @return true 存在 false 不存在
     */
    default boolean existCode(String code) {
        return exists(Wrappers.lambdaQuery(CargoPosition.class)
                .eq(CargoPosition::getCode, code)
        );
    }

    List<CargoPosition> queryList(@Param("pageQuery") CargoPositionPageQuery pageQuery,
                                  @Param("deptIds") List<Long> deptIds, @Param("positionIds") List<Long> positionIds);

//    default List<CargoPosition> queryEnabledListByStorageIds(List<Long> storagesIds, List<Long> deptIds) {
//        return selectList(Wrappers.lambdaQuery(CargoPosition.class)
//                .in(CargoPosition::getStorageId, storagesIds)
//                .eq(CargoPosition::getEnable, BooleanEnum.TRUE.getValue())
//        );
//    }

    List<CargoPosition> queryEnabledListByStorageIdsWithPermission(@Param("storagesIds") List<Long> storagesIds,
                                                                   @Param("deptIds") List<Long> deptIds);

    default CargoPosition selectEnabledByCode(String code) {
        return selectOne(Wrappers.lambdaQuery(CargoPosition.class)
                .eq(CargoPosition::getCode, code)
                .eq(CargoPosition::getEnable, BooleanEnum.TRUE.getValue())
        );
    }

    /**
     * 根据货位id查询货位路径
     *
     * @param id
     * @return
     */
    String getCargoPositionPath(@Param("id") Long id, @Param("sp") String sp);


    /**
     * 根据货位id列表查询多个货位路径
     */
    List<CargoPathVO> getCargoPositionPathMap(@Param("idList")List<Long> idList, @Param("sp") String sp);

    default CargoPosition selectByCode(String no){
        if (StrUtil.isBlank(no)){
            return null;
        }
        return selectOne(Wrappers.lambdaQuery(CargoPosition.class)
                .eq(CargoPosition::getCode, no)
        );
    }

    default List<CargoPosition> queryEnabledList(){
        return selectList(Wrappers.lambdaQuery(CargoPosition.class)
                .eq(CargoPosition::getEnable, BooleanEnum.TRUE.getValue())
        );
    }
}
