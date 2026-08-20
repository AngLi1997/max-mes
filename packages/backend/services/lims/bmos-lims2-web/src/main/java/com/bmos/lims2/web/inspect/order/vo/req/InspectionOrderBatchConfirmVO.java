package com.bmos.lims2.web.inspect.order.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 检验单批量确认请求VO
 *
 * @author yigaohui
 * @since 2025/01/27 15:30
 */
@Data
@ApiModel("检验单批量确认请求")
public class InspectionOrderBatchConfirmVO {

    @ApiModelProperty(value = "检验单ID列表", required = true)
    @NotEmpty(message = "检验单ID列表不能为空")
    private List<Long> ids;
}