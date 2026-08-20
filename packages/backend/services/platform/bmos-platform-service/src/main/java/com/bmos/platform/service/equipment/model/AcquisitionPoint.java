package com.bmos.platform.service.equipment.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.platform.service.equipment.enums.AcquisitionPlatformEnum;
import com.bmos.platform.service.equipment.service.enums.AcquisitionPointDataTypeEnum;
import com.bmos.platform.service.equipment.service.enums.AcquisitionPointStatusEnum;
import com.bmos.platform.service.equipment.service.enums.AcquisitionPointTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;


/**
 * 采集点数据表(AcquisitionPoint)实体类
 *
 * @author makejava
 * @since 2024-04-19 15:17:52
 */
@Getter
@Setter
@ToString
@TableName("bp_acquisition_point")
public class AcquisitionPoint extends BaseDO implements Serializable {
    private static final long serialVersionUID = -44718737082588826L;


    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 采集点名称，用这个值和scada系统关联
     */
    private String dataPointName;


    /**
     * 采集点类型
     */
    private AcquisitionPointTypeEnum type;

    /**
     * 数据类型
     */
    private AcquisitionPointDataTypeEnum dataType;
    /**
     * 状态
     */
    private AcquisitionPointStatusEnum status;

    /**
     * 描述
     */
    private String description;

    /**
     * 设备数据code
     */
    private String equipmentTagDataCode;

    /**
     * 数采平台
     */
    private AcquisitionPlatformEnum acquisitionPlatform;
}

