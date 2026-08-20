package com.bmos.lims2.server.eln.record.dto;

import com.bmos.file.docx.model.DocxFooter;
import com.bmos.file.docx.model.DocxHeader;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(value = "保存组件实体类")
public class RecordComponentSaveDTO {

    @ApiModelProperty(value = "记录项业务id")
    @NotNull
    private Long itemId;

    @ApiModelProperty(value = "记录项排序号")
    private Integer sort;

    @ApiModelProperty(value = "记录项名称")
    private String name;

    @ApiModelProperty(value = "记录id")
    @NotNull
    private Long id;

    @ApiModelProperty(value = "版本id")
    @NotNull
    private Long recordVersionId;

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
