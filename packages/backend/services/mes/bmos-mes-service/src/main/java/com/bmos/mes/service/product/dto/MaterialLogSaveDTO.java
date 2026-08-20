package com.bmos.mes.service.product.dto;

import com.bmos.mes.service.product.model.MaterialExpandInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@ApiModel("物料日志保存DTO")
@Getter
@Setter
public class MaterialLogSaveDTO {

    @ApiModelProperty("操作时间")
    private LocalDateTime operationTime;

    @ApiModelProperty("操作类型")
    private Integer operationType;

    @ApiModelProperty("具体操作")
    private Integer specificOperationType;

    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("物料编码(合并)")
    private String materialCode;

    @ApiModelProperty("物料批号")
    private String materialBatchNo;

    @ApiModelProperty("物料件号")
    private String materialNo;

    @ApiModelProperty("预定量")
    private String scheduled;

    @ApiModelProperty("可用量")
    private String available;

    @ApiModelProperty("皮重")
    private String tareWeight;

    @ApiModelProperty("毛重")
    private String grossWeight;

    @ApiModelProperty("单位")
    private String unitName;

    @ApiModelProperty("有效期")
    private String expirationTime;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("生产批号")
    private String batchNo;

    @ApiModelProperty("暂存货位")
    private String materialPositionName;

    @ApiModelProperty("货位编码")
    private String materialPositionCode;

    @ApiModelProperty("所属位置")
    private String materialPositionPath;

    @ApiModelProperty("检验id")
    private Long inspectId;

    @ApiModelProperty("物料拓展信息")
    private MaterialExpandInfo expandInfo;

    @ApiModelProperty("原始编码")
    private String originalCode;

    @ApiModelProperty("原厂编号")
    private String originalNo;

}
