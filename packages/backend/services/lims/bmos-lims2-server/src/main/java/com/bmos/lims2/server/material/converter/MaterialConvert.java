package com.bmos.lims2.server.material.converter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.bmos.lims2.feign.material.dto.CategoryIssueFeignDTO;
import com.bmos.lims2.feign.material.dto.MaterialIssueFeignDTO;

import com.bmos.lims2.server.material.dto.*;
import com.bmos.lims2.server.material.entity.Material;
import com.bmos.lims2.server.material.entity.MaterialCategory;
import com.bmos.lims2.server.material.entity.MaterialField;
import com.bmos.lims2.server.inspect.document.entity.DocumentConfig;

import com.bmos.lims2.server.platform.material.dto.*;
import com.bmos.platform.facade.dict.vo.DictDataFeignVO;
import com.bmos.platform.facade.dict.vo.DictDetailFeignVO;
import com.bmos.unit.service.UnitCache;
import com.google.common.collect.Lists;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface MaterialConvert {

    MaterialConvert INSTANCE = Mappers.getMapper(MaterialConvert.class);

    List<CategoryIssueDTO> convert2CategoryDTO(List<CategoryIssueFeignDTO> categoryList);

    default List<MaterialCategoryTreeNodeDTO> convert2TreeNodeDTO(List<SyncTreeNodeDTO> syncTreeNodeDTOList, Long parantId) {
        List<MaterialCategoryTreeNodeDTO> inspectionTreeNodeVOS = new ArrayList<>();
        if (CollectionUtil.isEmpty(syncTreeNodeDTOList)) {
            return inspectionTreeNodeVOS;
        }
        for (SyncTreeNodeDTO syncTreeNodeDTO : syncTreeNodeDTOList) {
            MaterialCategoryTreeNodeDTO inspectionTreeNodeVO = convert2TreeNodDTO(syncTreeNodeDTO);
            inspectionTreeNodeVO.setParentId(parantId);
            if (CollUtil.isNotEmpty(syncTreeNodeDTO.getChildren())) {
                inspectionTreeNodeVO.setChildren(convert2TreeNodeDTO(syncTreeNodeDTO.getChildren(), syncTreeNodeDTO.getId()));
            }
            inspectionTreeNodeVOS.add(inspectionTreeNodeVO);
        }
        return inspectionTreeNodeVOS;
    }

    default List<MaterialCategoryTreeNodeDTO> convert2TreeNodDTO(List<SyncTreeNodeDTO> syncTreeNodeDTOList, Long parantId) {
        List<MaterialCategoryTreeNodeDTO> inspectionTreeNodeVOS = new ArrayList<>();
        if (CollectionUtil.isEmpty(syncTreeNodeDTOList)) {
            return inspectionTreeNodeVOS;
        }
        for (SyncTreeNodeDTO syncTreeNodeDTO : syncTreeNodeDTOList) {
            MaterialCategoryTreeNodeDTO inspectionTreeNodeVO = convert2TreeNodDTO(syncTreeNodeDTO);
            inspectionTreeNodeVO.setParentId(parantId);
            if (CollUtil.isNotEmpty(syncTreeNodeDTO.getChildren())) {
                inspectionTreeNodeVO.setChildren(convert2TreeNodDTO(syncTreeNodeDTO.getChildren(), syncTreeNodeDTO.getId()));
            }
            inspectionTreeNodeVOS.add(inspectionTreeNodeVO);
        }
        return inspectionTreeNodeVOS;
    }

    MaterialCategoryTreeNodeDTO convert2TreeNodDTO(SyncTreeNodeDTO syncTreeNodeDTOList);

    SyncMaterialInfoDTO convert2MaterialDTO(MaterialSyncDTO reqVO);

    List<MaterialCategoryTreeNodeDTO> convertDO2TreeNodeVO(List<MaterialCategory> basicCategoryList);

    MaterialParamDTO convert2ProductsParam(MaterialPageQueryDTO reqVO);

    MaterialCategory convert2ProductsDO(MaterialCategoryCreateDTO reqVO);

    MaterialCategory convert2CategoryDO(CategoryIssueDTO categoryIssueDTO);

    MaterialCategoryParamDTO convert2CategoryParam(MaterialCategoryQueryDTO reqVO);

    MaterialWithFieldDTO convert2ProductsInfoVO(Material basicProductsDO);

    List<MaterialDTO> convert2ProductsEasyVO(List<Material> basicProducts);


    List<MaterialIssueDTO> convert2MaterialIssueDTO(List<MaterialIssueFeignDTO> materialList);


    ProductMaterialSaveDTO convertToRemoteSaveDTO(MaterialSaveDTO dto);

    Material convert2InspectDO(MaterialSaveDTO dto);

    default List<MaterialField> convert2ProductsField(List<MaterialFieldSaveDTO> fieldSaveDTOList, Long id) {
        if (CollUtil.isEmpty(fieldSaveDTOList)) {
            return Lists.newArrayList();
        }
        List<MaterialField> result = Lists.newArrayList();
        for (MaterialFieldSaveDTO fieldSaveDTO : fieldSaveDTOList) {
            MaterialField field = new MaterialField();
            field.setMaterialId(id);
            field.setField(fieldSaveDTO.getField());
            field.setFieldName(fieldSaveDTO.getFieldName());
            field.setFieldType(fieldSaveDTO.getFieldType());
            field.setFieldTypeName(fieldSaveDTO.getFieldTypeName());
            field.setFieldValue(fieldSaveDTO.getFieldValue());
            result.add(field);
        }
        return result;
    }

    List<MaterialFieldDTO> convert2ProductsFieldVO(List<MaterialField> basicProductsFields);


    default List<MaterialFieldTypeDTO> convert2MaterialFieldTypeVO(List<DictDetailFeignVO> data){
        List<MaterialFieldTypeDTO> result = Lists.newArrayList();
        if (CollUtil.isEmpty(data)){
            return result;
        }
        for (DictDetailFeignVO dictDetailFeignVO : data) {
            MaterialFieldTypeDTO vo = new MaterialFieldTypeDTO();
            vo.setFieldType(dictDetailFeignVO.getDictCode());
            vo.setFieldTypeName(dictDetailFeignVO.getDictName());
            List<MaterialFieldDTO> fieldList = new ArrayList<>();
            vo.setFieldList(fieldList);
            if (CollUtil.isEmpty(dictDetailFeignVO.getDictDataList())){
                continue;
            }
            for (DictDataFeignVO dictDataFeignVO : dictDetailFeignVO.getDictDataList()) {
                MaterialFieldDTO fieldVO = new MaterialFieldDTO();
                fieldVO.setField(dictDataFeignVO.getDictValue());
                fieldVO.setFieldName(dictDataFeignVO.getDictLabel());
                fieldList.add(fieldVO);
            }
            result.add(vo);
        }
        return result;
    }

    List<MaterialFieldInfoDTO> convertMaterialFieldInfoDTOList(List<MaterialField> fieldList);

    /**
     * 将MaterialCategory转换为MaterialInspectTreeNodeDTO（检品分类节点）
     */
    default MaterialInspectTreeNodeDTO convertCategoryToTreeNode(MaterialCategory category) {
        MaterialInspectTreeNodeDTO node = new MaterialInspectTreeNodeDTO();
        node.setId(category.getId());
        node.setParentId(category.getParentId());
        node.setName(category.getName());
        node.setCode(category.getCode());
        node.setMergeCode(category.getMergeCode());
        node.setShowName(category.getMergeCode() + "-" + category.getName());
        node.setNodeType(MaterialInspectTreeNodeDTO.NodeType.CATEGORY);
        node.setCategoryFlag(true);
        node.setCategoryType(category.getCategoryType());
        node.setPlatformCategoryId(category.getPlatformCategoryId());
        node.setCreateTime(category.getCreateTime());
        return node;
    }

    /**
     * 将检品分类列表转换为树节点列表
     */
    default List<MaterialInspectTreeNodeDTO> convertCategoriesToTreeNodes(List<MaterialCategory> categories) {
        List<MaterialInspectTreeNodeDTO> nodes = new ArrayList<>();
        if (CollUtil.isEmpty(categories)) {
            return nodes;
        }
        for (MaterialCategory category : categories) {
            nodes.add(convertCategoryToTreeNode(category));
        }
        return nodes;
    }

    /**
     * 将Material转换为MaterialInspectTreeNodeDTO（检品节点）
     */
    default MaterialInspectTreeNodeDTO convertMaterialToTreeNode(Material material, UnitCache unitCache) {
        MaterialInspectTreeNodeDTO node = new MaterialInspectTreeNodeDTO();
        node.setId(material.getId());
        node.setParentId(material.getCategoryId()); // Material的categoryId作为parentId
        node.setName(material.getName());
        node.setCode(material.getCode());
        node.setSpecification(material.getSpecification());
        node.setMergeCode(material.getCode());
        node.setShowName(material.getCode() + "-" + material.getName());
        node.setNodeType(MaterialInspectTreeNodeDTO.NodeType.MATERIAL);
        node.setCategoryFlag(false);
        node.setRemark(material.getRemark());
        node.setCreateTime(material.getCreateTime());
        node.setUnitId(material.getUnitId());
        node.setUnitName(unitCache.getGlobalUnitName(material.getUnitId()));
        return node;
    }

    /**
     * 将检品列表转换为树节点列表
     */
    default List<MaterialInspectTreeNodeDTO> convertMaterialsToTreeNodes(List<Material> materials, UnitCache unitCache) {
        List<MaterialInspectTreeNodeDTO> nodes = new ArrayList<>();
        if (CollUtil.isEmpty(materials)) {
            return nodes;
        }
        for (Material material : materials) {
            nodes.add(convertMaterialToTreeNode(material,unitCache));
        }
        return nodes;
    }

    /**
     * 将MaterialCategory转换为MaterialInspectTreeWithDocumentDTO（检品分类节点）
     */
    default MaterialInspectTreeWithDocumentDTO convertCategoryToTreeWithDocumentNode(MaterialCategory category) {
        MaterialInspectTreeWithDocumentDTO node = new MaterialInspectTreeWithDocumentDTO();
        node.setId(category.getId());
        node.setParentId(category.getParentId());
        node.setName(category.getName());
        node.setCode(category.getCode());
        node.setMergeCode(category.getMergeCode());
        node.setShowName(category.getMergeCode() + "-" + category.getName());
        node.setNodeType(MaterialInspectTreeWithDocumentDTO.NodeType.CATEGORY);
        node.setCategoryFlag(true);
        node.setCategoryType(category.getCategoryType());
        node.setPlatformCategoryId(category.getPlatformCategoryId());
        node.setCreateTime(category.getCreateTime());
        return node;
    }

    /**
     * 将检品分类列表转换为带请验单信息的树节点列表
     */
    default List<MaterialInspectTreeWithDocumentDTO> convertCategoriesToTreeWithDocumentNodes(List<MaterialCategory> categories) {
        List<MaterialInspectTreeWithDocumentDTO> nodes = new ArrayList<>();
        if (CollUtil.isEmpty(categories)) {
            return nodes;
        }
        for (MaterialCategory category : categories) {
            nodes.add(convertCategoryToTreeWithDocumentNode(category));
        }
        return nodes;
    }

    /**
     * 将Material转换为MaterialInspectTreeWithDocumentDTO（检品节点）
     */
    default MaterialInspectTreeWithDocumentDTO convertMaterialToTreeWithDocumentNode(Material material, UnitCache unitCache, DocumentConfig documentConfig) {
        MaterialInspectTreeWithDocumentDTO node = new MaterialInspectTreeWithDocumentDTO();
        node.setId(material.getId());
        node.setParentId(material.getCategoryId()); // Material的categoryId作为parentId
        node.setName(material.getName());
        node.setCode(material.getCode());
        node.setSpecification(material.getSpecification());
        node.setMergeCode(material.getCode());
        node.setShowName(material.getCode() + "-" + material.getName());
        node.setNodeType(MaterialInspectTreeWithDocumentDTO.NodeType.MATERIAL);
        node.setCategoryFlag(false);
        node.setRemark(material.getRemark());
        node.setCreateTime(material.getCreateTime());
        node.setUnitId(material.getUnitId());
        node.setUnitName(unitCache.getGlobalUnitName(material.getUnitId()));
        
        // 设置请验单信息
        if (documentConfig != null) {
            node.setDocumentConfigId(documentConfig.getId());
            node.setDocumentConfigName(documentConfig.getName());
        }
        
        return node;
    }

    Material convertToProductMaterial(MaterialSaveDTO dto);

    MaterialCategory convertMaterialCategory(MaterialCategoryCreateDTO dto);
}
