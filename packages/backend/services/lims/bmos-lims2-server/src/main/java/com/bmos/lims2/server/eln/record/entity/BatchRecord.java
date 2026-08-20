package com.bmos.lims2.server.eln.record.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@TableName(value = "bm_batch_record")
@Setter
@Getter
@ToString
public class BatchRecord extends BaseDO {

    @ApiModelProperty(value = "记录名称")
    private String name;

    @ApiModelProperty(value = "分类id")
    private Long categoryId;

    @ApiModelProperty(value = "记录编号")
    private String code;
}
