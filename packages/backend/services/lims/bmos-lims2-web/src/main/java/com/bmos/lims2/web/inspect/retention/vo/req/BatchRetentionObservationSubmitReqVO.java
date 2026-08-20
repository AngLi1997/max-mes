package com.bmos.lims2.web.inspect.retention.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Description: 批量留样观察提交请求VO
 * @Author: yigaohui
 * @Date: 2026/02/11
 */
@Data
@ApiModel("批量留样观察提交请求")
public class BatchRetentionObservationSubmitReqVO {

    @ApiModelProperty(value = "观察任务列表", required = true)
    @NotEmpty(message = "观察任务列表不能为空")
    @Valid
    private List<ObservationTaskItem> tasks;

    @Data
    @ApiModel("观察任务项")
    public static class ObservationTaskItem {

        @ApiModelProperty(value = "任务ID", required = true)
        private Long taskId;

        @ApiModelProperty(value = "观察结果（true-符合，false-不符合）", required = true)
        private Boolean observationResult;

        @ApiModelProperty(value = "观察备注")
        private String observationRemark;
    }
}
