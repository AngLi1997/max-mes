package com.bmos.platform.service.system.code.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.platform.common.enums.StatusEnum;
import com.bmos.platform.common.enums.system.code.VersionStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
* 编码规则版本信息表
*/
@Getter
@Setter
@ToString
@TableName(value = "bp_code_rule_version", autoResultMap = true)
public class CodeRuleVersion extends BaseDO {
    /**
     * @see CodeRule#getCode()
     */
    @ApiModelProperty("编码规则编码 -- 关联CodeRule#code")
    private String ruleCode;

    @ApiModelProperty("数据字典id")
    private Long dictId;

    @ApiModelProperty("版本号")
    private String version;

    @ApiModelProperty("版本描述")
    private String description;

    @ApiModelProperty("编辑 EDIT 确认 CONFIRM")
    private VersionStatusEnum versionStatus;

    @ApiModelProperty("启用 停用")
    private Boolean status;

    /**
    * @see CodeRuleVersionDetail#getId() 列表
    */
    @ApiModelProperty("重置规则")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Long> resetRule;

    @ApiModelProperty("删除标志")
    private Long delVersionFlag;
}
