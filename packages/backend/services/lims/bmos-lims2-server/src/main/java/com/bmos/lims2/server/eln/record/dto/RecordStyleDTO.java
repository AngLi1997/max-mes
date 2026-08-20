package com.bmos.lims2.server.eln.record.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Tolerate;

@Setter
@Getter
@ToString
@Builder
@ApiModel(value = "批记录版本样式实体类")
public class RecordStyleDTO {

    @Tolerate
    public RecordStyleDTO() {
    }

    @ApiModelProperty(value = "样式")
    private Integer pattern;


    @ApiModelProperty("页脚高度")
    private String pageFooterHeight;

    @ApiModelProperty("页头高度")
    private String pageHeaderHeight;
}
