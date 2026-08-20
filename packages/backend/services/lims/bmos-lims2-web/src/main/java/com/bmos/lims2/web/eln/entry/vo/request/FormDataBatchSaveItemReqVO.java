package com.bmos.lims2.web.eln.entry.vo.request;

import com.bmos.web.validation.InsertValidation;
import com.bmos.web.validation.UpdateValidation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @Description: 批量保存-数据项请求VO
 * @Author: yigaohui
 * @Date: 2025/11/20 00:00
 */
@Getter
@Setter
@ToString
public class FormDataBatchSaveItemReqVO {

    @NotEmpty(groups = {InsertValidation.class, UpdateValidation.class})
    @ApiModelProperty(value = "数据值", required = true)
    private String value;

    @ApiModelProperty(value = "数据值扩展（如checkbox的所有值）", required = true)
    private String valueExtension;

    @ApiModelProperty(value = "组件id", required = true)
    @NotNull(groups = {InsertValidation.class, UpdateValidation.class})
    private Long fieldId;

    @NotEmpty(groups = {InsertValidation.class, UpdateValidation.class})
    @ApiModelProperty(value = "组件类型", required = true)
    private String componentType;

    @NotNull(groups = {InsertValidation.class, UpdateValidation.class})
    @ApiModelProperty(value = "操作时间", required = true)
    private LocalDateTime operationTime;

    @NotEmpty(groups = InsertValidation.class)
    @ApiModelProperty(value = "操作人", required = true)
    private String operationUser;

    @ApiModelProperty(value = "复核人")
    private String reviewUser;

    @ApiModelProperty(value = "复核时间")
    private LocalDateTime reviewTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否是空值")
    private Boolean emptyValue;
}


