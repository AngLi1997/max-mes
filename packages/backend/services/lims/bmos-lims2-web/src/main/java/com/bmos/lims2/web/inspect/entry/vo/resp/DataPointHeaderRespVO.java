package com.bmos.lims2.web.inspect.entry.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description: 检项查询-表头返回VO
 * @Author: yigaohui
 * @Date: 2025/09/05 10:55
 */
@Data
@ApiModel("检项查询-表头")
public class DataPointHeaderRespVO {


	@ApiModelProperty("分组表头：分析项-数据点分组（用于前端渲染一二级表头）")
	private List<HeaderGroup> headerGroups;

	@Data
    public static class HeaderLevel1 {
        private Long parameterId;
        private String parameterCode;
        private String parameterName;
    }

	@Data
	public static class HeaderLevel2 {
		private String pointName;
		private String pointType;
	}

	@Data
	public static class HeaderGroup {
		private Long parameterId;
		private String parameterCode;
		private String parameterName;
		private List<HeaderLevel2> dataPoints;
	}
}


