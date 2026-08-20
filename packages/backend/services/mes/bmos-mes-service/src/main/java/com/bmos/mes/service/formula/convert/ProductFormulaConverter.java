package com.bmos.mes.service.formula.convert;

import com.bmos.mes.service.formula.dto.ProductFormulaMaterialDTO;
import com.bmos.mes.service.formula.dto.ProductFormulaSaveDTO;
import com.bmos.mes.service.formula.dto.ProductFormulaSaveVersionDTO;
import com.bmos.mes.service.formula.model.ProductFormula;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.formula.model.ProductFormulaVersion;
import com.bmos.mes.service.formula.vo.ProductFormulaMaterialListVO;
import com.bmos.mes.service.formula.vo.ProductFormulaMaterialVO;
import com.bmos.mes.service.formula.vo.ProductFormulaVersionDetailVO;
import com.bmos.mes.service.output.weigh.vo.OutputMaterialItem;
import com.bmos.mes.service.requisition.vo.RequisitionPlanMaterialVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ProductFormulaConverter {

    ProductFormulaConverter INSTANCE = Mappers.getMapper(ProductFormulaConverter.class);


    ProductFormula convertToProductFormula(ProductFormulaSaveDTO dto);

    ProductFormulaVersionDetailVO convertToVersionDetailVO(ProductFormulaVersion version);


    default List<ProductFormulaMaterialVO> convertToProductFormulaMaterialVO(List<ProductFormulaMaterial> materialList){
        return materialList.stream().map(e->{
            ProductFormulaMaterialVO vo = this.convertToProductFormulaMaterialVO(e);
            vo.parseToleranceInfo(e.getToleranceInfo());
            vo.setWeighRequirementList(e.getWeighRequirementList());
            return vo;
        }).collect(Collectors.toList());
    }

    ProductFormulaMaterialVO convertToProductFormulaMaterialVO(ProductFormulaMaterial material);

    default List<ProductFormulaMaterial> convertToProductFormulaMaterialList(List<ProductFormulaMaterialDTO> materialList){
        return materialList.stream().map(e->{
            ProductFormulaMaterial formulaMaterial = this.convertToProductFormulaMaterial(e);
            formulaMaterial.setToleranceInfo(e.getToleranceInfo());
            formulaMaterial.setWeighRequirementList(e.getWeighRequirementList());
            return formulaMaterial;
        }).collect(Collectors.toList());
    }

    @Mapping(target = "quantityType", expression = "java(com.bmos.mes.common.enums.formula.QuantityTypeEnum.getEnumByValue(dto.getQuantityType()))")
    @Mapping(target = "dryPureType", expression = "java(com.bmos.mes.common.enums.formula.DryAndPureTypeEnum.getEnumByValue(dto.getDryPureType()))")
    @Mapping(target = "unpackingToleranceType", expression = "java(com.bmos.mes.common.enums.formula.ToleranceTypeEnum.getEnumByValue(dto.getUnpackingToleranceType()))")
    @Mapping(target = "chargeMixtureToleranceType", expression = "java(com.bmos.mes.common.enums.formula.ToleranceTypeEnum.getEnumByValue(dto.getChargeMixtureToleranceType()))")
    @Mapping(target = "oddmentToleranceType", expression = "java(com.bmos.mes.common.enums.formula.ToleranceTypeEnum.getEnumByValue(dto.getOddmentToleranceType()))")
    @Mapping(target = "materialType", expression = "java(com.bmos.mes.common.enums.CategoryInfoTypeEnum.getEnumByValue(dto.getMaterialType()))")
    ProductFormulaMaterial convertToProductFormulaMaterial(ProductFormulaMaterialDTO dto);

    ProductFormulaVersion convertToProductFormulaVersion(ProductFormulaSaveVersionDTO dto);

    List<ProductFormulaMaterialListVO> convertToProductFormulaMaterialListVO(List<ProductFormulaMaterial> materials);

    RequisitionPlanMaterialVO convertToRequisitionPlanMaterialVO(ProductFormulaMaterial material);

    @Mapping(target = "id", source = "materialId")
    @Mapping(target = "name", source = "materialName")
    @Mapping(target = "specification", source = "materialSpecification")
    @Mapping(target = "mergeCode", source = "materialMergeCode")
    OutputMaterialItem convertToOutputMaterialItem(ProductFormulaMaterial material);

    List<OutputMaterialItem> convertToOutputMaterialItem(List<ProductFormulaMaterial> materialsList);
}
