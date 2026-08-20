package com.bmos.mes.service.storage.manage.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@ApiModel("组件投料列表VO")
@Data
public class ComponentChargeListVO {

    @ApiModelProperty("物料id")
    private Long materialId;

    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("物料编码")
    private String materialMergeCode;

    @ApiModelProperty("物料规格")
    private String specification;

    @ApiModelProperty("批次信息列表")
    private List<ChargeBatchInfo> chargeBatchInfoList;

    @Data
    public static class ChargeBatchInfo {

        @ApiModelProperty("物料批号")
        private String materialBatchNo;

        @ApiModelProperty("物料批次id")
        private Long materialBatchId;

        @ApiModelProperty("投料量")
        private BigDecimal quantity;

        @ApiModelProperty("单位id")
        private Long unitId;

        @ApiModelProperty("单位名称")
        private String unitName;

    }

}
