package com.bmos.mes.service.tareweigh.config.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 皮重配置信息VO
 * @author liang
 * @version 1.0.0
 * @date 2024/9/23 10:40
 */
@Data
@ApiModel("皮重配置信息VO")
public class TareWeighConfigVO {

    @ApiModelProperty(value = "id", example = "1")
    private Long id;

    @ApiModelProperty(value = "皮重", example = "10.0")
    private String tareWeigh;

    @ApiModelProperty(value = "单位", example = "kg")
    private String unit;

    @ApiModelProperty(value = "单位id", example = "1")
    private Long unitId;

    @ApiModelProperty(value = "基本单位", example = "kg")
    private String basicUnit;

    @ApiModelProperty(value = "基本单位id", example = "1")
    private Long basicUnitId;

    @ApiModelProperty(value = "描述", example = "描述")
    private String describeInfo;

    @ApiModelProperty(value = "修订人id", example = "1")
    private String editorId;

    @ApiModelProperty(value = "修订人", example = "张三")
    private String editorName;

    @ApiModelProperty(value = "修订人登录名", example = "zhangsan")
    private String editorLoginName;

    @ApiModelProperty(value = "修订时间", example = "2024-09-23 10:40:00")
    private LocalDateTime editTime;
}
