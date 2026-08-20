package com.bmos.mes.service.plan.document.controller.vo;

import com.bmos.mes.common.enums.plan.BatchRecordArchiveOperateTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 批记录档案操作历史
 */
@Getter
@Setter
@ApiModel("批记录档案操作历史")
public class ArchiveHistoryVO {

    @ApiModelProperty("操作记录id")
    private Long id;

    @ApiModelProperty("操作类型")
    private BatchRecordArchiveOperateTypeEnum operationType;

    @ApiModelProperty("操作人名称")
    private String createBy;

    @ApiModelProperty("操作人名称")
    private String createUsername;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("操作")
    private String comment;

    @ApiModelProperty("节点名称")
    private String nodeName;

    /**
     * 此为path
     */
    @ApiModelProperty("扩展信息")
    private String ext;

    /**
     * 若有值 则前端显示下载按钮，若没有值 前端不显示下载按钮
     */
    @ApiModelProperty("文件路径")
    private String path;

}
