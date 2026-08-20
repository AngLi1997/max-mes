package com.bmos.mes.service.plan.document.service.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 批记录模板验证DTO
 */
@Getter
@Setter
@ApiModel("批记录模板验证DTO")
public class TemplateVerifyDTO extends TemplateVersionOperateDTO {

    /**
     * 选择的生产计划id
     */
    private Long planId;

    /**
     * 当前排序
     */
    private List<Long> sortPlanIdList;
}
