package com.bmos.lims2.web.stability.plan.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * 稳定性考察计划保存请求VO
 */
@Data
@ApiModel("稳定性考察计划保存请求")
public class StabilityInspectPlanSaveReqVO {

    @ApiModelProperty("计划ID（新增时为null）")
    private Long id;

    @ApiModelProperty(value = "检品ID", required = true)
    @NotNull(message = "检品不能为空")
    private Long materialId;

    @ApiModelProperty("检品名称")
    private String materialName;

    @ApiModelProperty("检品编码")
    private String materialCode;

    @ApiModelProperty("检品规格")
    private String materialSpec;

    @ApiModelProperty(value = "稳定性方案ID", required = true)
    @NotNull(message = "稳定性方案不能为空")
    private Long schemeId;

    @ApiModelProperty(value = "稳定性方案版本ID", required = true)
    @NotNull(message = "稳定性方案版本不能为空")
    private Long schemeVersionId;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty(value = "批次列表", required = true)
    @NotEmpty(message = "至少添加一个批次")
    @Valid
    private List<BatchVO> batches;

    @Data
    @ApiModel("批次信息")
    public static class BatchVO {

        @ApiModelProperty(value = "批号", required = true)
        @NotNull(message = "批号不能为空")
        private String batchNo;

        @ApiModelProperty(value = "批次生产日期", required = true)
        @NotNull(message = "批次生产日期不能为空")
        private LocalDate productionDate;
    }
}
