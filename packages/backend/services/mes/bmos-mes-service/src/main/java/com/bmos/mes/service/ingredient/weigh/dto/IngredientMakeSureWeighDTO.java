package com.bmos.mes.service.ingredient.weigh.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 确认称量人dto
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/18 14:02
 */
@ApiModel("配料确认称量人dto")
@Data
public class IngredientMakeSureWeighDTO {

    /**
     * 配料单id
     */
    @ApiModelProperty(value = "配料单id", example = "1", required = true)
    @NotNull
    private Long ingredientPlanId;

    /**
     * 生产计划id
     */
    @ApiModelProperty(value = "生产计划id", example = "1", required = true)
    @NotNull
    private Long productPlanId;

    /**
     * 工序步骤模型id
     */
    @ApiModelProperty(value = "工序步骤模型id", example = "1", required = true)
    @NotNull
    private Long procedureStepModelId;

    /**
     * 拷贝版本
     */
    @ApiModelProperty(value = "拷贝版本(默认0)", example = "1", required = true)
    @NotNull
    private Long copyVersion;

    /**
     * 组件id
     */
    @ApiModelProperty(value = "组件id", example = "1")
    @NotNull
    private Long componentId;

    /**
     * 暂存物料批次id
     */
    @ApiModelProperty(value = "暂存物料批次id", example = "1", required = true)
    @NotNull
    private Long storageMaterialBatchId;

    /**
     * 称量人id
     */
    @ApiModelProperty(value = "称量人id(首次确认时必填)", example = "1", required = true)
    private String weigherId;

    /**
     * 复核人id
     */
    @ApiModelProperty(value = "复核人id(首次确认时必填)", example = "1", required = true)
    private String reCheckerId;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", example = "备注")
    @Length(max = 200)
    private String remark;

    /**
     * 消耗物料件id列表
     */
    @ApiModelProperty(value = "消耗物料件id列表")
    private List<Long> consumeStorateMaterialIdList = new ArrayList<>();
}
