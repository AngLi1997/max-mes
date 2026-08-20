package com.bmos.lims2.web.inspect.sampling.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 样品标签打印请求VO
 *
 * @author yigaohui
 * @since 2025/01/29 16:00
 */
@Data
@ApiModel("样品标签打印请求")
public class SampleUpdateReqVO {

    @ApiModelProperty(value = "请验单ID", required = true)
    private Long inspectionOrderId;

    @ApiModelProperty(value = "样品列表", required = true)
    @NotEmpty(message = "样品列表不能为空")
    private List<SamplingItemReqVO> samples;

    @ApiModelProperty("备注")
    private String remark;
}
