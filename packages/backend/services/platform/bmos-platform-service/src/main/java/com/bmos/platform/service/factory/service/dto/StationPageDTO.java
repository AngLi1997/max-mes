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
 * 工位分页DTO
 */
@Getter
@Setter
@ApiModel("工位分页请求入参")
public class StationPageDTO extends BasePage {

    /**
     * 工位名称
     */
    @ApiModelProperty(value = "工位名称")
    private String name;

    @ApiModelProperty("工位编码")
    private String code;

    /**
     * 工位启停状态
     */
    @ApiModelProperty(value = "工位启停状态")
    @ApiModelEnumProperty(value = "启停",enumClass = StatusEnum.class)
    private Boolean enable;

    @ApiModelProperty(value = "分类id")
    private Long moduleId;

    private List<Long> moduleIdList;


}
