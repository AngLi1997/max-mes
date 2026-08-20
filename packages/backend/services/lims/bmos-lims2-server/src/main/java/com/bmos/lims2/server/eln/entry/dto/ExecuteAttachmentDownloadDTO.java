package com.bmos.lims2.server.eln.entry.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @Description: 附件下载请求DTO
 * @Author: yigaohui
 * @Date: 2026/01/09 00:00
 */
@Getter
@Setter
@ApiModel("附件下载请求参数")
public class ExecuteAttachmentDownloadDTO {

    @NotBlank(message = "附件路径不能为空")
    @ApiModelProperty(value = "附件路径", required = true)
    private String path;
}

