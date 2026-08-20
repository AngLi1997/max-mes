package com.bmos.platform.service.equipment.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 设备标签下的所有状态
 */
@Getter
@Setter
@ApiModel("设备标签下的所有状态")
public class EquipmentStatusAppVO {

     private Long id;

     /**
      * 设备状态名称
      */
     @ApiModelProperty("设备状态名称")
     private String name;

     /**
      * 有效期
      */
     @ApiModelProperty("有效期")
     private LocalDateTime expireDateTime;
     /**
      * 默认效期值
      */
     @ApiModelProperty("默认效期值")
     private String value;

     /**
      * 当前设备状态是否完成
      */
     @ApiModelProperty("当前设备状态是否完成")
     private Boolean finishStatus;

     /**
      * 设备状态code
      */
     @ApiModelProperty("设备状态code")
     private String code;



}
