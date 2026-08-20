package com.bmos.platform.service.system.code.vo;

import com.bmos.common.util.json.JsonUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("DetailCodeRuleVersionDetailVO:编码规则版本详情")
public class DetailCodeRuleVersionDetailVO {
    @ApiModelProperty("规则id")
    private Long id;
    @ApiModelProperty("版本id")
    private Long versionId;
    @ApiModelProperty("编码")
    private String code;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("数据字典id")
    private Long dictId;
    @ApiModelProperty("版本号")
    private String version;
    @ApiModelProperty("版本描述")
    private String description;
    @ApiModelProperty("重置规则")
    private String resetRule;

    @ApiModelProperty("重置规则列表")
    private List<Integer> resetRules;

    public List<Integer> getResetRules() {
        return JsonUtils.parseArray(resetRule, Integer.class);
    }

    @ApiModelProperty("编码规则详情")
    private List<CodeRuleVersionDetailVO> details;
}
