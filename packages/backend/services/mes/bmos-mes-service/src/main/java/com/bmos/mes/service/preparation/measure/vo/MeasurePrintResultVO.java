package com.bmos.mes.service.preparation.measure.vo;

import com.bmos.mes.common.enums.preparation.MeasureStageEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel("量取打码结果VO")
public class MeasurePrintResultVO {

    @ApiModelProperty("物料件号")
    private String no;

    @ApiModelProperty("物料总量")
    private BigDecimal quantity;

    @ApiModelProperty("目标量")
    private BigDecimal targetQuantity;

    @ApiModelProperty("未量取")
    private BigDecimal unmeasuredQuantity;

    @ApiModelProperty("已量取")
    private BigDecimal measuredQuantity;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("下次量取阶段")
    private MeasureStageEnum nextMeasureStage;

    @ApiModelProperty("量取结果")
    private List<MeasureResultItem> resultList = new ArrayList<>();

    @Data
    @ApiModel("量取结果")
    public static class MeasureResultItem {
        @ApiModelProperty("量取量")
        private BigDecimal measureQuantity;

        @ApiModelProperty("物料件号")
        private String storageMaterialNo;

        @ApiModelProperty("物料件id")
        private Long storageMateriaId;

        @ApiModelProperty("单位")
        private String unitName;

        @ApiModelProperty("容器名称")
        private String containerName;

        @ApiModelProperty("货位名称")
        private String materialPositionName;

    }

}
