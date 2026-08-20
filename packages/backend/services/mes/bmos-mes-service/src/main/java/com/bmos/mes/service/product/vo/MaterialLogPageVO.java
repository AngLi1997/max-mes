package com.bmos.mes.service.product.vo;


import com.bmos.mes.common.enums.material.MaterialOperationTypeEnum;
import com.bmos.mes.common.enums.material.MaterialQualityStatusEnum;
import com.bmos.mes.common.enums.material.MaterialSpecificOperationTypeEnum;
import com.bmos.mes.service.product.model.MaterialExpandInfo;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@ApiModel("物料日志分页VO")
@Getter
@Setter
public class MaterialLogPageVO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("操作时间")
    private LocalDateTime operationTime;

    @ApiModelProperty("操作类型")
    @ApiModelEnumProperty(value = "操作类型", enumClass = MaterialOperationTypeEnum.class)
    private MaterialOperationTypeEnum operationType;

    @ApiModelProperty("具体操作")
    @ApiModelEnumProperty(value = "具体操作", enumClass = MaterialSpecificOperationTypeEnum.class)
    private MaterialSpecificOperationTypeEnum specificOperationType;

    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("操作人员名称")
    private String userName;

    @ApiModelProperty("操作人员账号")
    private String loginName;

    @ApiModelProperty("物料编码(合并)")
    private String materialCode;

    @ApiModelProperty("产品物料编码(合并)")
    private String productMergeCode;

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
    private String inspectId;

    @ApiModelProperty("物料拓展信息")
    private MaterialExpandInfo expandInfo;

    @ApiModelProperty("原始编码")
    private String originalCode;

    @ApiModelProperty("原厂编号")
    private String originalNo;

    @ApiModelProperty("请验单号")
    private String requestVerifyNo;

    @ApiModelProperty("报告单号")
    private String reportNo;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("物料状态")
    private Boolean enable;

    /**
     * 供应商
     */
    @ApiModelProperty("供应商")
    private String supplier;

    /**
     * 生产商
     */
    @ApiModelProperty("生产商")
    private String producer;

    @ApiModelProperty("游标")
    private Long searchAfter;

    @ApiModelProperty("质量件状态")
    private MaterialQualityStatusEnum qualityStatus;

}
