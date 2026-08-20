package com.bmos.platform.service.system.code.mapper;

import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.service.system.code.model.CodeRuleVersion;
import com.bmos.platform.service.system.code.model.CodeRuleVersionDetail;
import com.bmos.platform.service.system.code.vo.CodeRuleUseDetailVO;
import com.bmos.platform.service.system.code.vo.CodeRuleVersionDetailVO;
import com.bmos.platform.service.system.expression.model.ExpressionCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CodeRuleVersionDetailMapper extends BaseMapperX<CodeRuleVersionDetail> {
    /**
     * 详情
     * @param versionId 版本ID
     * @return List<CodeRuleVersionDetailVO>
     */
    List<CodeRuleVersionDetailVO> detail(@Param("versionId") Long versionId);

    void deleteByCodeRuleVersionId(@Param("codeRuleVesrionId") Long codeRuleVesrionId);

    default boolean existsParameterId(Long parameterId) {
        return exists(new LambdaQueryWrapperX<CodeRuleVersionDetail>()
            .eq(CodeRuleVersionDetail::getParameterId, parameterId)
            .last(" limit 1 ")
        );
    }

    List<CodeRuleUseDetailVO> listDetail(@Param("versionId") Long versionId);
}
