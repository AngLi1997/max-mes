package com.bmos.mes.service.process.dto.query;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author renjinguang
 */
@Setter
@Getter
@ToString
@ApiModel("查询工艺审批填报dto")
public class ProcessConfirmQueryDTO extends BasePage {

    @ApiModelProperty("产品分类id")
    private Long productCategoryId;

    @ApiModelProperty("工艺名称")
    private String processName;

    @ApiModelProperty("生产批号")
    private String planBatchNo;

    @ApiModelProperty("审批结论")
    private String confirmOpinion;

    @ApiModelProperty("产品id")
    private Long productId;

    @ApiModelProperty(hidden = true)
    private List<Long> productIdList;
}
