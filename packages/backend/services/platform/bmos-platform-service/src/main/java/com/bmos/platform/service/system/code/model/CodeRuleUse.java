package com.bmos.platform.service.system.code.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.platform.common.enums.BooleanEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;
import lombok.experimental.Tolerate;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 编码规则使用表
 */
@Getter
@Setter
@Builder
@With
@TableName(value = "bp_code_rule_use")
public class CodeRuleUse {
    @Tolerate
    public CodeRuleUse() {}
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty("主键")
    private Long id;

    /**
     * @see CodeRule#getCode()
     */
    @ApiModelProperty("编码code")
    private String code;

    @ApiModelProperty("完整标号")
    private String fullNo;

    @ApiModelProperty("序列号")
    private Long sequence;

    @ApiModelProperty("重置字段数据")
    private String resetNo;

    @TableField("is_confirm")
    @ApiModelProperty("是否业务方确认")
    private BooleanEnum confirm;

    @TableField("is_skip")
    @ApiModelProperty("是否需跳过 -- 需跳号的数据")
    private BooleanEnum skip;

    @ApiModelProperty("重置日期 存放重置规则日期最大值")
    private LocalDate resetDate;

    @ApiModelProperty("")
    private LocalDateTime createTime;

    @ApiModelProperty("")
    private LocalDateTime updateTime;
}
