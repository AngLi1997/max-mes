package com.bmos.wms.service.cargo.mapper;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.wms.service.cargo.dto.CargoPageQuery;
import com.bmos.wms.service.cargo.model.Cargo;
import com.bmos.wms.service.cargo.vo.CargoPageVO;
import com.bmos.wms.service.sendout.vo.SendOrderItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 货品信息
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/22 17:02
 */
@Mapper
public interface ICargoMapper extends BaseMapperX<Cargo> {

    /**
     * 根据货品分类id查询货品信息
     *
     * @param cargoCategoryId 货品分类id
     * @return
     */
    default List<Cargo> selectByCargoCategoryId(Long cargoCategoryId) {
        if (cargoCategoryId == null) {
            return new ArrayList<>();
        }
        return selectList(Wrappers.lambdaQuery(Cargo.class)
                .eq(Cargo::getCargoCategoryId, cargoCategoryId)
        );
    }

    /**
     * 根据子物料id查询货品信息
     *
     * @param id
     * @return
     */
    default List<Cargo> selectBySubMaterialId(Long id) {
        if (id == null) {
            return new ArrayList<>();
        }
        return selectList(Wrappers.lambdaQuery(Cargo.class)
                .eq(Cargo::getSubMaterialId, id)
        );
    }

    List<CargoPageVO> queryPage(@Param("pageQuery") CargoPageQuery pageQuery, @Param("cargoCategoryIds") List<Long> cargoCategoryIds);

    default List<Cargo> selectByPlatformMaterialIds(List<Long> platformIds) {
        if (CollectionUtil.isEmpty(platformIds)) {
            return new ArrayList<>();
        }
        return selectList(Wrappers.lambdaQuery(Cargo.class)
                .in(Cargo::getPlatformMaterialId, platformIds)
        );
    }

    default List<Cargo> selectByCargoCategoryIds(Collection<Long> cargoCategoryIds) {
        if (CollectionUtil.isEmpty(cargoCategoryIds)) {
            return new ArrayList<>();
        }
        return selectList(Wrappers.lambdaQuery(Cargo.class)
                .in(Cargo::getCargoCategoryId, cargoCategoryIds)
        );
    }

    default List<Cargo> selectEnableList() {
        return selectList(Wrappers.lambdaQuery(Cargo.class)
                .eq(Cargo::getEnable, true)
        );
    }

    default List<Cargo> selectNorMemberListByCargoCategoryId(Long categoryId) {
        return selectList(Wrappers.lambdaQuery(Cargo.class)
                .eq(Cargo::getCargoCategoryId, categoryId)
                .eq(Cargo::getIsMember, false)
                .eq(Cargo::getEnable, true)
                .orderByDesc(Cargo::getMergeCode)
        );
    }

    /**
     * 根据货品id查询发货单明细
     *
     * @param cargoIds 货品id列表
     * @return
     */
    List<SendOrderItemVO> selectSendOrderItemWithCargoIds(@Param("cargoIds") List<Long> cargoIds);
}
