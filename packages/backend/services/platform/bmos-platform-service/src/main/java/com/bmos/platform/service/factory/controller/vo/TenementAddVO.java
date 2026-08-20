package com.bmos.platform.service.factory.controller.vo;

import com.bmos.web.validation.InsertValidation;
import com.bmos.web.validation.UpdateValidation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * @className: TenementAddVO
 * @author: yigaohui
 * @date: 2024/12/30 13:37
 * @Version: 1.0
 * @description:
 */
@Data
@ApiModel("楼宇添加VO")
public class TenementAddVO {
    /**
     * 标签编码
     */
    @NotEmpty
    @ApiModelProperty("楼宇编码")
    private String code;
    /**
     * tag名称
     */
    @NotEmpty
    @ApiModelProperty("楼宇名称")
    private String name;

    @ApiModelProperty("父级id")
    private Long parentId;
}
