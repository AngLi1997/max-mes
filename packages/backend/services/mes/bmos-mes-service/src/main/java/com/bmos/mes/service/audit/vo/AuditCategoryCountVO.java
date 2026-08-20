package com.bmos.mes.service.audit.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Tolerate;

/**
 * @author renjinguang
 */
@Setter
@Getter
@Builder
@ToString
@ApiModel(value = "分类审核代办任务数量返回vo")
public class AuditCategoryCountVO {
    @Tolerate
    public AuditCategoryCountVO() {}
    @ApiModelProperty("数量")
    private Integer number;

    @ApiModelProperty("分类code")
    private String categoryCode;
}
