package com.bmos.lims2.server.inspect.entry.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description: 检项查询-数据点值分页查询参数
 * @Author: yigaohui
 * @Date: 2025/09/05 10:50
 */
@Getter
@Setter
@ApiModel("检项查询-数据点值分页查询参数")
public class DataPointQueryPageReqDTO extends BasePage {

	@ApiModelProperty(value = "检品ID", required = true)
	private Long materialId;

	@ApiModelProperty(value = "方案ID", required = true)
	private Long schemeId;

	@ApiModelProperty(value = "请验开始时间")
	private LocalDateTime requestStartTime;

	@ApiModelProperty(value = "请验结束时间")
	private LocalDateTime requestEndTime;

	@ApiModelProperty(value = "检验单ID集合")
	private List<Long> inspectionOrderIds;
}


