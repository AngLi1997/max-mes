package com.bmos.lims2.web.inspect.item.vo.req;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 检验项分页查询VO
 */
@Setter
@Getter
@ApiModel("检验项分页查询VO")
public class InspectProgramPageReqVO extends BasePage {

    /**
     * 检验名称
     */
    @ApiModelProperty("检验名称")
    private String name;

    /**
     * 检验编码
     */
    @ApiModelProperty("检验编码")
    private String code;

    /**
     * 关键字
     */
    @ApiModelProperty("关键字")
    private String keyword;

    /**
     * 不需要查询的检验项id列表集合
     */
    @ApiModelProperty("不需要查询的检验项id列表集合")
    private List<Long> excludeIdList;

    /**
     * 分析项名称
     */
    @ApiModelProperty("分析项名称")
    private String parameterName;

}
