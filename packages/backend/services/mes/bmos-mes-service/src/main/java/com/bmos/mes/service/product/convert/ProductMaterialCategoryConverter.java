package com.bmos.mes.service.product.convert;

import com.bmos.audit.engine.core.query.resp.TaskListResp;
import com.bmos.mes.service.formula.vo.ProductFormulaAuditPageVO;
import com.bmos.mes.service.formula.vo.ProductFormulaVersionAuditVO;
import com.bmos.mes.service.product.dto.CategoryIssueDTO;
import com.bmos.mes.service.product.dto.ProductMaterialCategorySaveDTO;
import com.bmos.mes.service.product.dto.ProductMaterialCategoryUpdateDTO;
import com.bmos.mes.service.product.model.ProductMaterialCategory;
import com.bmos.mes.service.product.vo.ProductMaterialCategoryTreeNodeVO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper
public interface ProductMaterialCategoryConverter {

    ProductMaterialCategoryConverter INSTANCE = Mappers.getMapper(ProductMaterialCategoryConverter.class);

    ProductMaterialCategory convertMaterialCategory(ProductMaterialCategorySaveDTO dto);
    ProductMaterialCategory convertMaterialCategory(ProductMaterialCategoryUpdateDTO dto);
    ProductMaterialCategory convertMaterialCategory(CategoryIssueDTO dto);

    List<ProductMaterialCategoryTreeNodeVO> convertCategoryTreeNode(List<ProductMaterialCategory> categories);

    default List<ProductFormulaAuditPageVO> convertToAuditPageVO(List<TaskListResp> list, Map<String, ProductFormulaVersionAuditVO> map){
        return list.stream().map(element -> convertToPageVO(element, map.get(element.getProcessInstanceId()))).collect(Collectors.toList());
    }

    default ProductFormulaAuditPageVO convertToPageVO(TaskListResp element, ProductFormulaVersionAuditVO auditVO){
        ProductFormulaAuditPageVO pageVO = convertToPageVO(element);
        convertVO(pageVO, auditVO);
        return pageVO;
    }

    void convertVO(@MappingTarget ProductFormulaAuditPageVO pageVO, ProductFormulaVersionAuditVO auditVO);

    ProductFormulaAuditPageVO convertToPageVO(TaskListResp element);
}
