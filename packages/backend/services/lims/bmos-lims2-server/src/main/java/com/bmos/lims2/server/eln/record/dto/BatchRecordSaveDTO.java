package com.bmos.lims2.server.eln.record.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
@ToString
@ApiModel(value = "批记录添加实体类")
public class BatchRecordSaveDTO {

    @ApiModelProperty(value = "分类id")
    @NotNull
    private Long categoryId;

    @ApiModelProperty(value = "记录版本id")
    private Long versionId;

    @ApiModelProperty(value = "记录名称")
    @NotBlank
    private String name;

    @ApiModelProperty(value = "记录编号")
    @NotBlank
    private String code;

    @ApiModelProperty(value = "版本号")
    private String version;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "记录管理表id")
    private Long recordId;

    @ApiModelProperty(value = "存放指令集地址")
    private String filePath;

    @ApiModelProperty("部门id集合")
    private List<Long> deptIds;

    @ApiModelProperty(value = "记录项集合")
    private List<RecordItemListDTO> items;
}
