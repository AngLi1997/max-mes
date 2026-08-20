package com.bmos.lims2.web.inspect.order.vo.req;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @Description: 检验单依赖下拉-请求VO
 * @Author: yigaohui
 * @Date: 2025/09/05 10:35
 */
@Data
@ApiModel("检验单选项请求")
public class InspectionOrderOptionReqVO extends BasePage {

	@ApiModelProperty(value = "检品ID", required = true)
	@NotNull
	private Long materialId;

	@ApiModelProperty(value = "方案版本ID，可选")
	private Long schemeVersionId;

	@ApiModelProperty(value = "请验开始时间，可选")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime requestStartTime;

	@ApiModelProperty(value = "请验结束时间，可选")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime requestEndTime;
}


