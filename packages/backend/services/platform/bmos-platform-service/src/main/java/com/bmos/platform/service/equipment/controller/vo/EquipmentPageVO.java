package com.bmos.platform.service.equipment.controller.vo;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.mybatis.dataobject.BaseUserDO;
import com.bmos.platform.service.utils.UserUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 设备分页查询VO
 */
@Getter
@Setter
@ApiModel("设备分页查询VO")
public class EquipmentPageVO {

    /**
     * 设备id
     */
    @ApiModelProperty("设备id")
    private Long id;

    /**
     * 设备名称
     */
    @ApiModelProperty("设备名称")
    private String name;

    /**
     * 设备code
     */
    @ApiModelProperty("设备code")
    private String code;

    /**
     * 启停状态
     */
    @ApiModelProperty("启停状态")
    private Boolean enable;

    /**
     * 标签名称
     */
    @ApiModelProperty("标签名称")
    private String tagName;

    /**
     * 最后更新人
     */
    @ApiModelProperty("最后更新人")
    private String updateBy;

    /**
     * 最后更新时间
     */
    @ApiModelProperty("最后更新时间")
    private LocalDateTime updateTime;

    public String getUpdateBy(){
        BaseUserDO user = UserUtils.getUser(updateBy);
        return ObjectUtil.isEmpty(user) ? "" : user.getUserName() + StrUtil.DASHED +user.getLoginName();
    }

}
