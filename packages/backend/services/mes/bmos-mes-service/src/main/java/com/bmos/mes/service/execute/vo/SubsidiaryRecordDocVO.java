package com.bmos.mes.service.execute.vo;

import com.bmos.file.docx.model.DocxFooter;
import com.bmos.file.docx.model.DocxHeader;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("辅助记录归档VO")
public class SubsidiaryRecordDocVO {

    @ApiModelProperty("记录项名称")
    private String recordName;

    @ApiModelProperty("记录项id")
    private Long recordItemId;

    @ApiModelProperty("记录项版本id")
    private Long recordVersionId;

    @ApiModelProperty("记录项类型")
    private String itemType;

    @ApiModelProperty("文件内容")
    private String fileContent;

    @ApiModelProperty("数据")
    private List<IntactFormDataItemVO> dataList;

    @ApiModelProperty("附件")
    private List<IntactFormAttachmentItemVO> attachments;

    @ApiModelProperty("复制版本")
    private Long copyVersion;

    @ApiModelProperty(value = "页脚")
    private DocxFooter docxFooter;

    @ApiModelProperty(value = "页眉")
    private DocxHeader docxHeader;

    @ApiModelProperty("是否作废")
    private Boolean discard;

    @ApiModelProperty(value = "首页不同")
    private Boolean firstDifferent;

    private String pageConfig;

}
