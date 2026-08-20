package com.bmos.wms.service.storage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 编辑货位参数
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/5 11:51
 */
@Data
@ApiModel("编辑货位参数")
public class MaterialPositionEditDTO {


    @ApiModelProperty(value = "货位id", example = "1", required = true)
    @NotNull
    private Long id;

    /**
     * 暂存货位
     */
    @ApiModelProperty(value = "暂存货位", example = "培养室-盐酸组氨酸货位", required = true)
    @NotBlank
    @Length(max = 100)
    private String position;

    /**
     * 货位编码
     */
    @ApiModelProperty(value = "货位编码", example = "KQ-PY-101", required = true)
    @NotBlank
    @Length(max = 100)
    private String code;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", example = "备注")
    @Length(max = 200)
    private String remark;

    /**
     * 部门授权id列表
     */
    @ApiModelProperty(value = "部门授权id列表", required = true)
    @NotEmpty
    private List<Long> deptIds;
}
