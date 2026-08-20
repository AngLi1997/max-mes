package com.bmos.wms.service.position.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmos.wms.service.position.dto.CargoPositionPageQuery;
import com.bmos.wms.service.position.model.CargoPosition;
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
     * 根据存储区域查询货位信息
     *
     * @param storageId 存储区域id
     * @return
     */
    default List<CargoPosition> queryEnabledListByStorageId(Long storageId) {
        if (storageId == null) {
            return new ArrayList<>();
        }
        return selectList(Wrappers.lambdaQuery(CargoPosition.class)
                .eq(CargoPosition::getStorageId, storageId)
                .eq(CargoPosition::getEnable, true)
        );
    }

    /**
     * 根据存储区域查询货位信息
     *
     * @param storageId 存储区域id
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
     * 校验同一个存储区域下暂存货位是否存在
     *
     * @param storageId 存储区域id
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

    List<CargoPosition> queryList(@Param("pageQuery") CargoPositionPageQuery pageQuery, @Param("deptIds") List<Long> deptIds, @Param("positionIds") List<Long> positionIds);


    List<CargoPosition> queryEnabledListByStorageIdsWithPermission(@Param("storagesIds") List<Long> storagesIds,
                                                                   @Param("deptIds") List<Long> deptIds);

    default CargoPosition selectEnabledByCode(String code) {
        return selectOne(Wrappers.lambdaQuery(CargoPosition.class)
                .eq(CargoPosition::getCode, code)
                .eq(CargoPosition::getEnable, true)
        );
    }

    String getCargoPositionPath(@Param("id") Long id);
}
