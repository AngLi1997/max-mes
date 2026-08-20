package com.bmos.lims2.web.inspect.entry.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @Description: 检项查询-分页行返回VO
 * @Author: yigaohui
 * @Date: 2025/09/05 10:55
 */
@Data
@ApiModel("检项查询-分页行")
public class DataPointRowRespVO {

	@ApiModelProperty("检验单ID")
	private Long inspectionOrderId;

	@ApiModelProperty("检验单号")
	private String inspectionOrderNo;

	@ApiModelProperty("请验时间")
	private LocalDateTime requestTime;

	@ApiModelProperty("数据点键 -> 值封装[键=数据点名称|数据点类型]")
	private Map<String, DataPointValue> pointNameToValue;

	@ApiModelProperty("分析项ID -> (数据点名称 -> 值)")
	private Map<Long, Map<String, String>> parameterToPointValues;
    @Data
    public static class DataPointValue {
        @ApiModelProperty("展示值")
        private String value;
        @ApiModelProperty("分析项ID")
        private Long parameterId;
        @ApiModelProperty("分析项CODE")
        private String parameterCode;
    }
}

