package com.bmos.lims2.web.inspect.division.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * @Description: 分样操作请求VO
 * @Author: yigaohui
 * @Date: 2025/01/29 16:45
 */
@Getter
@Setter
@ApiModel("分样操作请求")
public class SampleDivisionReqVO {

    @ApiModelProperty(value = "原样品ID", required = true)
    @NotNull(message = "原样品ID不能为空")
    private Long originalSampleId;

    @ApiModelProperty(value = "分样结果列表", required = true)
    @NotEmpty(message = "分样结果不能为空")
    @Valid
    private List<DivisionResultVO> divisionResults;

    @Getter
    @Setter
    @ApiModel("分样结果")
    public static class DivisionResultVO {

        @ApiModelProperty(value = "分样数量", required = true)
        @NotNull(message = "分样数量不能为空")
        @Pattern(regexp = "^-?\\d{1,6}(\\.\\d{1,5})?$", message = "分样数量最多整数6位，小数5位")
        private String quantity;

        @ApiModelProperty(value = "样品份数", required = true)
        @NotNull(message = "样品份数不能为空")
        @Min(value = 1, message = "样品份数必须大于等于1")
        private Integer sampleCount;

        @ApiModelProperty(value = "单位ID", required = true)
        @NotNull(message = "单位ID不能为空")
        private Long unitId;

        @ApiModelProperty("检验项目ID（可以为空，表示不指定检验项目）")
        private Long inspectItemId;

        @ApiModelProperty("备注")
        private String remark;
    }
}
