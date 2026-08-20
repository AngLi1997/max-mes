package com.bmos.mes.service.execute.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mes.service.execute.constant.ExecuteFormDataConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Tolerate;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@TableName("bm_execute_form_data")
public class ExecuteFormData {

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
     * 生产计划id
     */
    private Long productPlanId;

    /**
     * 批号
     */
    private String batchNo;

    /**
     * 工艺id
     */
    private Long processId;

    /**
     * 工艺版本
     */
    private String processVersion;

    /**
     * 记录项id
     */
    private Long recordItemId;

    /**
     * 组件id
     */
    private Long fieldId;

    /**
     * 组件类型
     */
    private String componentType;

    /**
     * 历史工序步骤id
     */
    private Long procedureStepId;

    /**
     * 是否复用
     */
    @TableField("is_reuse")
    private Boolean reuse;

    /**
     * 是否废弃
     */
    @TableField("is_discard")
    private Boolean discard;

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

    /**
     * 备注
     */
    private String remark;

    private Long rev;

    /**
     * 证明人
     */
    @TableField(exist = false)
    private String evidenceName;

    /**
     * 证明时间
     */
    @TableField(exist = false)
    private String evidenceTime;

    @ApiModelProperty("工序换班次数")
    private Integer procedureChangeNumber;

    @ApiModelProperty("工艺换班次数")
    private Integer processChangeNumber;

    @ApiModelProperty("工步模型id")
    private Long procedureStepModelId;

    /**
     * 是否是空值
     * 录入空值、修订空值时此处为true
     */
    @TableField(value = "is_empty_value")
    private Boolean emptyValue;


    public Long getProcedureStepId() {
        return reuse ? ExecuteFormDataConstant.FORMULA_PROCEDURE_STEP_ID : procedureStepId;
    }
}
