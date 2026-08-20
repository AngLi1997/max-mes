package com.bmos.mes.service.ingredient.weigh.dto;

import com.bmos.common.validate.EnumValidate;
import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.mes.common.enums.ingredient.WeighType;
import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("称量日志查询DTO")
@Data
public class WeighLogQueryDTO extends BasePage {

    @ApiModelProperty("物料类型")
    @EnumValidate(value = CategoryInfoTypeEnum.class)
    private Integer categoryType;

    @ApiModelProperty("物料id")
    private Long materialId;

    @ApiModelProperty("物料批号")
    private String materialBatchNo;

    @ApiModelProperty("物料件号")
    private String materialNo;

    @ApiModelProperty("称量类型")
    @EnumValidate(WeighType.class)
    private Integer weighType;

    @ApiModelProperty("秤具信息")
    private String equipmentInfo;

    @ApiModelProperty("产品信息")
    private String productInfo;

    @ApiModelProperty("生产批号")
    private String productBatchNo;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;


}
