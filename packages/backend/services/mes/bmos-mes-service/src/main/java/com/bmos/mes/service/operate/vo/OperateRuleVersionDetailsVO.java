package com.bmos.mes.service.operate.vo;

import com.bmos.mes.common.enums.operate.OperateRuleVersionStateEnum;
import io.swagger.annotations.ApiModel;
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
@ApiModel(value = "操作规程版本详情返回vo")
public class OperateRuleVersionDetailsVO {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("版本号")
    private String version;

    @ApiModelProperty("描述")
    private String remark;

    @ApiModelProperty("文件上传地址")
    private String url;

    @ApiModelProperty("文件上传时间")
    private LocalDateTime uploadTime;

    @ApiModelProperty("流程实例id")
    private String instanceId;

    @ApiModelProperty("状态")
    private String state;

    @ApiModelProperty("状态")
    private OperateRuleVersionStateEnum stateEnum;

    @ApiModelProperty("生效日期")
    private String effectDate;

    @ApiModelProperty("线下文件生效日期")
    private String fileEffectDate;

    @ApiModelProperty("流程类型")
    private String auditType;

    @ApiModelProperty("主表id")
    private Long operateId;

    @ApiModelProperty("文件名称")
    private String name;

    @ApiModelProperty("文件编号")
    private String code;

    @ApiModelProperty("分类id")
    private Long categoryId;

    public OperateRuleVersionStateEnum getStateEnum() {
        return OperateRuleVersionStateEnum.getEnumByCode(state);
    }
}
