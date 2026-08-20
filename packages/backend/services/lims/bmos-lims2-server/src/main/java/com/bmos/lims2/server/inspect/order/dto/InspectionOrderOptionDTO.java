package com.bmos.lims2.server.inspect.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @Description: 检验单下拉/列表依赖查询-返回DTO
 * @Author: yigaohui
 * @Date: 2025/09/05 10:30
 */
@Getter
@Setter
@ApiModel("检验单选项返回数据")
public class InspectionOrderOptionDTO {

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


