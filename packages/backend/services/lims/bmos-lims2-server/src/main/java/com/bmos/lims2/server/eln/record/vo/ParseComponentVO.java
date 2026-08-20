package com.bmos.lims2.server.eln.record.vo;

import com.bmos.file.docx.model.DocxFooter;
import com.bmos.file.docx.model.DocxHeader;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(value = "工艺配置批记录配置VO")
public class ParseComponentVO {

    @ApiModelProperty(value = "记录项id")
    private Long id;

    @ApiModelProperty(value = "记录项文档内容")
    private String fileContent;

    @ApiModelProperty(value = "文档样式")
    private String pageConfig;

    @ApiModelProperty(value = "组件集合")
    private List<ComponentListVO> componentList = new ArrayList<>();

    @ApiModelProperty(value = "文档样式")
    private Boolean style;

    @ApiModelProperty(value = "首页不同")
    private Boolean firstDifferent = false;

    @ApiModelProperty(value = "奇偶不同")
    private Boolean oddAndEvenDifferent = false;

    @ApiModelProperty(value = "页码样式")
    private Integer pageNumberStyle = 0;

    @ApiModelProperty(value = "页脚")
    private DocxFooter docxFooter = new DocxFooter();

    @ApiModelProperty(value = "页眉")
    private DocxHeader docxHeader = new DocxHeader();

    @ApiModelProperty(value = "记录项业务id")
    private Long itemId;

    @ApiModelProperty(value = "记录项排序字段")
    private Integer sort;

    @ApiModelProperty(value = "记录项名称")
    private String name;

    @ApiModelProperty(value = "记录项最大下标")
    private Integer maxNumber;

    @ApiModelProperty(value = "记录项类型")
    private String itemType;
}
