package com.bmos.mes.service.components.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务组件实例
 * @author liang
 * @version 1.0.0
 * @date 2024/7/17 16:44
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "bm_business_component_instance")
public class BusinessComponentInstance extends BaseDO {

    /**
     * 生产计划id(用于确定生产计划)
     */
    @ApiModelProperty(value = "生产计划id(用于确定生产计划)", example = "1", required = true)
    private Long productPlanId;

    /**
     * 工序步骤id(用于确定流程)
     */
    @ApiModelProperty(value = "工序步骤id(用于确定流程)", example = "1", required = true)
    private Long procedureStepId;

    /**
     * 工序步骤模型id(用于确定流程模型)
     */
    @ApiModelProperty(value = "工序步骤模型id(用于确定流程模型)", example = "1", required = true)
    private Long procedureStepModelId;

    /**
     * 工艺id
     */
    @ApiModelProperty(value = "工艺id", example = "1", required = true)
    private Long processId;

    /**
     * 工艺版本
     */
    @ApiModelProperty(value = "工艺版本", example = "1", required = true)
    private String processVersion;

    /**
     * 记录项id
     */
    @ApiModelProperty(value = "记录项id",required = true)
    private Long recordItemId;

    /**
     * 记录项版本id
     */
    @ApiModelProperty(value = "记录项版本id",required = true)
    private Long recordVersionId;

    /**
     * 拷贝版本(默认0 用于确定移动端临时复制记录)
     */
    @ApiModelProperty(value = "拷贝版本(默认0 用于确定移动端临时复制记录)", example = "0", required = true)
    private Long copyVersion = 0L;

    /**
     * 组件id(用于确定组件类型)
     */
    @ApiModelProperty(value = "组件id(用于确定组件类型)", example = "1")
    private Long componentId;

    /**
     * 是否复用
     */
    @ApiModelProperty(value = "是否复用",required = true)
    private Boolean reuse;

    /**
     * 组件配置json
     */
    @ApiModelProperty(value = "组件配置json", example = "{}")
    private String componentConfigJson;

    /**
     * 组件配置id
     */
    @ApiModelProperty(value = "组件配置id", example = "1")
    private Long procedureStepConfigId;

    /**
     * 组件类型
     */
    @ApiModelEnumProperty(value = "组件类型", enumClass = BusinessComponentTypeEnum.class)
    private BusinessComponentTypeEnum componentType;

    /**
     * 组件名称
     */
    @ApiModelProperty(value = "组件名称")
    private String componentName;

    /**
     * 生产批号
     */
    @ApiModelProperty(value = "生产批号", example = "1", required = true)
    private String batchNo;
}
