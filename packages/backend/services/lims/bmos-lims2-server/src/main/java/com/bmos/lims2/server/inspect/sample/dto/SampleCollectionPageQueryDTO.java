package com.bmos.lims2.server.inspect.sample.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description: 样品领取列表分页查询DTO
 * @Author: yigaohui
 * @Date: 2025/01/29 17:00
 */
@Getter
@Setter
@ApiModel(value = "样品领取列表分页查询参数")
public class SampleCollectionPageQueryDTO extends BasePage {

    @ApiModelProperty("检验项目ID")
    private Long inspectItemId;

    @ApiModelProperty("检验项目名称")
    private String inspectItemName;

    @ApiModelProperty("样品编号")
    private String sampleNo;

    @ApiModelProperty("检验单号")
    private String orderNo;

    @ApiModelProperty("检品名称")
    private String materialName;


    @ApiModelProperty("检品ID")
    private List<Long> materialIds;

    @ApiModelProperty("批次号")
    private String batchNo;


    @ApiModelProperty("请验开始时间")
    private LocalDateTime inspectionRequestTimeStart;

    @ApiModelProperty("请验结束时间")
    private LocalDateTime inspectionRequestTimeEnd;

}
