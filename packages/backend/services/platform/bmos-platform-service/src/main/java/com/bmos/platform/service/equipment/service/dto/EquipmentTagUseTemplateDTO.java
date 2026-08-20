package com.bmos.platform.service.equipment.service.dto;

import com.baomidou.mybatisplus.annotation.*;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.web.validation.InsertValidation;
import com.bmos.web.validation.UpdateValidation;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Size;
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
public class EquipmentTagUseTemplateDTO{

    private String template;

    private String operateName;

    private Long tagId;

    private Long id;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String updateBy;
}
