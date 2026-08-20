package com.bmos.platform.service.factory.service.dto;

import com.bmos.mybatis.page.BasePage;
import com.bmos.platform.common.enums.StatusEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 房间分页入
 */
@Getter
@Setter
@ApiModel("房间分页入参数")
public class RoomPageDTO extends BasePage {

    /**
     * 房间编码
     */
    @ApiModelProperty(value = "房间编码")
    private String code;

    /**
     * 房间启停状态
     */
    @ApiModelProperty(value = "启停")
    @ApiModelEnumProperty(value = "启停", enumClass = StatusEnum.class)
    private Boolean enable;

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

    @ApiModelProperty("状态")
    private Integer status;
}
