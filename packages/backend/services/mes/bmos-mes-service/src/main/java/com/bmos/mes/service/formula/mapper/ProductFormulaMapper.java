package com.bmos.mes.service.formula.mapper;

import com.bmos.mes.service.formula.dto.ProductFormulaPageQueryDTO;
import com.bmos.mes.service.formula.model.ProductFormula;
import com.bmos.mes.service.formula.vo.ProductFormulaListVO;
import com.bmos.mes.service.formula.vo.ProductFormulaPageVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductFormulaMapper extends BaseMapperX<ProductFormula> {

    default boolean checkNameExists(String name) {
        return exists(new LambdaQueryWrapperX<ProductFormula>()
                .eq(ProductFormula::getName, name));
    }

    List<ProductFormulaPageVO> selectPageList(ProductFormulaPageQueryDTO dto);

    List<ProductFormulaPageVO> selectPageByProductIdList(ProductFormulaPageQueryDTO dto);

    default boolean existedProductFormula(Long productId){
        return exists(new LambdaQueryWrapperX<ProductFormula>().eq(ProductFormula::getProductId, productId));
    }

    List<ProductFormulaListVO> selectEnableProductFormulaByProductId(@Param("productId") Long productId, @Param("deptIds") List<Long> deptIds);

    List<String> getAuditBusinessKey(@Param("deptIdList") List<Long> deptIdList);

    ProductFormulaListVO getFormulaByVersionId(@Param("versionId") Long productFormulaVersionId);
}
