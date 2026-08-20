package com.bmos.mes.service.preparation.measure.vo;

import com.bmos.mes.service.storage.manage.vo.MaterialBatchFieldVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@ApiModel("配液量取物料件VO")
@Builder
@Getter
public class LiquidMeasureMaterialPieceVO {

    @ApiModelProperty("物料件id")
    private Long id;

    @ApiModelProperty("物料合并编码")
    private String materialMergeCode;

    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("物料批号")
    private String materialBatchNo;

    @ApiModelProperty(value = "生产日期", example = "2024-03-29")
    private LocalDate produceDate;

    @ApiModelProperty("物料件号")
    private String materialNo;

    @ApiModelProperty("物料量")
    private BigDecimal materialQuantity;

    @ApiModelProperty("单位id")
    private Long unitId;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("有效期至")
    private LocalDate expiredDate;

    @ApiModelProperty("供应商")
    private String supplier;

    @ApiModelProperty("生产商")
    private String producer;

    @ApiModelProperty("原产批号")
    private String originalBatchNo;

    @ApiModelProperty("原始编码")
    private String originalCode;

    @ApiModelProperty("自定义字段信息")
    private List<MaterialBatchFieldVO> fieldList;


}
