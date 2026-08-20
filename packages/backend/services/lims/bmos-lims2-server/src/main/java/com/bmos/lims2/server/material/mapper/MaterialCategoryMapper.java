package com.bmos.lims2.server.material.mapper;

import com.bmos.lims2.server.material.dto.MaterialCategoryParamDTO;
import com.bmos.lims2.server.material.entity.MaterialCategory;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 检品分类mapper
 */
@Mapper
public interface MaterialCategoryMapper extends BaseMapperX<MaterialCategory> {

    /**
     * 根据参数查询检品分类信息
     * @param param
     * @return
     */
    List<MaterialCategory> selectByParam(@Param("param") MaterialCategoryParamDTO param);

    /**
     * 校验当前id下是否有子分类
     * @param id
     * @return
     */
    default boolean existsChild(Long id) {
        return exists(new LambdaQueryWrapperX<MaterialCategory>()
                .eq(MaterialCategory::getParentId, id));
    }

    List<MaterialCategory> selectChildCategoryId(@Param("parentId") Long parentId);
}
