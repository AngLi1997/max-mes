package com.bmos.file.docx.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

@Getter
@Setter
@ToString
public class DocxOutlineInfo {


    @ApiModelProperty(value = "大纲名称")
    private String markName;

    @ApiModelProperty(value = "Html文件流")
    private String fileContent;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "true:竖版，false:横版")
    private Boolean style;

    @ApiModelProperty(value = "源文档")
    @JsonIgnore
    private XWPFDocument srcDoc;

    @ApiModelProperty(value = "页脚")
    private DocxFooter docxFooter = new DocxFooter();

    @ApiModelProperty(value = "页眉")
    private DocxHeader docxHeader = new DocxHeader();

    @ApiModelProperty(value = "首页不同")
    private Boolean firstDifferent = false;

    @ApiModelProperty(value = "奇偶不同")
    private Boolean oddAndEvenDifferent = false;

    @ApiModelProperty(value = "页码样式")
    private Integer pageNumberStyle = 0;

    @ApiModelProperty(value = "页码起始值")
    private Integer pageStartingNumber = 1;
}
