package com.bmos.platform.service.factory.controller.vo;

import com.bmos.mybatis.page.BasePage;
import com.bmos.platform.service.factory.enums.TenementFloorStatusEnums;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @className: TenementFloorPageQueryVO
 * @author: yigaohui
 * @date: 2024/12/30 14:50
 * @Version: 1.0
 * @description:
 */

@Data
@ApiModel("楼层列表查询VO")
public class TenementFloorListQueryVO {

    @ApiModelProperty("楼层名称")
    private String name;

    @ApiModelProperty("楼层编码")
    private String code;

    @ApiModelProperty("楼栋id")
    private List<Long> tenementIds;

    @ApiModelEnumProperty(value = "楼栋状态", enumClass = TenementFloorStatusEnums.class)
    private TenementFloorStatusEnums status;
}
