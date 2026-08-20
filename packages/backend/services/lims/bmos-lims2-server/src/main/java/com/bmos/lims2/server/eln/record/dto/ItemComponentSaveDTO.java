package com.bmos.lims2.server.eln.record.dto;

import com.bmos.file.docx.model.DocxFooter;
import com.bmos.file.docx.model.DocxHeader;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel("单个记录项和组件保存DTO")
@Data
public class ItemComponentSaveDTO {

    @ApiModelProperty("记录版本id")
    @NotNull
    private Long recordVersionId;

    @ApiModelProperty("记录id")
    @NotNull
    private Long id;

    @ApiModelProperty(value = "记录项业务id")
    @NotNull
    private Long itemId;

    @ApiModelProperty(value = "html文件")
    @NotBlank
    private String fileContent;

    @ApiModelProperty(value = "记录项最大下标")
    private Integer maxNumber;

    @ApiModelProperty(value = "版本样式")
    private String pageConfig;

    @ApiModelProperty(value = "组件集合")
    private List<ComponentListDTO> componentList;

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
