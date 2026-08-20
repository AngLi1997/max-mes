package com.bmos.platform.service.equipment.controller.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 设备tag新增Vo
 *
 * @author yigaohui
 * @date 2024/8/12
 **/
@Data
@Validated
@ApiModel("设备类型新增VO")
public class EquipmentTagVO {

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
    private List<EquipmentTagPropertyVO> infoPropertyList;

    /**
     * 设备数据配置列表
     */
    private List<EquipmentTagPropertyVO> dataPropertyList;

    /**
     * 状态列表
     */
    private List<EquipmentTagPropertyVO> statusPropertyList;

    private String description;

    private List<EquipmentTagUseTemplateVO> useTemplateList;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createBy;
    private String updateBy;

}
