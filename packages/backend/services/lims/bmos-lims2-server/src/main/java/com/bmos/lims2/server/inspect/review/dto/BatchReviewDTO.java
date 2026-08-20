package com.bmos.lims2.server.inspect.review.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ApiModel("批量复核请求DTO")
public class BatchReviewDTO {

    @ApiModelProperty("任务ID列表")
    @NotEmpty
    private List<@NotNull Long> taskIds;

    @ApiModelProperty("是否通过")
    @NotNull
    private Boolean approve;

    @ApiModelProperty("不通过原因")
    private String reason;

    @ApiModelProperty("复核人Id")
    private String reviewerId;
}


