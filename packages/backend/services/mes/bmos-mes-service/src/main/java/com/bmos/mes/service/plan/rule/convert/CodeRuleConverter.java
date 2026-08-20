package com.bmos.mes.service.plan.rule.convert;

import com.bmos.mes.service.plan.rule.dto.CodeRuleSaveDTO;
import com.bmos.mes.service.plan.rule.dto.CodeRuleUpdateDTO;
import com.bmos.mes.service.plan.rule.model.CodeRule;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface CodeRuleConverter {
    CodeRuleConverter INSTANCE = Mappers.getMapper(CodeRuleConverter.class);

    default List<CodeRule> convertList(CodeRuleSaveDTO dto) {
        return dto.getProcessIds()
            .stream()
            .map(processId -> {
                CodeRule codeRule = convertDO(dto);
                codeRule.setProcessId(processId);
                return codeRule;
            })
            .collect(Collectors.toList());
    }

    CodeRule convertDO(CodeRuleSaveDTO dto);

    CodeRule convertDO(CodeRuleUpdateDTO dto);
}
