package com.bmos.lims2.server.operate.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author renjinguang
 */
@Setter
@Getter
@ToString
@TableName(value = "bm_operate_rule_category")
public class OperateRuleCategory extends BaseDO {

    @ApiModelProperty("分类名称")
    private String name;

    @ApiModelProperty("上级id")
    private Long parentId;

    @ApiModelProperty("编码")
    private String code;

}
