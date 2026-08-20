package com.bmos.platform.service.system.code.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@TableName(value = "bp_code_rule_dept")
public class CodeRuleDept {
    public CodeRuleDept() {

    }

    public CodeRuleDept(Long codeRuleId, Long deptId) {
        this.codeRuleId = codeRuleId;
        this.deptId = deptId;
    }

    @ApiModelProperty("字典表")
    private Long codeRuleId;

    @ApiModelProperty("部门id")
    private Long deptId;
}
