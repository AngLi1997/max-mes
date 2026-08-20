package com.bmos.platform.service.factory.service.dto;

import com.bmos.mybatis.page.BasePage;
import com.bmos.platform.common.enums.StatusEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 产线分页入参
 */
@Getter
@Setter
@ApiModel("产线分页入参")
public class LinePageDTO extends BasePage {

    /**
     * 房间编码
     */
    @ApiModelProperty(value = "产线编码")
    private String code;

    /**
     * 房间启停状态
     */
    @ApiModelProperty(value = "产线启停状态")
    @ApiModelEnumProperty(value = "产线启停状态" ,enumClass = StatusEnum.class)
    private Boolean enable;

    /**
     * 房间名称
     */
    @ApiModelProperty(value = "产线名称")
    private String name;

    /**
     * 房间模型id
     */
    @ApiModelProperty(value = "产线模型id")
    private Long moduleId;

}
