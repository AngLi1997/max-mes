package com.bmos.mes.service.audit.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author renjinguang
 */
@Getter
@Setter
@ToString
@ApiModel(value = "查询分类树")
public class FlowAuditCategoryVO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "分类编码")
    private String code;

    @ApiModelProperty(value = "上级id")
    private Long parentId;

    @ApiModelProperty(value = "分类编码")
    private String categoryCode;

    @ApiModelProperty(value = "下级集合")
    private List<FlowAuditCategoryVO> itemList;
}
