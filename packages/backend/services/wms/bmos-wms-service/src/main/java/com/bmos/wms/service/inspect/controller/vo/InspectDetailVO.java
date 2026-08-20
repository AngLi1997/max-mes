package com.bmos.wms.service.inspect.controller.vo;

import com.bmos.wms.common.enums.inspect.InspectStatusEnum;
import com.bmos.wms.common.enums.inspect.MaterialQualityStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ApiModel("WMS 检验单详情VO")
public class InspectDetailVO {

    @ApiModelProperty("检验单id")
    private Long id;

    @ApiModelProperty("LIMS 检验单号")
    private String inspectNo;

    @ApiModelProperty("状态")
    private InspectStatusEnum status;

    @ApiModelProperty("汇总检验结果")
    private MaterialQualityStatusEnum inspectResult;

    @ApiModelProperty("退回原因 / 重发起原因")
    private String reason;

    @ApiModelProperty("货品id")
    private Long cargoId;

    @ApiModelProperty("货品名称")
    private String cargoName;

    @ApiModelProperty("货品合并编码")
    private String mergeCode;

    @ApiModelProperty("货品批号")
    private String materialBatchNo;

    @ApiModelProperty("原厂批号")
    private String factoryBatchNo;

    @ApiModelProperty("单位id")
    private Long unitId;

    @ApiModelProperty("请验单配置id")
    private Long inspectConfigId;

    @ApiModelProperty("检验方案id")
    private Long schemeId;

    @ApiModelProperty("检验方案版本id")
    private Long schemeVersionId;

    @ApiModelProperty("请验人")
    private String inspector;

    @ApiModelProperty("请验时间")
    private LocalDateTime inspectTime;

    @ApiModelProperty("请验单字段详情")
    private List<InspectInfoVO> inspectInfoVOList;

    @ApiModelProperty("检验项结果列表")
    private List<InspectProgramResultVO> inspectProgramResultVOList;
}
