package com.bmos.mes.service.lotrelease.template.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/8/27 15:48
 */
@Data
public class LotReleaseTemplateEditDTO {

    private Long lotReleaseTemplateVersionId;

    @NotBlank
    private String templateUrl;

    private String remark;
}
