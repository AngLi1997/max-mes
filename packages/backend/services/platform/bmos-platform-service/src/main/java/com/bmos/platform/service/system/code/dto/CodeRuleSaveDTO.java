package com.bmos.platform.service.system.code.dto;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.common.exception.BmosException;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.platform.common.enums.BooleanEnum;
import com.bmos.platform.common.enums.system.code.RuleTypeEnum;
import com.bmos.platform.common.exception.PlatformResponseCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* 编码规则主表
*/
@Getter
@Setter
@ApiModel("CodeRuleSaveDTO:编码规则保存DTO")
public class CodeRuleSaveDTO {
    @NotBlank
    @ApiModelProperty(value = "规则编码", required = true)
    private String code;
    @NotBlank
    @ApiModelProperty(value = "规则名称", required = true)
    private String name;
    @NotBlank
    @ApiModelProperty(value = "版本号", required = true)
    private String version;
    @ApiModelProperty(value = "数据字典id", required = true)
    private Long dictId;
    @NotNull
    @ApiModelProperty(value = "版本描述", required = true)
    private String description;
    @ApiModelProperty(value = "重置规则")
    private List<Integer> resetRule;
    @Valid
    @NotEmpty
    @ApiModelProperty(value = "规则信息", required = true)
    private List<CodeRuleVersionDetailSaveDTO> codeRuleVersionDetails;

    @JsonIgnore
    public boolean isValidated() {
        if (CollUtil.isEmpty(resetRule)) {
            return Boolean.TRUE;
        }
        Map<Integer, String> codeRuleVersionDetailMap = codeRuleVersionDetails.stream()
            .collect(Collectors.toMap(CodeRuleVersionDetailSaveDTO::getSort, CodeRuleVersionDetailSaveDTO::getType));
        long dataTypeNum = resetRule.stream()
            .map(codeRuleVersionDetailMap::get)
            .filter(type -> RuleTypeEnum.DATE.getValue().equals(type))
            .count();
        if (dataTypeNum > 1L) {
            throw new BmosException(PlatformResponseCode.REST_RULE_DATA_ONLY_ONE);
        }
        if (resetRule.stream()
            .map(codeRuleVersionDetailMap::get)
            .anyMatch(type -> RuleTypeEnum.SEQUENCE.getValue().equals(type))) {
            throw new BmosException(PlatformResponseCode.REST_RULE_SEQUENCR_NOT_EXISTS);
        }
        for (CodeRuleVersionDetailSaveDTO codeRuleVersionDetail : codeRuleVersionDetails) {
            codeRuleVersionDetail.isTrue();
        }
        return Boolean.TRUE;
    }
}
