package com.bmos.mes.common.model.execute;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("设备数采绘图拓展值")
public  class AcquisitionPictureExtInfo {
    /**
     * 设备信息
     */
    private String equipmentInfo;

    /**
     * 设备数据
     */
    private String equipmentData;

    /**
     * 采集人
     */
    private String acquisitionUser;

    /**
     * 采集时间
     */
    private String acquisitionTime;

    /**
     * 设备id
     */
    private Long equipmentId;

    /**
     * 图片地址
     */
    private String url;
}