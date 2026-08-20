package com.bmos.mes.service.execute.model.calculate;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
public class CalculateParam {


    /**
     * 数据值
     */
    private String value;


    /**
     * 扩展字段 前端使用
     */
    private String valueExtension;

    /**
     * 扩展字段 后端使用
     */
    private String extInfo;

    /**
     * 组件id
     */
    private Long fieldId;

    /**
     * 组件类型
     */
    private String componentType;

    /**
     * 是否是系统创建
     */
    @TableField("is_system_create")
    private Boolean systemCreate;

    /**
     * 复制版本（默认0）
     */
    private Long copyVersion;

    /**
     * 操作类型
     * {@link com.bmos.mes.service.execute.enums.ExecuteFormDataType}
     */
    private String operationType;

    /**
     * 操作时间
     */
    private LocalDateTime operationTime;

    /**
     * 操作人
     */
    private String operationUser;

    /**
     * 复核人
     */
    private String reviewUser;

    /**
     * 复核时间
     */
    private LocalDateTime reviewTime;

    private Long rev;

    /**
     * 时间格式 暂用在签名时间格式上
     */
    private String timeFormat;
    private Boolean emptyValue;
}
