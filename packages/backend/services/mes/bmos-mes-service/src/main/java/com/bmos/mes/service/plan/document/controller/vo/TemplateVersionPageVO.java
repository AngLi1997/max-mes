package com.bmos.mes.service.plan.document.controller.vo;

import com.bmos.common.validate.EnumValidate;
import com.bmos.mes.common.enums.ingredient.WeighSignStatus;
import com.bmos.mes.common.enums.plan.TemplateVersionStatusEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 分页查询批记录模板版本VO
 */
@Getter
@Setter
@ApiModel("批记录模板版本分页VO")
public class TemplateVersionPageVO {

    /**
     * 版本id
     */
    @ApiModelProperty("版本id")
    private Long id;

    /**
     * 版本
     */
    @ApiModelProperty("版本")
    private String version;

    /**
     * 状态
     */
    @ApiModelEnumProperty(value = "状态", enumClass = TemplateVersionStatusEnum.class)
    @EnumValidate(TemplateVersionStatusEnum.class)
    private TemplateVersionStatusEnum status;

    @ApiModelProperty(value = "是否默认")
    private Boolean normal;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

}
