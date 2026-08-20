package com.bmos.lims2.web.inspect.order.vo.req;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @className: ConfirmedInspectionOrderPageReqVO
 * @author: yigaohui
 * @date: 2025/8/19 17:38
 * @Version: 1.0
 * @description:
 */

@Data
@ApiModel("已确认请验单分页查询请求")
public class ConfirmedInspectionOrderPageReqVO extends BasePage {

    @ApiModelProperty("检验单号")
    private String orderNo;
    @ApiModelProperty("检品名称")
    private String materialName;
    @ApiModelProperty("批次号")
    private String batchNo;
}
