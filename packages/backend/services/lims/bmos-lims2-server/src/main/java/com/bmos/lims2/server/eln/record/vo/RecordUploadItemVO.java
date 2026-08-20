package com.bmos.lims2.server.eln.record.vo;


import com.bmos.file.docx.model.DocxFooter;
import com.bmos.file.docx.model.DocxHeader;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class RecordUploadItemVO {

    @ApiModelProperty(value = "html")
    private String fileContent;

    @ApiModelProperty(value = "记录名称")
    private String name;

    @ApiModelProperty(value = "记录类型")
    private String itemType;

    @ApiModelProperty(value = "文档样式")
    private Boolean style;

    @ApiModelProperty(value = "记录项源文件路径")
    private String filePath;

    @ApiModelProperty(value = "首页不同")
    private Boolean firstDifferent = false;

    @ApiModelProperty(value = "奇偶不同")
    private Boolean oddAndEvenDifferent = false;

    @ApiModelProperty(value = "页码样式")
    private Integer pageNumberStyle = 0;

    @ApiModelProperty(value = "页码起始值")
    private Integer pageStartingNumber = 1;

    @ApiModelProperty(value = "页脚")
    private DocxFooter docxFooter = new DocxFooter();

    @ApiModelProperty(value = "页眉")
    private DocxHeader docxHeader = new DocxHeader();

}
