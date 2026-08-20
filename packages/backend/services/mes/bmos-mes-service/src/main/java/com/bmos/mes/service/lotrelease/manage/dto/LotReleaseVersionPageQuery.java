package com.bmos.mes.service.lotrelease.manage.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 批签发查询版本分页请求
 * @author liang
 * @version 1.0.0
 * @date 2024/8/23 11:18
 */
@ApiModel("批签发查询版本分页请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class LotReleaseVersionPageQuery extends BasePage {

    @ApiModelProperty(value = "批签发模板id", example = "1")
    private Long lotReleaseTemplateId;

    @ApiModelProperty(value = "生产计划id", example = "1")
    private Long planId;
}
