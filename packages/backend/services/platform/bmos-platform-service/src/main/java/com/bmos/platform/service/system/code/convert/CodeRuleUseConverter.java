package com.bmos.platform.service.system.code.convert;

import com.bmos.expression.pojo.KeyValue;
import com.bmos.platform.common.enums.BooleanEnum;
import com.bmos.platform.service.system.code.model.CodeRuleUse;
import com.bmos.platform.service.system.code.vo.BatchNextUseCodeElementVO;
import com.bmos.platform.service.system.code.vo.supplierno.CodeRuleUseVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface CodeRuleUseConverter {
    CodeRuleUseConverter INSTANCE = Mappers.getMapper(CodeRuleUseConverter.class);

    default CodeRuleUse convertDO(CodeRuleUseVO vo) {
        LocalDateTime now = LocalDateTime.now();
        return CodeRuleUse.builder()
            .code(vo.getCode())
            .fullNo(vo.getFullNo())
            .resetNo(vo.getResetFiledValue())
            .resetDate(vo.getCodeApplyTime())
            .sequence(vo.getSequence())
            .confirm(BooleanEnum.FALSE)
            .skip(BooleanEnum.FALSE)
            .createTime(now)
            .updateTime(now)
            .build();
    }

    default List<CodeRuleUse> convertList(CodeRuleUseVO vo, List<BatchNextUseCodeElementVO> elementVOS) {
        CodeRuleUse codeRuleUse = convertDO(vo);
        return elementVOS.stream()
            .map(elementVO -> codeRuleUse.withSequence(elementVO.getSequence()).withFullNo(elementVO.getFullNo()))
            .collect(Collectors.toList());
    }

    default CodeRuleUse convertDO2(CodeRuleUseVO vo, String fullNo) {
        CodeRuleUse codeRuleUse = convertDO(vo);
        codeRuleUse.setSkip(BooleanEnum.TRUE);
        codeRuleUse.setConfirm(BooleanEnum.TRUE);
        codeRuleUse.setFullNo(fullNo);
        return codeRuleUse;
    }

    default List<CodeRuleUse> convertList2(CodeRuleUseVO vo, List<String> fullNos) {
        CodeRuleUse codeRuleUse = convertDO(vo);
        codeRuleUse.setSkip(BooleanEnum.TRUE);
        codeRuleUse.setConfirm(BooleanEnum.TRUE);
        return fullNos.stream().map(codeRuleUse::withFullNo).collect(Collectors.toList());
    }
}
