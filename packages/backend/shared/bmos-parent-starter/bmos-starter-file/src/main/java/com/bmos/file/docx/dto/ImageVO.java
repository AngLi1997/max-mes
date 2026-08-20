package com.bmos.file.docx.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * @ClassName ImagesVO
 * @Description 附件解析vo
 * @Author Ren Jin Guang
 * @Date 2024/8/2 18:06
 */
@Getter
@Setter
@ToString
@ApiModel(value = "附件图片解析vo")
public class ImageVO {

    @ApiModelProperty("附件id")
    private Long attachmentId;

    @ApiModelProperty("解析数据")
    private String value;

    @ApiModelProperty("附件取证人")
    private String evidenceName;

    @ApiModelProperty("附件取证时间")
    private String evidenceTime;

    @ApiModelProperty("图注")
    private String imageCaption;
}
