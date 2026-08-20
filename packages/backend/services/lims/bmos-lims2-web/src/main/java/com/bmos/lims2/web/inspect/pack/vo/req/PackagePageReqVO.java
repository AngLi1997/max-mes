package com.bmos.lims2.web.inspect.pack.vo.req;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 实验包分页参数
 */
@Getter
@Setter
@ApiModel("实验包分页参数")
public class PackagePageReqVO extends BasePage {

    /**
     * 实验包编码
     */
    private String code;

    /**
     * 实验包名称
     */
    private String name;

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 排除的检验项目id
     */
    private List<Long> excludeIdList;

    /**
     * 检验项目名称 - 用于筛选
     */
    @ApiModelProperty(value = "检验项目名称 - 用于筛选")
    private String inspectItemName;

}
