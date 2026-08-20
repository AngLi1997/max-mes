package com.bmos.mes.service.lotrelease.template.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/8/26 18:53
 */
@Data
public class LotReleaseTemplateVersionCreateDTO {

    @NotNull
    private Long templateId;

    @NotBlank
    private String templateUrl;

    @NotBlank
    @Length(max = 100)
    private String version;

    @Length(max = 200)
    private String remark;
}
