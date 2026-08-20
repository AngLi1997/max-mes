package com.bmos.platform.service.equipment.controller.vo;

import cn.hutool.core.util.ObjectUtil;
import com.bmos.platform.facade.equipment.enums.AcquisitionPlatformEnum;
import com.bmos.platform.facade.equipment.enums.EquipmentStatusCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * 设备APP信息VO
 */
@Getter
@Setter
@ApiModel("设备APP信息VO")
public class EquipmentAppInfoVO {

    /**
     * 设备id
     */
    @ApiModelProperty("设备id")
    private Long id;

    /**
     * 设备编码
     */
    @ApiModelProperty("设备编码")
    private String code;

    /**
     * 设备名称
     */
    @ApiModelProperty("设备名称")
    private String name;

    /**
     * 设备状态
     */
    @ApiModelProperty("设备状态")
    private Integer status;

    /**
     * 设备状态名称
     */
    @ApiModelProperty("设备状态名称")
    private String statusName;

    /**
     * 生产批号
     */
    @ApiModelProperty("生产批号")
    private String batchNo;

    /**
     * 产品名称
     */
    @ApiModelProperty("产品名称")
    private String productName;

    /**
     * 设备过期时间
     */
    @ApiModelProperty("设备过期时间")
    private LocalDate expireDateTime;

    @ApiModelProperty("工位名称")
    private List<CodeNameVO> stationNameList;

    @ApiModelProperty("房间名称")
    private List<CodeNameVO> roomNameList;

    @ApiModelProperty("产线名称")
    private List<CodeNameVO> productionLineNameList;

    /**
     * 设备标签
     */
    @ApiModelProperty("设备标签")
    private List<TagVO> tagVOList;

    /**
     * 设备状态列表
     */
    @ApiModelProperty("设备状态列表")
    private List<EquipmentStatusAppVO> equipmentStatusAppVOList;


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
     * 数采平台
     */
    @ApiModelProperty(value = "数采平台")
    private AcquisitionPlatformEnum acquisitionPlatform;

    public String getStatusName() {
        EquipmentStatusCodeEnum enumList = EquipmentStatusCodeEnum.getByCode(status);
        return ObjectUtil.isEmpty(enumList) ? "" : enumList.getDesc();
    }


}
