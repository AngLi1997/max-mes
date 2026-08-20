package com.bmos.lims2.server.inspect.parameter.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * 分析项分页查询VO
 */
@Setter
@Getter
@ApiModel("分析项分页查询VO")
public class InspectParameterPageReqDTO extends BasePage {
    /**
     * 分析项名称
     */
    @ApiModelProperty(value = "分析项名称")
    @Length(max = 30)
    private String name;

    /**
     * 分析项编码
     */
    @ApiModelProperty(value = "分析项编码")
    @Length(max = 30)
    private String code;

    /**
     * 关键字
     */
    @ApiModelProperty(value = "关键字")
    private String keyword;

    /**
     * 需要排除的分析项id列表集合
     */
    @ApiModelProperty(value = "需要排除的分析项id列表集合")
    private List<Long> excludeIdList;

}
