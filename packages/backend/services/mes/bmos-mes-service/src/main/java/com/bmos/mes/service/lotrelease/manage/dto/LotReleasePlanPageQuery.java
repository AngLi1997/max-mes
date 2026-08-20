package com.bmos.mes.service.lotrelease.manage.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 批签发查询生产计划分页请求
 * @author liang
 * @version 1.0.0
 * @date 2024/8/20 11:37
 */
@ApiModel("批签发查询生产计划分页请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class LotReleasePlanPageQuery extends BasePage {

    @ApiModelProperty(value = "批号", example = "CPX001231102")
    private String batchNo;

    @ApiModelProperty(value = "生产工艺", example = "生产工艺")
    private String processName;

    @ApiModelProperty (value = "产品分类id", example = "1")
    private Long productCategoryId;

    @ApiModelProperty (value = "产品id", example = "1")
    private Long productId;

    @ApiModelProperty(value = "是否批签发", example = "true")
    private Boolean lotRelease = true;

    @ApiModelProperty(value = "生产结束日期开始", example = "2024-08-01")
    private String startDate;

    @ApiModelProperty(value = "生产结束日期结束", example = "2024-08-01")
    private String endDate;
}
