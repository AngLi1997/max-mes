package com.bmos.mes.service.process.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel("工艺关联VO")
public class ProcessRelationVO {

    @ApiModelProperty("关联工艺id")
    private Long relationProcessId;

    @ApiModelProperty("关联物料id集合")
    private List<Long> materialIds;

}
