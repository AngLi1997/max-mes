package com.bmos.platform.service.factory.controller.vo;

import com.bmos.mybatis.page.BasePage;
import com.bmos.platform.service.factory.enums.TenementFloorStatusEnums;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @className: TenementFloorPageQueryVO
 * @author: yigaohui
 * @date: 2024/12/30 14:50
 * @Version: 1.0
 * @description:
 */

@Data
@ApiModel("楼层分页查询VO")
public class TenementFloorPageQueryVO extends BasePage {

    @ApiModelProperty("楼层名称")
    private String name;

    @ApiModelProperty("楼层编码")
    private String code;

    @ApiModelProperty("楼栋id")
    private Long tenementId;

    @ApiModelEnumProperty(value = "楼栋状态", enumClass = TenementFloorStatusEnums.class)
    private TenementFloorStatusEnums status;
}
