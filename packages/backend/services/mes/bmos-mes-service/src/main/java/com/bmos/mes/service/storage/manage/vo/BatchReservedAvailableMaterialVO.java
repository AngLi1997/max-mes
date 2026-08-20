package com.bmos.mes.service.storage.manage.vo;

import com.bmos.mes.service.product.model.MaterialExpandInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ApiModel("批次下可预订及已预订的物料VO")
@Data
public class BatchReservedAvailableMaterialVO {

    @ApiModelProperty("物料id")
    private Long id;

    @ApiModelProperty("物料批号")
    private String materialBatchNo;

    @ApiModelProperty("物料件号")
    private String materialNo;

    @ApiModelProperty("暂存物料件id")
    private String storageMaterialId;

    @ApiModelProperty("预定量")
    private BigDecimal reserveQuantity;

    @ApiModelProperty("物料量")
    private BigDecimal quantity;

    @ApiModelProperty("理论量")
    private BigDecimal theoreticalQuantity;

    @ApiModelProperty("单位id")
    private Long unitId;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("货位名")
    private String materialPositionName;

    @ApiModelProperty("货位编码")
    private String materialPositionCode;

    @ApiModelProperty("有效日期")
    private String expiredDate;

    @ApiModelProperty("拓展单位id")
    private Long unitExtendId;

    @ApiModelProperty("物料拓展信息")
    private MaterialExpandInfo materialExpandInfo;

    @ApiModelProperty("水分 无则0")
    @NotNull
    private BigDecimal hydration;

    @ApiModelProperty("含量 无或大于100则100")
    @NotNull
    private BigDecimal noHydrationContent;

    @ApiModelProperty("是否已预定")
    private Boolean reserved;

    @ApiModelProperty("供应商")
    private String supplier;

    @ApiModelProperty("生产商")
    private String producer;

}
