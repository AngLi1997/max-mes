package com.bmos.lims2.server.inspect.division.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Description: 分样操作DTO
 * @Author: yigaohui
 * @Date: 2025/01/29 16:45
 */
@Getter
@Setter
@ApiModel("分样操作数据对象")
public class SampleDivisionDTO {

    @ApiModelProperty("原样品ID")
    private Long originalSampleId;

    @ApiModelProperty("分样结果列表")
    private List<DivisionResultDTO> divisionResults;

    @Getter
    @Setter
    @ApiModel("分样结果数据对象")
    public static class DivisionResultDTO {

        @ApiModelProperty("分样数量")
        @Pattern(regexp = "^(?:0|[1-9]\\d{0,5})(?:\\.\\d{1,5})?$", message = "分样数量最多整数6位，小数5位")
        private String quantity;

        @ApiModelProperty("样品份数")
        private Integer sampleCount;

        @ApiModelProperty("单位ID")
        private Long unitId;

        @ApiModelProperty("检验项目ID")
        private Long inspectItemId;

        @ApiModelProperty("备注")
        private String remark;
    }
}
