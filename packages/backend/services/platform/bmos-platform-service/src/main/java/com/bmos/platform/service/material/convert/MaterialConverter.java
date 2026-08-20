package com.bmos.platform.service.material.convert;

import cn.hutool.core.collection.CollUtil;
import com.bmos.platform.facade.material.dto.MaterialTreeNodeVO;
import com.bmos.platform.service.material.dto.MaterialCategorySaveDTO;
import com.bmos.platform.service.material.dto.MaterialCategoryUpdateDTO;
import com.bmos.platform.service.material.dto.MaterialSaveDTO;
import com.bmos.platform.service.material.dto.MaterialUpdateDTO;
import com.bmos.platform.service.material.model.Material;
import com.bmos.platform.service.material.model.MaterialCategory;
import com.bmos.platform.service.material.vo.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface MaterialConverter {
    MaterialConverter INSTANCE = Mappers.getMapper(MaterialConverter.class);

    MaterialCategory convertCategory(MaterialCategorySaveDTO dto);

    List<MaterialCategoryTreeNodeVO> convertCategoryTreeNode(List<MaterialCategory> categories);

    Material convert(MaterialSaveDTO dto);

    Material convertUpdate(MaterialUpdateDTO dto);

    List<MaterialVO> convertPrincipalList(List<Material> materials);

    MaterialDetailVO convertDetail(Material material);

    List<IssueTreeNodeVO> convertCategoryIssueTreeNode(List<MaterialCategory> categories);
    List<IssueTreeNodeVO> convertMaterialIssueTreeNode(List<Material> categories);

    MaterialCategory convertCategory(MaterialCategoryUpdateDTO dto);

    default List<IssueMaterialVO> convert2IssueVOList(List<Material> materials, Integer dyingPeriod){
        List<IssueMaterialVO> issueMaterialVOS = new ArrayList<>();
        if (CollUtil.isEmpty(materials)){
            return issueMaterialVOS;
        }
        for (Material material : materials) {
            IssueMaterialVO issueMaterialVO = convert2IssueVO(material);
            issueMaterialVO.setDyingPeriod(dyingPeriod);
            issueMaterialVOS.add(issueMaterialVO);
        }
        return issueMaterialVOS;
    }

    IssueMaterialVO convert2IssueVO(Material material);

    MaterialTreeNodeVO convert2MaterialTreeNode(MaterialCategory e);

    MaterialTreeNodeVO convert2MaterialTreeNode(Material e);

    Material convertMaterial(MaterialTemplateVO vo);
}
