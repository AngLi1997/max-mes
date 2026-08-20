package com.bmos.lims2.server.eln.entry.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.lims2.server.eln.entry.enums.ExecuteFormDataType;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Tolerate;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@TableName("bm_execute_form_data")
public class ExecuteFormData extends BaseDO {

    @Tolerate
    public ExecuteFormData() {
    }

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

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
     * 请验单id
     */
    private Long inspectionOrderId;

    /**
     * 批号
     */
    private String batchNo;

    /**
     * 方案id
     */
    private Long schemeId;

    /**
     * 工艺版本id
     */
    private Long schemeVersionId;

    /**
     * 记录项id
     */
    private Long recordItemId;

    private Long recordVersionId;

    private Long recordId;

    private Long taskId;

    private Long itemId;

    private Long itemConfigId;

    private Long parameterId;

    private Long parameterConfigId;

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
     * 操作类型
     * {@link ExecuteFormDataType}
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

    /**
     * 备注
     */
    private String remark;

    private Long rev;
    /**
     * 是否是空值
     * 录入空值、修订空值时此处为true
     */
    @TableField(value = "is_empty_value")
    private Boolean emptyValue;

    /**
     * 是否废弃
     */
    @TableField("is_discard")
    private Boolean discard;

}
