package com.bmos.lims2.server.eln.record.vo;

import cn.hutool.core.util.StrUtil;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.file.docx.model.DocxFooter;
import com.bmos.file.docx.model.DocxHeader;
import com.bmos.lims2.common.enums.RecordItemTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(value = "查询记录项list")
public class RecordItemListVO {

    @ApiModelProperty(value = "记录id")
    private Long id;

    @ApiModelProperty(value = "记录名称")
    private String recordName;

    @ApiModelProperty(value = "记录版本id")
    private Long recordVersionId;

    @ApiModelProperty(value = "记录项业务id")
    private Long itemId;

    @ApiModelProperty(value = "记录项排序字段")
    private Integer sort;

    @ApiModelProperty(value = "记录项名称")
    private String name;

    @ApiModelProperty(value = "html文件地址")
    private String fileContent;

    @ApiModelProperty(value = "记录项最大下标")
    private Integer maxNumber;

    @ApiModelProperty(value = "记录项类型")
    private RecordItemTypeEnum itemType;

    @ApiModelProperty(value = "文档配置")
    private String pageConfig;

    @ApiModelProperty(value = "组件集合")
    private List<ComponentListVO> componentList;

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

    @JsonIgnore
    private String docxFooterJson;

    @ApiModelProperty(value = "页眉")
    private DocxHeader docxHeader;

    @JsonIgnore
    private String docxHeaderJson;

    public DocxFooter getDocxFooter() {
        if (StrUtil.isBlank(docxFooterJson)){
            return new DocxFooter();
        }
        return JsonUtils.parseObject(docxFooterJson, DocxFooter.class);
    }

    public DocxHeader getDocxHeader() {
        if (StrUtil.isBlank(docxHeaderJson)){
            return new DocxHeader();
        }
        return JsonUtils.parseObject(docxHeaderJson, DocxHeader.class);
    }
}
