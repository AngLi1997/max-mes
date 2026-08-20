package com.bmos.lims2.server.inspect.receive.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Description: 留样接收分页查询DTO
 * @Author: yigaohui
 * @Date: 2026/02/06 16:00
 */
@Getter
@Setter
@ApiModel("留样接收分页查询参数")
public class RetentionReceivePageQueryDTO extends BasePage {

    @ApiModelProperty("样品编号")
    private String sampleNo;

    @ApiModelProperty("物料ID集合")
    private List<Long> materialIds;

    @ApiModelProperty("批次号")
    private String batchNo;
}
