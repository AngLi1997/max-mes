package com.bmos.platform.service.system.code.convert;

import com.bmos.platform.service.system.code.dto.CodeRuleVersionDetailSaveDTO;
import com.bmos.platform.service.system.code.model.CodeRuleVersion;
import com.bmos.platform.service.system.code.model.CodeRuleVersionDetail;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface CodeRuleVersionDetailConverter {
    CodeRuleVersionDetailConverter INSTANCE = Mappers.getMapper(CodeRuleVersionDetailConverter.class);

    default List<CodeRuleVersionDetail> convertList(Long id, List<CodeRuleVersionDetailSaveDTO> details) {
        return details.stream()
            .map(this::convertDO)
            .peek(entity -> entity.setCodeRuleVersionId(id))
            .collect(Collectors.toList());
    }

    CodeRuleVersionDetail convertDO(CodeRuleVersionDetailSaveDTO dto);
}
