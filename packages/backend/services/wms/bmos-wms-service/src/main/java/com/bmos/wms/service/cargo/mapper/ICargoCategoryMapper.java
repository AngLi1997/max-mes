package com.bmos.wms.service.cargo.mapper;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.wms.service.cargo.model.CargoCategory;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 货品分类信息
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/22 17:02
 */
@Mapper
public interface ICargoCategoryMapper extends BaseMapperX<CargoCategory> {


    /**
     * 根据编码查询
     *
     * @param code 货品分类编码
     * @return 货品分类
     */
    default CargoCategory selectByCode(String code) {
        if (StrUtil.isBlank(code)) {
            return null;
        }
        return selectOne(Wrappers.lambdaQuery(CargoCategory.class)
                .eq(CargoCategory::getCargoCategoryCode, code)
        );
    }

    default List<CargoCategory> selectByParentId(Long id) {
        if (id == null) {
            return new ArrayList<>();
        }
        return selectList(Wrappers.lambdaQuery(CargoCategory.class)
                .eq(CargoCategory::getParentId, id));
    }

    default List<CargoCategory> queryListByParentId(List<Long> parentIds) {
        if (CollectionUtil.isEmpty(parentIds)) {
            return new ArrayList<>();
        }
        return selectList(Wrappers.lambdaQuery(CargoCategory.class)
                .in(CargoCategory::getParentId, parentIds)
        );
    }

    default List<CargoCategory> queryListByIds(Collection<Long> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return selectList(Wrappers.lambdaQuery(CargoCategory.class)
                .in(CargoCategory::getId, ids)
        );
    }
}
