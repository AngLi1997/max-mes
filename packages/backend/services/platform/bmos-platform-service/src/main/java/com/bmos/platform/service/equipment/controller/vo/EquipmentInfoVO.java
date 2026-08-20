package com.bmos.platform.service.equipment.controller.vo;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.mybatis.dataobject.BaseUserDO;
import com.bmos.platform.facade.equipment.enums.EquipmentStatusCodeEnum;
import com.bmos.platform.service.equipment.enums.AcquisitionPlatformEnum;
import com.bmos.platform.service.utils.UserUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 设备信息VO
 */
@Getter
@Setter
@ApiModel("设备信息VO")
public class EquipmentInfoVO {

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


    @ApiModelProperty("设备分类id")
    private Long categoryId;


    @ApiModelProperty("设备编码")
    private String code;

    /**
     * 设备标签id集合
     */
    @ApiModelProperty("设备信息")
    private List<TagVO> tagIdList;

    @ApiModelProperty("描述")
    private String description;


    /**
     * tag的名称集合
     */
    private List<String> tagNames;

    /**
     * 设备有限期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("设备有限期")
    private LocalDateTime expireDateTime;


    /**
     * 工位id
     */
    @ApiModelProperty("工位id")
    private List<Long> stationIdList;

    /**
     * 设备是否可用状态
     * com.bmos.platform.service.equipment.enums.EquipmentStatusEnum
     */
    @ApiModelProperty("设备是否可用状态")
    private Integer status;

    @ApiModelProperty("批次号")
    private String batchNo;

    @ApiModelProperty("启停状态")
    private Boolean enable;

    @ApiModelProperty("最后更新人")
    private String updateBy;

    @ApiModelProperty("最后更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;



    /**
     * 数采平台
     */
    @ApiModelProperty(value = "数采平台")
    private AcquisitionPlatformEnum acquisitionPlatform;

    /**
     * 设备所属标签的设备状态配置
     */
    @ApiModelProperty("设备所属标签的设备状态配置")
    private List<EquipmentStatusVO> statusPropertyList;

    /**
     * 设备下标签的属性配置
     */
    @ApiModelProperty("设备下标签的属性配置")
    private List<EquipmentPropertyVO> infoPropertyList;

    /**
     * 设备下数据属性配置
     */
    @ApiModelProperty("设备下数据属性配置")
    private List<EquipmentPropertyAcquisitionPointVO> dataPropertyList;

    /**
     * 设备状态名称
     */
    @ApiModelProperty("设备状态名称")
    private String statusName;

    public String getUpdateBy(){
        BaseUserDO user = UserUtils.getUser(updateBy);
        return ObjectUtil.isEmpty(user) ? "" : user.getUserName() + StrUtil.DASHED +user.getLoginName();
    }



    public String getStatusName() {
        EquipmentStatusCodeEnum enumList = EquipmentStatusCodeEnum.getByCode(status);
        return ObjectUtil.isEmpty(enumList) ? "" : enumList.getDesc();
    }
}
