package com.bmos.platform.service.equipment.controller.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

/**
 * 设备类型使用日志模板
 *
 * @author yigaohui
 * @date 2024/8/13
 **/
@Data
@ApiModel("设备类型使用日志模板表")
@Validated
public class EquipmentTagUseTemplateVO {

    private String template;

    private String operateName;

    private Long tagId;

    private Long id;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String updateBy;
}
