package com.bmos.lims2.server.operate.dto.version;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author renjinguang
 */
@Setter
@Getter
@ToString
@ApiModel(value = "新增版本参数dto")
public class UpdateVersionDTO {

    @ApiModelProperty("主键id")
    @NotNull
    private Long id;

    @ApiModelProperty("主表id")
    @NotNull
    private Long operateId;

    @ApiModelProperty("文件上传地址")
    @NotBlank
    private String url;

    @ApiModelProperty("文件上传时间")
    private LocalDateTime uploadTime;

    @ApiModelProperty("线下文件生效日期")
    private String fileEffectDate;

    @ApiModelProperty("版本号")
    @NotBlank
    private String version;

    @ApiModelProperty("描述")
    private String remark;
}
