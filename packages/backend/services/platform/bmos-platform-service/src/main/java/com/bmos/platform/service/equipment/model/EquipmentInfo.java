package com.bmos.platform.service.equipment.model;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.platform.service.equipment.enums.AcquisitionPlatformEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.io.Serializable;

/**
 * 设备基础信息表，记录设备的基础信息(BpEquipmentInfo)实体类
 *
 * @author makejava
 * @since 2024-04-22 20:36:22
 */
@Getter
@Setter
@TableName("bp_equipment_info")
public class EquipmentInfo extends BaseDO implements Serializable {

    /**
     * 设备编码
     */
    private String code;
    /**
     * 设备名称
     */
    private String name;
    /**
     * 当前设备被占用时的生产批号
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String batchNo;
    /**
     * 产品名称
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String productName;
    /**
     * 占用设备的工位id
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long applyStationId;
    /**
     * 设备状态(1-可用 2-不可用 3-故障 4-占用)
     */
    private Integer status;
    /**
     * 设备状态有效期（设备小的所有设备状态的最小有效期）
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private LocalDateTime expireDateTime;
    /**
     * 设备类别id，关联到bp_equipment_category表中的id
     */
    private Long categoryId;
    /**
     * 启停状态
     */
    private Boolean enable;

    /**
     * 当前批号占用后绑定的操作日志id
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long operateLogId;

    /**
     * 描述
     */
    private String description;


    /**
     * 数采平台
     */
    private AcquisitionPlatformEnum acquisitionPlatform;


}

