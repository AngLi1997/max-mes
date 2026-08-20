package com.bmos.lims2.feign.mes.dto;

import lombok.Data;

@Data
public class MesSchemeFeignVO {
    private Long schemeId;
    private Long schemeVersionId;
    private String name;
    private String displayName;
}
