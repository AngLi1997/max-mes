package com.bmos.platform.service.dict.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@TableName(value = "bp_dict_detail")
public class DictDetail extends BaseDO {

    @ApiModelProperty(value = "数据标签")
    private String dictLabel;

    @ApiModelProperty(value = "数据值")
    private String dictValue;

    @ApiModelProperty(value = "字典id")
    private Long dictId;
}
