package com.bmos.mes.service.execute.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel("查询完整数据VO")
public class IntactFormDataVO {

    @ApiModelProperty("记录项id")
    private Long id;

    @ApiModelProperty("记录项名称")
    private String recordName;

    @ApiModelProperty("记录项id")
    private Long recordItemId;

    @ApiModelProperty("记录项版本id")
    private Long recordVersionId;

    @ApiModelProperty("记录项类型")
    private String itemType;

    @ApiModelProperty("工序步骤id")
    private Long procedureStepId;

    @ApiModelProperty("步骤id")
    private Long procedureId;

    @ApiModelProperty("步骤模型id")
    private Long procedureModelId;

    @ApiModelProperty("工步模型id")
    private Long procedureStepModelId;

    @ApiModelProperty("文件内容")
    private String fileContent;

    @ApiModelProperty("数据")
    private List<IntactFormDataItemVO> dataList;

    @ApiModelProperty("附件")
    private List<IntactFormAttachmentItemVO> attachments;

    @ApiModelProperty("排序")
    private Long order;

    @ApiModelProperty("复制版本")
    private Long copyVersion;

    @ApiModelProperty("页眉内容")
    private String headerContent;

    @ApiModelProperty("页脚内容")
    private String footerContent;

    @ApiModelProperty("是否复用")
    private Boolean reuse;


    @ApiModelProperty("是否作废")
    private Boolean discard;

    @ApiModelProperty("工艺换班次数")
    private Integer processChangeNumber;

    @ApiModelProperty("工序换班次数")
    private Integer procedureChangeNumber;

    private String pageConfig;

    @ApiModelProperty("复制版本id")
    private Long copyVersionId;
}
