package com.bmos.mes.service.record.dto;

import com.bmos.file.docx.model.DocxFooter;
import com.bmos.file.docx.model.DocxHeader;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@ApiModel(value = "新增记录添加记录项实体类")
public class RecordItemListDTO {

    @ApiModelProperty(value = "记录项")
    @NotBlank
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

    @ApiModelProperty(value = "记录项id")
    private Long id;
}
