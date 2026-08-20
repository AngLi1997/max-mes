package com.bmos.mes.service.operate.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author renjinguang
 */
@Setter
@Getter
@ToString
@ApiModel(value = "新增文件dto")
public class SaveOperateRuleDTO {

    @ApiModelProperty("文件名称")
    @NotBlank
    private String name;

    @ApiModelProperty("文件编号")
    @NotBlank
    private String code;

    @ApiModelProperty("分类id")
    @NotNull
    private Long categoryId;

    @ApiModelProperty("文件上传地址")
    @NotBlank
    private String url;

    @ApiModelProperty("文件上传时间")
    @NotBlank
    private LocalDateTime uploadTime;

    @ApiModelProperty("线下文件生效日期")
    private String fileEffectDate;

    @ApiModelProperty("版本号")
    @NotBlank
    private String version;

    @ApiModelProperty("描述")
    private String remark;

    @ApiModelProperty("部门id集合")
    private List<Long> deptIds;




}

