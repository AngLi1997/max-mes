package com.bmos.mes.service.facotry.service.dto;

import com.bmos.mybatis.page.BasePage;
import com.bmos.platform.facade.factory.enums.RoomStatusEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 移动端房间分页入参数
 */
@Getter
@Setter
@ApiModel("移动端房间分页入参数")
public class RoomMobilePageDTO extends BasePage {

    /**
     * 房间编码
     */
    @ApiModelProperty(value = "房间编码")
    private String code;

    /**
     * 房间名称
     */
    @ApiModelProperty(value = "房间名称")
    private String name;

    /**
     * 房间模型id
     */
    @ApiModelProperty(value = "房间模型id")
    private Long moduleId;

    @ApiModelEnumProperty(value = "房间状态", enumClass = RoomStatusEnum.class)
    private Integer status;


}
