package com.bmos.platform.service.system.code.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

@Getter
@Setter
@Builder
@ApiModel("BatchNextUseCodeElementVO:获取下一个使用的编码数据")
public class BatchNextUseCodeElementVO {
    @Tolerate
    public BatchNextUseCodeElementVO() {}
    @ApiModelProperty("编码规则code")
    private String fullNo;

    @ApiModelProperty("序列号")
    private Long sequence;

    @ApiModelProperty("生成数量")
    private Boolean isExistsDatabase;

    @ApiModelProperty("排序")
    private Integer sort;
}
