package com.bmos.lims2.web.eln.entry.vo.request;

import com.bmos.web.validation.InsertValidation;
import com.bmos.web.validation.UpdateValidation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description: 批量保存请求VO（字段去掉inspect前缀）
 * @Author: yigaohui
 * @Date: 2025/11/20 00:00
 */
@Getter
@Setter
@ToString
public class FormDataBatchSaveReqVO {

    @ApiModelProperty(value = "请验单id", required = true)
    @NotNull(groups = {InsertValidation.class, UpdateValidation.class})
    private Long inspectionOrderId;

    @NotEmpty(groups = {InsertValidation.class, UpdateValidation.class})
    @ApiModelProperty(value = "批号", required = true)
    private String batchNo;

    @ApiModelProperty(value = "方案id", required = true)
    @NotNull(groups = {InsertValidation.class, UpdateValidation.class})
    private Long schemeId;

    @ApiModelProperty(value = "方案版id", required = true)
    @NotNull(groups = {InsertValidation.class, UpdateValidation.class})
    private Long schemeVersionId;

    @ApiModelProperty(value = "记录项id", required = true)
    @NotNull(groups = {InsertValidation.class, UpdateValidation.class})
    private Long recordItemId;

    @ApiModelProperty(value = "方法id", required = true)
    private Long recordId;

    @ApiModelProperty(value = "任务id", required = true)
    @NotNull(groups = {InsertValidation.class, UpdateValidation.class})
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
    @NotNull(groups = {InsertValidation.class, UpdateValidation.class})
    private Long recordVersionId;

    @ApiModelProperty(value = "数据集", required = true)
    @NotEmpty(groups = {InsertValidation.class, UpdateValidation.class})
    @Valid
    private List<FormDataBatchSaveItemReqVO> items;
}


