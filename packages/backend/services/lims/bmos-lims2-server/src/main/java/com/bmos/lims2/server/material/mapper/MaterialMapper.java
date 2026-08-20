package com.bmos.lims2.server.material.mapper;

import com.bmos.lims2.server.material.dto.MaterialDTO;
import com.bmos.lims2.server.material.dto.MaterialParamDTO;
import com.bmos.lims2.server.material.entity.Material;
import com.bmos.lims2.server.platform.material.dto.MaterialPrincipalQueryDTO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 检品信息mapper
 */
@Mapper
public interface MaterialMapper extends BaseMapperX<Material> {

    /**
     * 根据参数查询全量产品信息
     * @param productsParam
     * @return
     */
    List<Material> selectByParam(@Param("param") MaterialParamDTO productsParam);

    /**
     * 当前分类下是否存在检品信息
     * @param categoryId
     * @return
     */
    default boolean existsProducts(Long categoryId){
        return exists(new LambdaQueryWrapperX<Material>()
                .eq(Material::getCategoryId, categoryId));
    }

    /**
     * 校验当前检品是否存在
     * @param id
     * @return
     */
    default boolean existById(Long id){
        return exists(new LambdaQueryWrapperX<Material>()
                .eq(Material::getId, id));
    }

    /**
     * 根据平台物料id查询检品（平台物料id为全系统统一标识）
     * @param platformMaterialId 平台物料id
     * @return 检品，不存在返回 null
     */
    default Material selectByPlatformMaterialId(Long platformMaterialId){
        if (platformMaterialId == null) {
            return null;
        }
        return selectOne(new LambdaQueryWrapperX<Material>()
                .eq(Material::getPlatformMaterialId, platformMaterialId));
    }

    /**
     * 判断是否存在启用的成员物料引用指定所属物料ID
     * @param principalId 所属物料ID
     * @return 存在返回 true
     */
    default boolean existsEnabledSubMaterialByPrincipalId(Long principalId) {
        return exists(new LambdaQueryWrapperX<Material>()
                .eq(Material::getPrincipalMaterialId, principalId));
    }

    /**
     * 分页连表查询检品及分类名称
     * @param productsParam
     * @return
     */
    List<MaterialDTO> selectPageWithCategory(@Param("param") MaterialParamDTO productsParam);

    default List<Material> selectByCategoryIds(List<Long> categoryIdList) {
        return selectList(new LambdaQueryWrapperX<Material>()
                .in(Material::getCategoryId, categoryIdList));
    }

    List<MaterialDTO> selectPrincipalList(MaterialPrincipalQueryDTO dto);

    /**
     * 校验合并编码是否已存在
     * @param mergeCode 合并编码
     * @return 存在返回 true
     */
    default boolean existByMergeCode(String mergeCode) {
        return exists(new LambdaQueryWrapperX<Material>()
                .eq(Material::getMergeCode, mergeCode));
    }
}
