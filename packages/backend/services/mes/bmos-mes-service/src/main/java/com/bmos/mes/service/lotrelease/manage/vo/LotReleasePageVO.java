package com.bmos.mes.service.lotrelease.manage.vo;

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
@ApiModel("批签发分页数据vo")
public class LotReleasePageVO {

    @ApiModelProperty(value = "批签发模板id", example = "1")
    private Long id;

    @ApiModelProperty(value = "批签发模板名称", example = "1")
    private String name;

    @ApiModelProperty(value = "生效的批签发id", example = "1")
    private Long effectiveLotReleaseId;

    @ApiModelProperty(value = "生效的批签发编号", example = "10004")
    private String no;

    @ApiModelProperty(value = "生效的批签发版本", example = "1")
    private String templateVersion;

    @ApiModelProperty(value = "生效的生成人姓名", example = "张三")
    private String generatorName;

    @ApiModelProperty(value = "生效的生成人id", example = "1")
    private String generatorId;

    @ApiModelProperty(value = "生效的生成时间", example = "2024-08-20 11:26:00")
    private LocalDateTime generateTime;

    @ApiModelProperty(value = "生效时间", example = "2024-08-20 11:26:00")
    private LocalDateTime effectTime;

    @ApiModelProperty(value = "生效的文件url", example = "http://www.baidu.com")
    private String fileUrl;

    @ApiModelProperty(value = "生产批次id", example = "1")
    private Long planId;
}
