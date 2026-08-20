package com.bmos.mes.service.output.finished.dto;

import com.bmos.mes.service.execute.dto.BusinessDataHandleBaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ApiModel("保存成品产出数据")
public class SaveFinishedProductOutputDTO extends BusinessDataHandleBaseDTO {


    @ApiModelProperty("成品产出主键id")
    private Long id;

    @ApiModelProperty("产出列表")
    private List<FinishedProductOutputInfo> outputList;

    @ApiModelProperty("操作人id")
    private String operatorId;

    @Data
    public static class FinishedProductOutputInfo{
        @ApiModelProperty("单间量")
        private BigDecimal singleQuantity;

        @ApiModelProperty("单件单位")
        private Long unitId;

        @ApiModelProperty("件数")
        private Integer number;
    }

}
