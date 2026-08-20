package com.bmos.platform.service.system.code.convert;

import com.bmos.platform.service.system.code.dto.CodeRulePermissionDTO;
import com.bmos.platform.service.system.code.dto.CodeRuleSaveDTO;
import com.bmos.platform.service.system.code.dto.CodeRuleUpdateDTO;
import com.bmos.platform.service.system.code.model.CodeRule;
import com.bmos.platform.service.system.code.model.CodeRuleDept;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface CodeRuleConverter {
    CodeRuleConverter INSTANCE = Mappers.getMapper(CodeRuleConverter.class);

    CodeRule convertDO(CodeRuleSaveDTO dto);

    CodeRule convertDO(CodeRuleUpdateDTO dto);

    default List<CodeRuleDept> convertList(CodeRulePermissionDTO dto) {
        return dto.getDeptIds().stream()
            .map(deptId -> new CodeRuleDept(dto.getCodeRuleId(), deptId))
            .collect(Collectors.toList());
    }
}
