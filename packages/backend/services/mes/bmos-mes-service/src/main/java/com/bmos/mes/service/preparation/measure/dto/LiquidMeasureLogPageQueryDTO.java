package com.bmos.mes.service.preparation.measure.dto;

import com.bmos.common.validate.EnumValidate;
import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.mes.common.enums.preparation.MeasureTypeEnum;
import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("配液量取日志查询DTO")
@Data
public class LiquidMeasureLogPageQueryDTO extends BasePage {

    @ApiModelProperty("物料类型")
    @EnumValidate(value = CategoryInfoTypeEnum.class)
    private Integer categoryType;

    @ApiModelProperty("物料id")
    private Long materialId;

    @ApiModelProperty("物料批号")
    private String materialBatchNo;

    @ApiModelProperty("物料件号")
    private String materialNo;

    @ApiModelProperty("量取类型")
    @EnumValidate(MeasureTypeEnum.class)
    private Integer measureType;

    @ApiModelProperty("产品信息")
    private String productInfo;

    @ApiModelProperty("生产批号")
    private String productBatchNo;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

}
