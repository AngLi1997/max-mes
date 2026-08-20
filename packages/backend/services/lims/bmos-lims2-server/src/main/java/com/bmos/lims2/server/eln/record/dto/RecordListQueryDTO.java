package com.bmos.lims2.server.eln.record.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
@ApiModel(value = "批记录查询DTO")
public class RecordListQueryDTO extends BasePage {

    @ApiModelProperty("批记录名称")
    private String name;

    @ApiModelProperty("批记录编码")
    private String code;

    @ApiModelProperty("批记录id")
    private Long recordId;

    @ApiModelProperty("分类id")
    private Long categoryId;

    @ApiModelProperty("部门id集合")
    private List<Long> deptIds;

    @ApiModelProperty("分类集合")
    private List<Long> categoryList;

    @ApiModelProperty(hidden = true)
    private String recordState;
}
