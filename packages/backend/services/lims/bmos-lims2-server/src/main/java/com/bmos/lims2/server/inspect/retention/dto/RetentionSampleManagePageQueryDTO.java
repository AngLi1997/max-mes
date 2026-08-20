package com.bmos.lims2.server.inspect.retention.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Description: 留样样品管理分页查询DTO
 * @Author: yigaohui
 * @Date: 2026/02/09
 */
@Getter
@Setter
@ApiModel("留样样品管理分页查询参数")
public class RetentionSampleManagePageQueryDTO extends BasePage {

    @ApiModelProperty("样品编号")
    private String sampleNo;

    @ApiModelProperty("物料ID集合")
    private List<Long> materialIds;

    @ApiModelProperty("批次号")
    private String batchNo;

    @ApiModelProperty("状态：已接收、待销毁、已销毁（不传默认查询全部）")
    private String status;
}
