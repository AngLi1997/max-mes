package com.bmos.lims2.web.inspect.retention.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description: 批量样品销毁请求VO
 * @Author: yigaohui
 * @Date: 2026/02/11
 */
@Data
@ApiModel("批量样品销毁请求")
public class BatchSampleDestructionReqVO {

    @ApiModelProperty(value = "样品ID列表", required = true)
    @NotEmpty(message = "样品ID列表不能为空")
    private List<Long> sampleIds;

    @ApiModelProperty(value = "销毁原因", required = true)
    @NotBlank(message = "销毁原因不能为空")
    private String destructionReason;

    @ApiModelProperty(value = "销毁方式", required = true)
    @NotBlank(message = "销毁方式不能为空")
    private String destructionMethod;

    @ApiModelProperty(value = "销毁时间", required = true)
    @NotNull(message = "销毁时间不能为空")
    private LocalDateTime destructionTime;

    @ApiModelProperty(value = "销毁地点", required = true)
    @NotBlank(message = "销毁地点不能为空")
    private String destructionLocation;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("销毁人ID（默认当前登录人）")
    private String destructorId;

    @ApiModelProperty("销毁人名称（默认当前登录人）")
    private String destructorName;

    @ApiModelProperty(value = "监督人ID", required = true)
    @NotBlank(message = "监督人ID不能为空")
    private String supervisorId;

    @ApiModelProperty(value = "监督人名称", required = true)
    @NotBlank(message = "监督人名称不能为空")
    private String supervisorName;
}
