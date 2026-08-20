package com.bmos.mes.service.plan.production.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @ClassName ProductionPageDTO
 * @Description 查询生产计划管理页dto
 * @Author Ren Jin Guang
 * @Date 2024/8/27 16:54
 */
@Setter
@Getter
@ToString
@ApiModel("查询生产计划管理页dto")
public class ProductionPageDTO extends BasePage {

    @ApiModelProperty("计划名称")
    private String planName;

    @ApiModelProperty("指令单类型")
    private String planType;

    @ApiModelProperty("状态")
    private String planState;
}
