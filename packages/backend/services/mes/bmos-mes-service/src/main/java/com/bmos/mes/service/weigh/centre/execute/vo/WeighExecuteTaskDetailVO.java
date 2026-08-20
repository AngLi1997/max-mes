package com.bmos.mes.service.weigh.centre.execute.vo;

import cn.hutool.extra.spring.SpringUtil;
import com.bmos.unit.service.UnitCache;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/7/10 15:28
 */
@Data
@ApiModel("称量执行任务详情查询结果")
public class WeighExecuteTaskDetailVO {

    @ApiModelProperty(value = "任务id", example = "1")
    private Long id;

    @ApiModelProperty(value = "物料id", example = "1")
    private Long materialId;

    @ApiModelProperty(value = "物料名称", example = "氯化钠")
    private String materialName;

    @ApiModelProperty(value = "物料编码", example = "001")
    private String materialMergeCode;

    @ApiModelProperty(value = "称量中心id", example = "1")
    private Long weighCentreId;

    @ApiModelProperty(value = "称量中心名称", example = "称量中心")
    private String weighCentreName;

    @ApiModelProperty(value = "称量中心编码", example = "001")
    private String weighCentreCode;

    @ApiModelProperty(value = "任务总需求量", example = "8.000")
    private BigDecimal requirementQuantity;

    @ApiModelProperty(value = "已称量", example = "8.000")
    private BigDecimal weighed;

    @ApiModelProperty(value = "未称量", example = "8.000")
    private BigDecimal unWeighed;

    @ApiModelProperty(value = "单位id", example = "1")
    private Long unitId;

    @ApiModelProperty(value = "单位", example = "ml")
    private String unit;

    @ApiModelProperty(value = "称重中的需求")
    private WeighExecuteRequirementVO weighExecuteRequirement;

    @ApiModelProperty(value = "称量需求列表")
    private List<WeighExecuteRequirementVO> requirements;

    /**
     * 工位id
     */
    @ApiModelProperty(value = "工位id", example = "1")
    private List<Long> station = new ArrayList<>();

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
        this.unit = SpringUtil.getBean(UnitCache.class).getGlobalUnitName(unitId);
    }

    @ApiModelProperty("当前称量的物料批次id")
    private Long storageMaterialBatchId;
}
