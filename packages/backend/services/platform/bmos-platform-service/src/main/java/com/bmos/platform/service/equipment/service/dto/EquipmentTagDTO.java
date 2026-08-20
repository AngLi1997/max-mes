package com.bmos.platform.service.equipment.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 设备tag表，记录设备的标签信息(BpEquipmentTag)实体类
 *
 * @author makejava
 * @since 2024-04-22 20:40:02
 */
@Getter
@Setter
public class EquipmentTagDTO {


    private Long id;

    private Long parentId;
    /**
     * 标签编码
     */
    private String code;

    /**
     * tag名称
     */
    private String name;
    /**
     * 设备信息配置列表
     */
    private List<EquipmentTagPropertyDTO> infoPropertyList;

    /**
     * 设备数据配置列表
     */
    private List<EquipmentTagPropertyDTO> dataPropertyList;

    /**
     * 状态列表
     */
    private List<EquipmentTagPropertyDTO> statusPropertyList;

    private String description;

    private List<EquipmentTagUseTemplateDTO> useTemplateList;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createBy;
    private String updateBy;
    private Boolean deleted;

}

