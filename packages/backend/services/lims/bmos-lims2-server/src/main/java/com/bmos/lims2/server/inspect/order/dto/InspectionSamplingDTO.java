package com.bmos.lims2.server.inspect.order.dto;

import com.bmos.lims2.common.enums.SamplingStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * 检验取样信息DTO
 *
 * @author yigaohui
 * @since 2025/01/27 15:30
 */
@Getter
@Setter
@Accessors(chain = true)
@ApiModel("检验取样信息数据对象")
public class InspectionSamplingDTO {

    @ApiModelProperty("取样信息ID")
    private Long id;

    @ApiModelProperty("检验单ID")
    private Long inspectionOrderId;

    @ApiModelProperty("检验项目ID")
    private Long inspectItemId;

    @ApiModelProperty("检验项目名称")
    private String inspectItemName;

    @ApiModelProperty("计划取样量")
    private String plannedQuantity;

    @ApiModelProperty("单位ID")
    private Long unitId;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("计划取样份数")
    private Integer sampleCount;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("关联的样品ID")
    private Long sampleId;

    @ApiModelProperty("关联的样品编号")
    private String sampleNo;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}