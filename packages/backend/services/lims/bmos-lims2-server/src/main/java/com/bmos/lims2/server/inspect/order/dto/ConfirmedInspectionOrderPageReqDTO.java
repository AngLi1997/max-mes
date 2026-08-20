package com.bmos.lims2.server.inspect.order.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @className: ConfirmedInspectionOrderPageReqDTO
 * @author: yigaohui
 * @date: 2025/8/19 17:40
 * @Version: 1.0
 * @description:
 */

@Data
public class ConfirmedInspectionOrderPageReqDTO extends BasePage {
    @ApiModelProperty("检验单号")
    private String orderNo;
    @ApiModelProperty("检品名称")
    private String materialName;
    @ApiModelProperty("批次号")
    private String batchNo;
}
