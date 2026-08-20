package com.bmos.lims2.server.audit.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 流程分类实体类
 */
@TableName(value = "lm_flow_audit_category")
@Setter
@Getter
public class FlowAuditCategory extends BaseDO {

    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "分类编码")
    private String code;

    @ApiModelProperty(value = "上级id")
    private Long parentId;

    @ApiModelProperty(value = "层级名称")
    private String treeCode;

    @ApiModelProperty(value = "层级名称")
    private String treeName;
}
