package com.bmos.mes.service.inspect.controller.vo;

import com.bmos.mes.common.enums.inpspect.InspectStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel("请验单分页VO")
public class InspectPageVO {

    /**
     * 检验单id
     */
    @ApiModelProperty("检验单id")
    private Long id;

    /**
     * 请验状态
     */
    @ApiModelProperty("请验状态")
    private InspectStatusEnum status;

    /**
     * 退回原因
     */
    @ApiModelProperty("退回原因")
    private String reason;

    /**
     * 检验单编号
     */
    @ApiModelProperty("检验单编号")
    private String inspectNo;

    /**
     * 物料批号
     */
    @ApiModelProperty("物料批号")
    private String materialBatchNo;

    /**
     * 请验人
     */
    @ApiModelProperty("请验人")
    private String inspector;

    /**
     * 请验时间
     */
    @ApiModelProperty("请验时间")
    private LocalDateTime inspectTime;

    /**
     * 物料合并编码
     */
    @ApiModelProperty("物料合并编码")
    private String materialMergeCode;

    /**
     * 物料名称
     */
    @ApiModelProperty("物料名称")
    private String materialName;
}
