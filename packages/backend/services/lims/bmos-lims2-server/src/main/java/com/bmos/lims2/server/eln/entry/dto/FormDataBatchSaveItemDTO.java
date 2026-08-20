package com.bmos.lims2.server.eln.entry.dto;

import com.bmos.web.validation.InsertValidation;
import com.bmos.web.validation.UpdateValidation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class FormDataBatchSaveItemDTO {

    /**
     * 数据值
     */
    @NotEmpty(groups = {InsertValidation.class, UpdateValidation.class})
    @ApiModelProperty(value = "数据值",required = true)
    private String value;

    /**
     * 数据值扩展
     */
    @ApiModelProperty(value = "数据值扩展（如checkbox的所有值）",required = true)
    private String valueExtension;

    /**
     * 组件id
     */
    @ApiModelProperty(value = "组件id",required = true)
    @NotNull(groups = {InsertValidation.class, UpdateValidation.class})
    private Long fieldId;

    /**
     * 组件类型
     */
    @NotEmpty(groups = {InsertValidation.class, UpdateValidation.class})
    @ApiModelProperty(value = "组件类型",required = true)
    private String componentType;

    /**
     * 操作时间
     */
    @NotNull(groups = {InsertValidation.class, UpdateValidation.class})
    @ApiModelProperty(value = "操作时间",required = true)
    private LocalDateTime operationTime;

    /**
     * 操作人
     */
    @NotEmpty(groups = InsertValidation.class)
    @ApiModelProperty(value = "操作人",required = true)
    private String operationUser;

    /**
     * 复核人
     */
    @ApiModelProperty(value = "复核人")
    private String reviewUser;

    /**
     * 复核时间
     */
    @ApiModelProperty(value = "复核时间")
    private LocalDateTime reviewTime;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否是空值")
    private Boolean emptyValue;

}
