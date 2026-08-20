package com.bmos.lims2.server.eln.entry.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.lims2.server.eln.entry.enums.ExecuteFormDataType;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @Description: 执行表单数据-异常批注实体
 * @Author: yigaohui
 * @Date: 2025/12/05 00:00
 */
@Getter
@Setter
@ToString
@TableName("bm_execute_form_data_annotation")
public class ExecuteFormDataAnnotation extends BaseDO {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 批注值
     */
    private String value;

    /**
     * 批注值扩展
     */
    private String valueExtension;

    /**
     * 扩展信息（后端使用）
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
     * 方案版本id
     */
    private Long schemeVersionId;

    /**
     * 记录项id
     */
    private Long recordItemId;

    /**
     * 记录版本id
     */
    private Long recordVersionId;

    /**
     * 记录id（方法id）
     */
    private Long recordId;

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 检验项目Id
     */
    private Long itemId;

    /**
     * 检验项目配置id
     */
    private Long itemConfigId;

    /**
     * 检验分析项id
     */
    private Long parameterId;

    /**
     * 检验分析项配置id
     */
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
     * 是否系统创建
     */
    @TableField("is_system_create")
    private Boolean systemCreate;

    /**
     * 操作类型
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
     * 备注
     */
    private String remark;
}


