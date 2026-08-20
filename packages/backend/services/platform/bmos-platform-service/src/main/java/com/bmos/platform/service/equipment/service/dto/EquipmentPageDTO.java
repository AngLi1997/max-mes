package com.bmos.platform.service.equipment.service.dto;

import com.bmos.mybatis.page.BasePage;
import com.bmos.platform.common.enums.StatusEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 设备分页查询DTO
 */
@Getter
@Setter
@ApiModel("设备分页查询DTO")
public class EquipmentPageDTO extends BasePage {

    /**
     * 设备名称
     */
    @ApiModelProperty("设备名称")
    private String name;

    /**
     * 设备标签id
     */
    @ApiModelProperty("设备标签id")
    private Long tagId;

    /**
     * 启停状态
     */
    @ApiModelProperty("启停状态")
    @ApiModelEnumProperty(value = "启停", enumClass = StatusEnum.class)
    private Boolean enable;

    @ApiModelProperty("分类id")
    private Long categoryId;

    @ApiModelProperty(hidden = true)
    private List<Long> categoryIdList;

    @ApiModelProperty(hidden = true)
    private List<Long> tagIds;

}
