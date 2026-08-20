package com.bmos.mes.service.components.vo;

import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 业务组件实例
 * @author liang
 * @version 1.0.0
 * @date 2024/7/17 16:44
 */
@Data
@ApiModel(value = "业务组件实例")
public class BusinessComponentInstanceVO {

    /**
     * 业务组件实例id
     */
    @ApiModelProperty(value = "业务组件实例id", example = "1", required = true)
    private Long id;

    /**
     * 生产计划id(用于确定生产计划)
     */
    @ApiModelProperty(value = "生产计划id(用于确定生产计划)", example = "1", required = true)
    private Long productPlanId;

    /**
     * 工序步骤模型id(用于确定流程模型)
     */
    @ApiModelProperty(value = "工序步骤模型id(用于确定流程模型)", example = "1", required = true)
    private Long procedureStepModelId;

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
     * 组件配置json
     */
    @ApiModelProperty(value = "组件配置json", example = "{}")
    private String componentConfigJson;

    /**
     * 组件类型
     */
    @ApiModelEnumProperty(value = "组件类型", enumClass = BusinessComponentTypeEnum.class)
    private BusinessComponentTypeEnum componentType;

    /**
     * 组件名称
     */
    @ApiModelProperty(value = "组件名称", example = "配料称量")
    private String componentName;

    /**
     * 生产批号
     */
    @ApiModelProperty(value = "生产批号", example = "1", required = true)
    private String batchNo;
}
