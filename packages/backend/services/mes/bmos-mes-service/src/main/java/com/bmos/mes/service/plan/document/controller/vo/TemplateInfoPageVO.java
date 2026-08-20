package com.bmos.mes.service.plan.document.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 批记录模板信息分页VO
 */
@Getter
@Setter
@ApiModel("批记录模板信息分页VO")
public class TemplateInfoPageVO {

    /**
     * 模板信息id
     */
    @ApiModelProperty("模板信息id")
    private Long id;

    /**
     * 模板信息名称
     */
    @ApiModelProperty("模板信息名称")
    private String name;

    /**
     * 分类名称
     */
    @ApiModelProperty("分类名称")
    private String categoryName;

    /**
     * 当前模板信息绑定的工序id集合
     */
    @ApiModelProperty("当前模板信息绑定的工序id集合")
    private List<Long> processIdList;

}
