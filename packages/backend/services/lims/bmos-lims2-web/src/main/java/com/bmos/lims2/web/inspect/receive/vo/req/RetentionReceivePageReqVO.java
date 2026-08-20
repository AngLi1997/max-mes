package com.bmos.lims2.web.inspect.receive.vo.req;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Description: 留样接收分页查询请求VO
 * @Author: yigaohui
 * @Date: 2026/02/06 16:30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("留样接收分页查询请求")
public class RetentionReceivePageReqVO extends BasePage {

    @ApiModelProperty("样品编号")
    private String sampleNo;

    @ApiModelProperty("物料ID集合")
    private List<Long> materialIds;

    @ApiModelProperty("批次号")
    private String batchNo;
}
