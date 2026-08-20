package com.bmos.lims2.server.eln.record.dto;

import com.bmos.file.docx.model.DocxFooter;
import com.bmos.file.docx.model.DocxHeader;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
@ApiModel(value = "添加记录项实体类")
public class RecordItemSaveDTO {

    @ApiModelProperty(value = "批记录版本id")
    @NotNull
    private Long recordVersionId;

    @ApiModelProperty(value = "记录项id")
    private Long id;

    @ApiModelProperty(value = "记录项名称")
    private String name;

    @ApiModelProperty(value = "html文件内容")
    private String fileContent;

    @ApiModelProperty(value = "记录项类型")
    private String itemType;

    @ApiModelProperty(value = "文档配置")
    private String pageConfig;

    @ApiModelProperty(value = "记录项源文件路径")
    private String filePath;

    @ApiModelProperty(value = "首页不同")
    private Boolean firstDifferent;

    @ApiModelProperty(value = "奇偶不同")
    private Boolean oddAndEvenDifferent;

    @ApiModelProperty(value = "页码样式")
    private Integer pageNumberStyle;

    @ApiModelProperty(value = "页码起始值")
    private Integer pageStartingNumber;

    @ApiModelProperty(value = "页脚")
    private DocxFooter docxFooter;

    @ApiModelProperty(value = "页眉")
    private DocxHeader docxHeader;

    @ApiModelProperty(value = "文档样式")
    private Boolean style;

    @ApiModelProperty(value = "单个记录项存放指令集地址")
    private String itemPath;

    @ApiModelProperty(value = "记录项最大下标")
    private Integer maxNumber;

}
