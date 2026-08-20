package com.bmos.mes.service.formula.mapper;

import com.bmos.mes.service.formula.dto.ProductFormulaAuditPageQueryDTO;
import com.bmos.mes.service.formula.dto.ProductFormulaVersionPageQueryDTO;
import com.bmos.mes.service.formula.model.ProductFormulaVersion;
import com.bmos.mes.service.formula.vo.ProductFormulaAuditPageVO;
import com.bmos.mes.service.formula.vo.ProductFormulaVersionAuditVO;
import com.bmos.mes.service.formula.vo.ProductFormulaVersionDetailVO;
import com.bmos.mes.service.formula.vo.ProductFormulaVersionPageVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductFormulaVersionMapper extends BaseMapperX<ProductFormulaVersion> {

    List<ProductFormulaVersionPageVO> selectPageList(ProductFormulaVersionPageQueryDTO dto);

    default boolean checkExistedEnabledVersion(Long productFormulaId) {
        return exists(new LambdaQueryWrapperX<ProductFormulaVersion>()
                .eq(ProductFormulaVersion::getProductFormulaId, productFormulaId)
                .eq(ProductFormulaVersion::getEnable, true));
    }

    default boolean checkExistedVersionNo(Long formulaId, String versionNo, Long versionId) {
        return exists(new LambdaQueryWrapperX<ProductFormulaVersion>()
                .eq(ProductFormulaVersion::getVersionNo, versionNo)
                .eq(ProductFormulaVersion::getProductFormulaId, formulaId)
                .neIfPresent(ProductFormulaVersion::getId, versionId));
    }

    default ProductFormulaVersion selectByProcessInstanceId(String processInstanceId) {
        return selectOne(new LambdaQueryWrapperX<ProductFormulaVersion>()
                .eq(ProductFormulaVersion::getProcessInstanceId, processInstanceId));
    }

    List<Long> selectAuditBusinessIds(ProductFormulaAuditPageQueryDTO dto);

    List<ProductFormulaVersionAuditVO> selectByProcessInstanceIds(@Param("ids") List<String> instanceIds);

    ProductFormulaVersionDetailVO selectDetailById(Long versionId);
}
