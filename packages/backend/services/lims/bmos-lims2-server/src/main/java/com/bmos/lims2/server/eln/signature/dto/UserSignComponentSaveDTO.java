package com.bmos.lims2.server.eln.signature.dto;

import com.bmos.lims2.server.eln.entry.dto.BusinessDataHandleBaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * 用户手写签名保存DTO
 */
@Getter
@Setter
@ApiModel("用户手写签名保存DTO")
public class UserSignComponentSaveDTO extends BusinessDataHandleBaseDTO {

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", required = true)
    @NotEmpty
    private String userId;

    /**
     * 复核人id
     */
    @ApiModelProperty(value = "复核人id")
    private String reviewUserId;
}