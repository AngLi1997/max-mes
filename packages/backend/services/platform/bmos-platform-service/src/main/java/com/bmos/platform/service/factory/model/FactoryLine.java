package com.bmos.platform.service.factory.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 产线(BpFactoryLine)实体类
 *
 * @author makejava
 * @since 2024-05-21 17:04:58
 */
@Getter
@Setter
@TableName("bp_factory_line")
public class FactoryLine extends BaseDO {

    /**
     * 产线编码
     */
    private String code;
    /**
     * 产线名称
     */
    private String name;
    /**
     * 产线描述
     */
    private String description;
    /**
     * 所属模型id
     */
    private Long moduleId;

    /**
     * 启停
     */
    private Boolean enable;

    /**
     * 绑定工艺的个数
     */
    private Integer useCount;

    /**
     * 操作人id
     */
    private String operateId;

    /**
     * 操作人名称 login_name-user_name
     */
    private String operator;

    /**
     * 操作时间
     */
    private LocalDateTime operateTime;

}

