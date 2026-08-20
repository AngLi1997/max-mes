package com.bmos.mes.service.process.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel("历史工序VO")
public class HistoricVO {
    private Long id;

    private String name;
}
