package com.bmos.mes.service.record.vo;

import com.bmos.file.docx.model.DocxFooter;
import com.bmos.file.docx.model.DocxHeader;
import com.bmos.mes.common.enums.record.RecordItemTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author renjinguang
 */
@Getter
@Setter
@ToString
@ApiModel(value = "记录项vo")
public class RecordItemVO {

    @ApiModelProperty(value = "记录项id")
    private Long id;

    @ApiModelProperty(value = "业务id——itemId")
    private Long itemId;

    @ApiModelProperty(value = "记录版本id")
    private Long recordVersionId;

    @ApiModelProperty(value = "记录项名称")
    private String name;

    @ApiModelProperty(value = "html文件地址")
    private String fileContent;

    @ApiModelProperty(value = "记录项类型")
    private String itemType;

    @ApiModelProperty(value = "文档样式")
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
}
