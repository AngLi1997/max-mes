package com.bmos.lims2.server.inspect.audit.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ApiModel("样品审核不通过提交参数")
public class SampleAuditRejectDTO {

	@ApiModelProperty("检验单ID")
	@NotNull
	private Long inspectionOrderId;

	@ApiModelProperty("需要退回的录入任务ID集合")
	@NotEmpty
	private List<Long> taskIds;

	@ApiModelProperty("退回原因")
	private String reason;

	@ApiModelProperty("工作流流程实例ID")
	@NotBlank
	private String processInstanceId;

	@ApiModelProperty("工作流任务ID")
	@NotBlank
	private String taskId;

	@ApiModelProperty("审批意见（传给工作流）")
	private String comment;
}
