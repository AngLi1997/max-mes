package com.bmos.mes.service.product.dto;

import com.bmos.mes.service.product.model.MaterialExpandInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ApiModel("生产物料编辑DTO")
public class ProductMaterialUpdateDTO {
    @ApiModelProperty(value = "id", required = true)
    @NotNull
    private Long id;

    /**
     * 拓展单位id
     */
    @ApiModelProperty(value = "拓展单位id", required = true)
    private Long unitExtendId;

    @ApiModelProperty(value = "是否是成品",required = true)
    private Boolean finishProduct;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "生产周期(天)")
    private Integer productionCycle;

    @ApiModelProperty(value = "内包规格")
    private String innerPackingSpecification;

    @ApiModelProperty(value = "包装规格")
    private String packingSpecification;

    @ApiModelProperty(value = "拓展信息")
    private MaterialExpandInfo expandInfo;

    @ApiModelProperty("产品标识")
    private String productMark;

    @ApiModelProperty("临期天数")
    private Integer dyingPeriod;

    @ApiModelProperty("自定义字段DTO")
    private List<MaterialFieldSaveDTO> fieldSaveDTOList;

}
