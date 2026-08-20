package com.bmos.mes.service.lotrelease.manage.vo;

import com.bmos.mes.service.lotrelease.manage.enums.LotReleaseStatus;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 批签发vo
 * @author liang
 * @version 1.0.0
 * @date 2024/8/20 11:26
 */
@Data
@ApiModel("批签发vo")
public class LotReleaseVO {

    @ApiModelProperty(value = "批签发id", example = "1")
    private Long id;

    @ApiModelProperty(value = "批签发编号", example = "10004")
    private String no;

    @ApiModelProperty(value = "生成人姓名", example = "张三")
    private String generatorName;

    @ApiModelProperty(value = "生成人id", example = "1")
    private String generatorId;

    @ApiModelProperty(value = "生成时间", example = "2024-08-20 11:26:00")
    private LocalDateTime generateTime;

    @ApiModelProperty(value = "生效时间", example = "2024-08-20 11:26:00")
    private LocalDateTime effectTime;

    @ApiModelProperty(value = "异常数量", example = "1")
    private Integer errorCount;

    @ApiModelProperty(value = "批签发名称", example = "批签发名称")
    private String lotReleaseName;

    @ApiModelProperty(value = "批签发版本", example = "1")
    private String lotReleaseVersion;

    @ApiModelEnumProperty(value = "批签发状态", enumClass = LotReleaseStatus.class)
    private LotReleaseStatus status;

    @ApiModelProperty(value = "备注", example = "备注")
    private String remark;
}
