package com.bmos.lims2.server.operate.model;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author renjinguang
 */
@Setter
@Getter
@ToString
@TableName(value = "bm_operate_rule_version")
public class OperateRuleVersion extends BaseDO {

    @ApiModelProperty("operate_rule_id")
    private Long operateId;

    @ApiModelProperty("版本号")
    private String version;

    @ApiModelProperty("描述")
    private String remark;

    @ApiModelProperty("历史状态")
    private String historyState;

    @ApiModelProperty("状态")
    private String state;

    @ApiModelProperty("文件上传地址")
    private String url;

    /**
     * 文件上传日期
     */
    @ApiModelProperty("文件上传日期")
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private LocalDateTime uploadTime;

    @ApiModelProperty("流程id")
    private String instanceId;

    @ApiModelProperty("流程类型")
    private String auditType;

    @ApiModelProperty("生效日期")
    private String effectDate;

    /**
     * 线下文件生效日期
     */
    @ApiModelProperty("线下文件生效日期")
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String fileEffectDate;

    @ApiModelProperty("分类id")
    @TableField(exist = false)
    private Long categoryId;
}
