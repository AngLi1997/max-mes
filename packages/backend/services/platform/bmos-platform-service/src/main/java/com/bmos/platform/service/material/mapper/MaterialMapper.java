package com.bmos.platform.service.material.mapper;

import cn.hutool.core.collection.CollUtil;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.common.enums.StatusEnum;
import com.bmos.platform.service.material.dto.MaterialChangeStatusDTO;
import com.bmos.platform.service.material.dto.MaterialPageQueryDTO;
import com.bmos.platform.service.material.dto.MaterialPrincipalQueryDTO;
import com.bmos.platform.service.material.model.Material;
import com.bmos.platform.service.material.vo.IssueTreeNodeVO;
import com.bmos.platform.service.material.vo.MaterialPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface MaterialMapper extends BaseMapperX<Material> {

    List<MaterialPageVO> selectPageList(MaterialPageQueryDTO dto);

    void updateStatus(MaterialChangeStatusDTO dto);

    default boolean existsCode(String mergeCode, Long id) {
        return exists(new LambdaQueryWrapperX<Material>()
                .eq(Material::getMergeCode, mergeCode)
                .neIfPresent(Material::getId, id)
                .last("limit 1"));
    }

    List<Material> selectPrincipalList(MaterialPrincipalQueryDTO dto);

    default List<Material> queryByUnitExtendId(Long id) {
        return selectList(new LambdaQueryWrapperX<Material>()
                .eq(Material::getUnitId, id));
    }

    default boolean existsCategoryMaterial(Long id) {
        return exists(new LambdaQueryWrapperX<Material>()
                .eq(Material::getMaterialCategoryId, id));
    }

    default List<Material> selectByIds(List<Long> materialIds) {
        if (CollUtil.isEmpty(materialIds)){
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapperX<Material>().in(Material::getId, materialIds));
    }

    List<Material> selectEnabledByCategoryId(Long parentId);

    default List<Material> selectEnabledByName(String keyword) {
        return selectList(new LambdaQueryWrapperX<Material>()
                .eq(Material::getStatus, StatusEnum.ON.getValue())
                .like(Material::getName, keyword));
    }

    List<IssueTreeNodeVO> selectIssueTreeNodeVOByKeyword(String keyword);

    void batchChangeDispenseRecord(@Param("ids") List<Long> mIds, @Param("record") String string);

    default boolean existsRelatedMaterial(Long id) {
        return exists(new LambdaQueryWrapperX<Material>().eq(Material::getPrincipalMaterialId, id));
    }
}
