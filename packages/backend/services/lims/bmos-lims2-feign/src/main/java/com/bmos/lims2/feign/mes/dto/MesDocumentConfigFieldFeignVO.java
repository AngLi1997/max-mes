package com.bmos.lims2.feign.mes.dto;

import lombok.Data;

@Data
public class MesDocumentConfigFieldFeignVO {
    private Long id;
    private String code;
    private String showName;
    private String dataName;
    private Boolean required;
    private String defaultValue;
    private Integer sort;
}
