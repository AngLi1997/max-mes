package com.bmos.mes.service.lotrelease.manage.vo;

import com.bmos.mes.service.lotrelease.template.enums.LotReleaseOperateType;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 批签发操作历史vo
 * @author liang
 * @version 1.0.0
 * @date 2024/8/20 13:56
 */
@Data
@ApiModel("批签发操作历史vo")
public class LogReleaseHistoryVO {

    @ApiModelProperty("操作记录id")
    private Long id;

    @ApiModelEnumProperty(value = "操作类型", enumClass = LotReleaseOperateType.class)
    private LotReleaseOperateType operationType;

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

    @ApiModelProperty("扩展信息")
    private String ext;

    /**
     * 若有值 则前端显示下载按钮，若没有值 前端不显示下载按钮
     */
    @ApiModelProperty("文件路径")
    private String path;
}
