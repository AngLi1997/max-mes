package com.bmos.lims2.web.eln.entry.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @Description: 异常批注保存请求VO（单对象）
 * @Author: yigaohui
 * @Date: 2025/12/05 00:00
 */
@Getter
@Setter
@ToString
public class FormDataAnnotationSaveReqVO {

    @ApiModelProperty(value = "请验单id", required = true)
    @NotNull
    private Long inspectionOrderId;

    @NotEmpty
    @ApiModelProperty(value = "批号", required = true)
    private String batchNo;

    @ApiModelProperty(value = "方案id", required = true)
    @NotNull
    private Long schemeId;

    @ApiModelProperty(value = "方案版id", required = true)
    @NotNull
    private Long schemeVersionId;

    @ApiModelProperty(value = "记录项id", required = true)
    @NotNull
    private Long recordItemId;

    @ApiModelProperty(value = "方法id", required = true)
    private Long recordId;

    @ApiModelProperty(value = "任务id", required = true)
    @NotNull
    private Long taskId;

    @ApiModelProperty(value = "检验项目Id")
    private Long itemId;

    @ApiModelProperty(value = "检验项目配置id")
    private Long itemConfigId;

    @ApiModelProperty(value = "检验分析项id")
    private Long parameterId;

    @ApiModelProperty(value = "检验分析项配置id")
    private Long parameterConfigId;

    @ApiModelProperty(value = "方法版本id", required = true)
    @NotNull
    private Long recordVersionId;

    @NotEmpty
    @ApiModelProperty(value = "批注值", required = true)
    private String value;

    @ApiModelProperty(value = "批注值扩展")
    private String valueExtension;

    @ApiModelProperty(value = "组件id", required = true)
    @NotNull
    private Long fieldId;

    @NotEmpty
    @ApiModelProperty(value = "组件类型", required = true)
    private String componentType;

    @NotNull
    @ApiModelProperty(value = "操作时间", required = true)
    private LocalDateTime operationTime;

    @ApiModelProperty(value = "备注")
    private String remark;
}


