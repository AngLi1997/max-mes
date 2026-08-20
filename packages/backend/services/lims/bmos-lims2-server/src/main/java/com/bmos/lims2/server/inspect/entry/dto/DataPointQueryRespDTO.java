package com.bmos.lims2.server.inspect.entry.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Description: 检项查询-表头+数据返回DTO
 * @Author: yigaohui
 * @Date: 2025/09/05 10:50
 */
@Getter
@Setter
@ApiModel("检项查询-表头+数据结果")
public class DataPointQueryRespDTO {

	@ApiModelProperty("分组表头：分析项-数据点分组（用于前端渲染一二级表头）")
	private List<HeaderGroup> headerGroups;

	@ApiModelProperty("数据部分：按录入值分页的行")
	private List<DataRow> rows;

	@Getter
	@Setter
	public static class HeaderLevel1 {
		private Long parameterId;
		private String parameterCode;
		private String parameterName;
	}

	@Getter
	@Setter
	public static class DataRow {
		private Long inspectionOrderId;
		private String inspectionOrderNo;
		private java.time.LocalDateTime requestTime;
		// 平铺结构：键=数据点名称|数据点类型，值=值对象
		private java.util.Map<String, DataPointValueDTO> pointNameToValue;
		// 新结构：分析项ID -> (数据点名称 -> 值)
		private java.util.Map<Long, java.util.Map<String, String>> parameterToPointValues;
	}

	@Getter
	@Setter
	public static class HeaderLevel2 {
		private String pointName;
		private String pointType;
	}

	@Getter
	@Setter
	public static class HeaderGroup {
		private Long parameterId;
		private String parameterCode;
		private String parameterName;
		private List<HeaderLevel2> dataPoints;
	}

	@Getter
	@Setter
	public static class DataPointValueDTO {
		private String value;
		private Long parameterId;
		private String parameterCode;
	}
}



