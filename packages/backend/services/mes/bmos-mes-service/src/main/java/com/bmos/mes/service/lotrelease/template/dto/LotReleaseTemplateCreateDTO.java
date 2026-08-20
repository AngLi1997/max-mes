package com.bmos.mes.service.lotrelease.template.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/8/26 18:53
 */
@Data
public class LotReleaseTemplateCreateDTO {

    private Long categoryId;

    @NotBlank
    private String templateUrl;

    @NotBlank
    @Length(max = 100)
    private String name;

    @NotBlank
    @Length(max = 100)
    private String version;

    private List<Long> deptIds;

    private String remark;
}
