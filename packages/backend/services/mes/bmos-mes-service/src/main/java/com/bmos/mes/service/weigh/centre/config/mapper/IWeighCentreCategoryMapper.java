package com.bmos.mes.service.weigh.centre.config.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.mes.service.weigh.centre.config.model.WeighCentreCategory;
import com.bmos.mes.service.weigh.centre.config.vo.WeighCentreCategoryPath;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/6/7 10:35
 */
@Mapper
public interface IWeighCentreCategoryMapper extends BaseMapperX<WeighCentreCategory> {

    default List<WeighCentreCategory> listByNameAndParentId(Long parentId, String name) {
        if (StringUtils.isBlank(name)){
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapper<WeighCentreCategory>()
                .isNull(parentId == null, WeighCentreCategory::getParentId)
                .eq(parentId != null, WeighCentreCategory::getParentId, parentId)
                .eq(WeighCentreCategory::getName, name)
        );
    }

    default List<WeighCentreCategory> listByParentId(Long parentId) {
        return selectList(new LambdaQueryWrapper<WeighCentreCategory>()
                .isNull(parentId == null, WeighCentreCategory::getParentId)
                .eq(parentId != null, WeighCentreCategory::getParentId, parentId)
        );
    }

    List<WeighCentreCategory> listAllChildren(@Param("parentId") Long parentId);

    List<WeighCentreCategoryPath> getNamePath(@Param("ids") List<Long> ids);
}
