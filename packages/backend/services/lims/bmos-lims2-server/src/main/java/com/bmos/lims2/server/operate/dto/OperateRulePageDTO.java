package com.bmos.lims2.server.operate.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author renjinguang
 */
@Setter
@Getter
@ToString
@ApiModel(value = "操作规程管理列表查询dto")
public class OperateRulePageDTO extends BasePage {

    @ApiModelProperty("文件名称")
    private String name;

    @ApiModelProperty("文件编号")
    private String code;

    @ApiModelProperty("分类id")
    private Long categoryId;

    @ApiModelProperty("分类集合")
    private List<Long> categoryIdList;

    @ApiModelProperty("数据权限id")
    private List<Long> deptIdList;
}
