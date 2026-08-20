package com.bmos.mes.service.preparation.input.controller.vo;


import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * 配液单简单信息
 */
@Getter
@Setter
@ApiModel("配液单VO")
public class PreparationPlanItemVO {

    /**
     * 配液单id
     */
    private Long id;

    /**
     * 配液单名称
     */
    private String name;

}
