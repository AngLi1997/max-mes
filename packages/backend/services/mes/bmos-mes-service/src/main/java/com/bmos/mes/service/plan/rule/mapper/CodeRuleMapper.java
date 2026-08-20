package com.bmos.mes.service.plan.rule.mapper;

import com.bmos.mes.common.enums.plan.CodeRuleTypeEnum;
import com.bmos.mes.service.plan.rule.dto.CodeRulePageDTO;
import com.bmos.mes.service.plan.rule.model.CodeRule;
import com.bmos.mes.service.plan.rule.vo.CodeRulePageVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface CodeRuleMapper extends BaseMapperX<CodeRule> {
    List<CodeRulePageVO> page(CodeRulePageDTO dto);

    void deleteByCodeRuleCode(@Param("codeRuleCode") String codeRuleCode);

    void deleteByProcessIdAndType(@Param("processIds") List<Long> processIds, @Param("typeEnum") String typeEnum);

    default List<Long> detailCode(String code) {
        return selectList(new LambdaQueryWrapperX<CodeRule>()
            .select(CodeRule::getProcessId)
            .eq(CodeRule::getCodeRuleCode, code))
            .stream()
            .map(CodeRule::getProcessId)
            .collect(Collectors.toList());
    }

    default CodeRule selectByProcessIdAndType(Long processId, String type) {
        return selectOne(CodeRule::getProcessId, processId, CodeRule::getType, type);
    }

    default List<CodeRule> getCodeRuleListByProcessIdAndType(Set<Long> processIdList, List<String> codes){
        return selectList(new LambdaQueryWrapperX<CodeRule>()
                .in(CodeRule::getProcessId,processIdList)
                .in(CodeRule::getType,codes));
    }
}
