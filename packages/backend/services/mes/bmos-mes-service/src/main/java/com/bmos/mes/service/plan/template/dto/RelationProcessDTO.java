package com.bmos.mes.service.plan.template.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel("生产计划模板关联工艺DTO")
@Data
public class RelationProcessDTO {

    private String activeVersion;

    private Long id;

    private String name;

    private String showName;

    private Integer val;

    private String reallyId;

}
