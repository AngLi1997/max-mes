package com.bmos.lims2.web.inspect.entry.vo.req;

import com.bmos.mybatis.page.BasePage;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description: 检项查询-数据点值分页查询参数
 * @Author: yigaohui
 * @Date: 2025/09/05 10:55
 */
@Data
@ApiModel("检项查询-分页查询参数")
public class DataPointQueryPageReqVO extends BasePage {

	@ApiModelProperty(value = "检品ID", required = true)
	private Long materialId;

	@ApiModelProperty(value = "方案ID", required = true)
	private Long schemeId;

	@ApiModelProperty(value = "请验开始时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private LocalDateTime requestStartTime;

	@ApiModelProperty(value = "请验结束时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private LocalDateTime requestEndTime;

	@ApiModelProperty(value = "检验单ID集合")
	private List<Long> inspectionOrderIds;
}


