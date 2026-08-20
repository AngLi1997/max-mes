package com.bmos.platform.service.system.code.mapper;

import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.service.system.code.model.CodeRuleDept;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface CodeRuleDeptMapper extends BaseMapperX<CodeRuleDept> {
    default void deletByCodeRuleId(Long codeRuleId) {
        delete(new LambdaQueryWrapperX<CodeRuleDept>()
            .eq(CodeRuleDept::getCodeRuleId, codeRuleId)
        );
    }

    default List<Long> selectByCodeRuleId(Long codeRuleId) {
        return selectList(CodeRuleDept::getCodeRuleId, codeRuleId)
            .stream()
            .map(CodeRuleDept::getDeptId)
            .collect(Collectors.toList());
    }
}
