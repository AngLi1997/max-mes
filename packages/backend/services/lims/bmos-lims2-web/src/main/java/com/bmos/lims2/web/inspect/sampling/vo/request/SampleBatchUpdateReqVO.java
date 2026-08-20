package com.bmos.lims2.web.inspect.sampling.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * 样品批量更新请求VO
 *
 * @author yigaohui
 * @since 2025/01/29 18:00
 */
@Data
@ApiModel("样品批量更新请求")
public class SampleBatchUpdateReqVO {

    @ApiModelProperty(value = "请验单ID", required = true)
    @NotNull(message = "请验单ID不能为空")
    private Long inspectionOrderId;

    @ApiModelProperty(value = "样品信息列表", required = true)
    @NotEmpty(message = "样品信息列表不能为空")
    @Valid
    private List<SampleInfoReqVO> samples;

    /**
     * 样品信息请求VO
     */
    @Data
    @ApiModel("样品信息请求")
    public static class SampleInfoReqVO {

        @ApiModelProperty("样品ID（新增时为空，后端根据此字段判断是新增还是更新）")
        private Long sampleId;

        @ApiModelProperty("样品编号（新增时可为空，系统自动生成）")
        private String sampleNo;

        @ApiModelProperty("检验项目ID（可选，为空表示整体取样）")
        private Long inspectionItemId;

        @ApiModelProperty("检验项目名称")
        private String inspectionItemName;

		@ApiModelProperty("计划取样量")
		@Pattern(regexp = "^-?\\d{1,6}(\\.\\d{1,5})?$", message = "计划取样量最多整数6位，小数5位")
		private String plannedQuantity;

		@ApiModelProperty("实际取样量")
		@Pattern(regexp = "^-?\\d{1,6}(\\.\\d{1,5})?$", message = "实际取样量最多整数6位，小数5位")
		private String actualQuantity;

        @ApiModelProperty("取样单位")
        private Long unitId;

        @ApiModelProperty("备注")
        private String remark;
    }
}
