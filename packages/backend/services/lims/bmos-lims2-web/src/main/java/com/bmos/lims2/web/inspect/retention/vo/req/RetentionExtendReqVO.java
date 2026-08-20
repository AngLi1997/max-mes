package com.bmos.lims2.web.inspect.retention.vo.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @Description: 留样延期请求VO
 * @Author: yigaohui
 * @Date: 2026/02/09
 */
@Data
@ApiModel("留样延期请求")
public class RetentionExtendReqVO {

    @ApiModelProperty(value = "新的留样期限", required = true)
    @NotNull(message = "新的留样期限不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate newExpiryDate;
}
