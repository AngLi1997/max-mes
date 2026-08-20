package com.bmos.mes.service.lotrelease.manage.vo;

import com.bmos.mes.service.lotrelease.manage.enums.LotReleaseStatus;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 批签发版本分页数据vo
 * @author liang
 * @version 1.0.0
 * @date 2024/8/23 11:33
 */
@Data
@ApiModel("批签发版本分页数据vo")
public class LotReleaseVersionPageVO {

    @ApiModelProperty(value = "批签发id", example = "1")
    private Long id;

    @ApiModelProperty(value = "批签发模板id", example = "1")
    private Long templateId;

    @ApiModelProperty(value = "批签发模板版本", example = "1")
    private String templateVersion;

    @ApiModelProperty(value = "文件地址", example = "1")
    private String fileUrl;

    @ApiModelProperty(value = "批签发编号", example = "1")
    private String no;

    @ApiModelProperty(value = "生成人姓名", example = "张三")
    private String generatorName;

    @ApiModelProperty(value = "生成人id", example = "1")
    private String generatorId;

    @ApiModelProperty(value = "生成时间", example = "2024-08-20 11:26:00")
    private LocalDateTime generateTime;

    @ApiModelProperty(value = "生效时间", example = "2024-08-20 11:26:00")
    private LocalDateTime effectTime;

    @ApiModelEnumProperty(value = "状态", enumClass = LotReleaseStatus.class)
    private LotReleaseStatus status;

    @ApiModelProperty(value = "备注", example = "1")
    private String remark;

    private String deploymentId;

    private String processInstanceId;
}
