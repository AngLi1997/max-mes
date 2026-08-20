package com.bmos.lims2.server.inspect.order.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @Description: 检验单下拉/列表依赖查询-请求DTO
 * @Author: yigaohui
 * @Date: 2025/09/05 10:30
 */
@Getter
@Setter
@ApiModel("检验单选项查询条件")
public class InspectionOrderOptionQueryDTO extends BasePage {

	@ApiModelProperty(value = "检品ID", required = true)
	private Long materialId;

	@ApiModelProperty(value = "检验方案版本ID，可选")
	private Long schemeVersionId;

	@ApiModelProperty(value = "请验开始时间，可选")
	private LocalDateTime requestStartTime;

	@ApiModelProperty(value = "请验结束时间，可选")
	private LocalDateTime requestEndTime;
}


