package com.bmos.mes.service.weigh.centre.config.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 编辑称量中心DTO
 * @author liang
 * @version 1.0.0
 * @date 2024/7/3 17:09
 */
@Data
@ApiModel("编辑称量中心DTO")
public class WeighCentreEditDTO {

    @ApiModelProperty(value = "称量中心id", example = "1", required = true)
    @NotNull
    private Long id;

    @ApiModelProperty(value = "称量中心名称", example = "称量中心名称", required = true)
    @NotBlank
    @Length(max = 100)
    private String name;

    @ApiModelProperty(value = "称量中心编码", example = "KQ-PY-101", required = true)
    @NotBlank
    @Length(max = 100)
    private String code;

    @ApiModelProperty(value = "部门授权id列表", required = true)
    @NotEmpty
    private List<Long> deptIds;

    @ApiModelProperty(value = "备注", example = "备注")
    @Length(max = 200)
    private String remark;
}
