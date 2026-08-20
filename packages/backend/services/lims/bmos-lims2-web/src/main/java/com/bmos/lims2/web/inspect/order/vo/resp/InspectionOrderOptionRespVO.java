package com.bmos.lims2.web.inspect.order.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description: 检验单依赖下拉-返回VO
 * @Author: yigaohui
 * @Date: 2025/09/05 10:35
 */
@Data
@ApiModel("检验单选项返回")
public class InspectionOrderOptionRespVO {

	@ApiModelProperty("检验单ID")
	private Long id;

	@ApiModelProperty("检验单号")
	private String orderNo;

	@ApiModelProperty("请验时间")
	private LocalDateTime requestTime;

	@ApiModelProperty("检品名称")
	private String materialName;

	@ApiModelProperty("方案版本号")
	private String schemeVersionNo;
}


