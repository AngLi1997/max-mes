package com.bmos.mes.service.plan.production.vo;

import com.bmos.common.util.json.JsonUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@ApiModel("生产计划日历VO")
@Data
public class ProductionPlanCalendarVO {

    @ApiModelProperty("生产计划itemId")
    private Long id;

    @ApiModelProperty("生产指令单id")
    private Long productPlanId;

    @ApiModelProperty("生产计划id")
    private Long productionPlanId;

    @ApiModelProperty("生产批号")
    private String batchNo;

    @ApiModelProperty("工艺名称")
    private String processName;

    @ApiModelProperty("工艺版本")
    private String processVersion;

    @ApiModelProperty("工艺开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startTime;

    @ApiModelProperty("工艺结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endTime;

    @ApiModelProperty(value = "工序执行配置", hidden = true)
    @JsonIgnore
    private String procedureList;

    @ApiModelProperty("产线id")
    private Long productionLineId;

    @ApiModelProperty("产线名称")
    private String productionLineName;

    @ApiModelProperty("产线code")
    private String productionLineCode;

    public List<ProcedureDetailVO> getProcedureDateList() {
        return JsonUtils.parseArray(procedureList, ProcedureDetailVO.class);
    }
}
