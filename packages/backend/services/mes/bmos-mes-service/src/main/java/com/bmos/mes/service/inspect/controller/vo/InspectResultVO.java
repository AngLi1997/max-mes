package com.bmos.mes.service.inspect.controller.vo;

import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.mes.common.enums.material.MaterialQualityStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ApiModel("请验结果详情")
public class InspectResultVO {

    /**
     * 产品名称
     */
    @ApiModelProperty("产品名称")
    private String productName;

    /**
     * 产品编码
     */
    @ApiModelProperty("产品编码")
    private String productMergeCode;

    /**
     * 指令单编号
     */
    @ApiModelProperty("指令单编号")
    private String planNo;

    /**
     * 生产批号
     */
    @ApiModelProperty("生产批号")
    private String batchNo;

    /**
     * 物料类型
     */
    @ApiModelProperty("物料类型")
    private CategoryInfoTypeEnum materialType;

    /**
     * 物料信息
     */
    @ApiModelProperty("物料信息")
    private String materialName;

    /**
     * 物料批号
     */
    @ApiModelProperty("物料批号")
    private String materialBatchNo;

    /**
     * 请验时间
     */
    @ApiModelProperty("请验时间")
    private LocalDateTime inspectTime;

    /**
     * 请验单号
     */
    @ApiModelProperty("请验单号")
    private String inspectNo;

    /**
     * 请验结果
     */
    @ApiModelProperty("请验结果")
    private MaterialQualityStatusEnum inspectResult;

    /**
     * 检验项结果
     */
    @ApiModelProperty("检验项结果")
    private List<InspectProgramResultVO> inspectProgramResultVOList;

}
