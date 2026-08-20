package com.bmos.mes.service.weigh.centre.task.vo;

import cn.hutool.extra.spring.SpringUtil;
import com.bmos.mes.common.enums.weigh.centre.TaskStatusEnum;
import com.bmos.unit.service.UnitCache;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 称量任务分页查询vo
 * @author liang
 * @version 1.0.0
 * @date 2024/7/8 17:41
 */
@Data
@ApiModel("称量任务分页查询vo")
public class WeighTaskPageVO {

    @ApiModelProperty(value = "称量任务id", example = "1")
    private Long id;

    @ApiModelProperty(value = "称量任务编号", example = "1")
    private String taskNo;

    @ApiModelProperty(value = "物料名称", example = "氯化钠")
    private String materialName;

    @ApiModelProperty(value = "物料编码", example = "WH03")
    private String materialMergeCode;

    @ApiModelProperty(value = "物料规格", example = "1个")
    private String materialSpecification;

    @ApiModelProperty(value = "称量中心名称", example = "狂犬疫苗配液称量中心")
    private String weighCentreName;

    @ApiModelProperty(value = "称量中心编码", example = "KQ-PYCL-101")
    private String weighCentreCode;

    @ApiModelProperty(value = "需求量", example = "8.000")
    private BigDecimal requirementQuantity;

    @ApiModelProperty(value = "单位id", example = "1")
    private Long unitId;

    @ApiModelProperty(value = "单位", example = "ml")
    private String unit;

    @ApiModelProperty(value = "执行时间", example = "2024-07-01")
    private LocalDate executeDate;

    @ApiModelEnumProperty(value = "任务状态", enumClass = TaskStatusEnum.class)
    private TaskStatusEnum taskStatus;

    @ApiModelProperty(value = "是否可取消", example = "true")
    private Boolean cancelAble;

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
        this.unit = SpringUtil.getBean(UnitCache.class).getGlobalUnitName(unitId);
    }
}
