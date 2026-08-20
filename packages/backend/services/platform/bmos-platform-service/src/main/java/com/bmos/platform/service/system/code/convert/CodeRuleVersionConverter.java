package com.bmos.platform.service.system.code.convert;

import com.bmos.platform.service.system.code.dto.CodeRuleSaveDTO;
import com.bmos.platform.service.system.code.dto.CodeRuleUpdateDTO;
import com.bmos.platform.service.system.code.dto.CodeRuleVersionSaveDTO;
import com.bmos.platform.service.system.code.dto.CodeRuleVersionUpdateDTO;
import com.bmos.platform.service.system.code.model.CodeRuleVersion;
import com.bmos.platform.service.system.expression.dto.ExpressionCategorySaveDTO;
import com.bmos.platform.service.system.expression.dto.ExpressionCategoryUpdateDTO;
import com.bmos.platform.service.system.expression.dto.ExpressionSaveDTO;
import com.bmos.platform.service.system.expression.dto.ExpressionUpdateDTO;
import com.bmos.platform.service.system.expression.model.Expression;
import com.bmos.platform.service.system.expression.model.ExpressionCategory;
import com.bmos.platform.service.system.expression.vo.ExpressionCategoryTreeNodeVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CodeRuleVersionConverter {
    CodeRuleVersionConverter INSTANCE = Mappers.getMapper(CodeRuleVersionConverter.class);

    @Mapping(source = "code", target = "ruleCode")
    CodeRuleVersion convertDO(CodeRuleSaveDTO dto);

    @Mapping(source = "versionId", target = "id")
    CodeRuleVersion convertDO(CodeRuleUpdateDTO dto);

    @Mapping(source = "code", target = "ruleCode")
    CodeRuleVersion convertDO(CodeRuleVersionSaveDTO dto);

    CodeRuleUpdateDTO convertDTO(CodeRuleVersionUpdateDTO dto);

    CodeRuleSaveDTO convertDTO(CodeRuleVersionSaveDTO dto);
}
