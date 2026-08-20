package com.bmos.lims2.web.inspect.receive.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Description: 留样样品批量接收请求VO
 * @Author: yigaohui
 * @Date: 2026/02/06 17:00
 */
@Data
@ApiModel("留样样品批量接收请求")
public class RetentionReceiveBatchReqVO {

    @ApiModelProperty(value = "样品ID列表", required = true)
    @NotEmpty(message = "样品ID列表不能为空")
    private List<Long> sampleIds;

    @ApiModelProperty(value = "储存位置", required = true)
    @NotBlank(message = "储存位置不能为空")
    private String storageLocation;
}
