package com.bmos.mes.service.dataset.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.mes.service.dataset.model.DatasetCategory;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/8/23 11:50
 */
@Mapper
public interface IDatasetCategoryMapper extends BaseMapperX<DatasetCategory> {

    default List<DatasetCategory> listByNameAndParentId(Long parentId, String name){
        if (StringUtils.isBlank(name)){
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapper<DatasetCategory>()
                .isNull(parentId == null, DatasetCategory::getParentId)
                .eq(parentId != null, DatasetCategory::getParentId, parentId)
                .eq(DatasetCategory::getName, name)
        );
    }

    default List<DatasetCategory> listByParentId(Long parentId) {
        return selectList(new LambdaQueryWrapper<DatasetCategory>()
                .isNull(parentId == null, DatasetCategory::getParentId)
                .eq(parentId != null, DatasetCategory::getParentId, parentId)
        );
    }

    List<DatasetCategory> listAllChildren(@Param("parentId") Long parentId);
}
