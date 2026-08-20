package com.bmos.lims2.server.eln.record.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@TableName(value = "bm_batch_record_category")
@Getter
@Setter
@ToString
public class BatchRecordCategory extends BaseDO {

    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "上级id")
    private Long parentId;

    @ApiModelProperty(value = "排序号")
    private Integer sort;

    @ApiModelProperty(value = "标识")
    private String code;

}
