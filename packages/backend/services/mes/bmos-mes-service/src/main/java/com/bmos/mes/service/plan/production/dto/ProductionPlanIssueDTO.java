package com.bmos.mes.service.plan.production.dto;

import cn.hutool.core.collection.CollUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.mes.common.exception.MesResponseCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@ApiModel("下发生产计划DTO")
@Data
public class ProductionPlanIssueDTO {

    @ApiModelProperty("计划名称")
    @NotBlank
    private String planName;

    @ApiModelProperty("生产计划模板id")
    @NotNull
    private Long planTemplateId;

    @ApiModelProperty("首批生产日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate planFirstDate;

    @ApiModelProperty("间隔时长")
    @NotNull
    private Integer duration;

    @ApiModelProperty("生产计划数量")
    @NotNull
    private Integer planNumber;

    @ApiModelProperty("指令单类型")
    @NotBlank
    private String planType;

    @ApiModelProperty("批次列表")
    @NotEmpty
    @Valid
    private List<List<ProductionPlanItemSaveDTO>> itemList;

    public void validBatchList() {
        for (List<ProductionPlanItemSaveDTO> items : itemList) {
            if (CollUtil.isEmpty(items)) {
                throw new BmosException(MesResponseCode.PRODUCTION_PLAN_ISSUE_PARAM_ERROR);
            }
            for (ProductionPlanItemSaveDTO item : items) {
                item.validProperties();
            }
        }
    }


}
