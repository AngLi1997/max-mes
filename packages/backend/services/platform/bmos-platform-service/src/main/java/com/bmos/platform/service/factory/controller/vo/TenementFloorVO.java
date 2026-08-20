package com.bmos.platform.service.factory.controller.vo;

import com.bmos.platform.service.factory.enums.TenementFloorStatusEnums;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @className: TenementFloorAddVO
 * @author: yigaohui
 * @date: 2024/12/30 14:32
 * @Version: 1.0
 * @description:
 */

@Data
@ApiModel("楼层添加VO")
public class TenementFloorVO {
    private Long id;
    /**
     * 楼栋id
     */
    @ApiModelProperty("楼栋id")
    @NotNull
    private Long tenementId;
    /**
     * 编码
     */
    @ApiModelProperty("楼层编码")
    @NotNull
    private String code;
    /**
     * 楼层名称
     */
    @ApiModelProperty("楼层名称")
    @NotNull
    private String name;


    /**
     * 状态
     */
    @ApiModelEnumProperty(value = "楼栋状态", enumClass = TenementFloorStatusEnums.class)
    private TenementFloorStatusEnums status;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("创建人名称")
    private String createByName;

    @ApiModelProperty("修改人")
    private String updateBy;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
