package com.bmos.lims2.server.inspect.order.dto;

import com.bmos.lims2.common.enums.InspectionOrderStatusEnum;
import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description: 样品接收分页查询DTO
 * @Author: yigaohui
 * @Date: 2025/01/29 16:30
 */
@Getter
@Setter
@Accessors(chain = true)
@ApiModel("样品接收分页查询条件")
public class SampleReceivePageQueryDTO extends BasePage {

    @ApiModelProperty("检验单号")
    private String orderNo;

    @ApiModelProperty("检品名称")
    private String materialName;

    @ApiModelProperty("检品编码")
    private String materialCode;

    @ApiModelProperty("批次号")
    private String batchNo;


    @ApiModelProperty("创建时间开始")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime inspectionRequestTimeStart;

    @ApiModelProperty("创建时间结束")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime inspectionRequestTimeEnd;

    @ApiModelProperty("检品ID集合")
    private List<Long> materialIds;
}
