package com.bmos.lims2.server.inspect.entry.dto;

import com.bmos.lims2.common.enums.DataPointTypeEnum;
import com.bmos.lims2.common.enums.TaskStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import com.bmos.lims2.common.enums.ExecuteMethodEnum;

/**
 * @Description: 检验录入记录DTO（数据点级投影 + 任务级派生字段）
 * @Author: yigaohui
 * @Date: 2025/09/12 14:20
 */
@Getter
@Setter
@ApiModel("检验录入记录数据对象")
public class InspectionEntryRecordDTO {

	@ApiModelProperty("主键ID")
	private Long id;

	@ApiModelProperty("检验单ID")
	private Long inspectionOrderId;

	@ApiModelProperty("检验单号")
	private String inspectionOrderNo;

	@ApiModelProperty("任务ID")
	private Long taskId;

	@ApiModelProperty("方案ID")
	private Long schemeId;

	@ApiModelProperty("方案版本ID")
	private Long schemeVersionId;

	@ApiModelProperty("方案实验包配置ID")
	private Long packageId;

	@ApiModelProperty("方案检验项目配置ID")
	private Long itemConfigId;

	@ApiModelProperty("方案分析项配置ID")
	private Long parameterConfigId;

	@ApiModelProperty("数据点配置ID")
	private Long dataPointConfigId;

	@ApiModelProperty("数据点名称")
	private String dataPointName;

	@ApiModelProperty("数据点ID")
	private Long dataPointId;

	@ApiModelProperty("数据点类型")
	private DataPointTypeEnum pointType;

	private String dateStyle;

	@ApiModelProperty("时间类型显示格式（仅当pointType为TIME时有效）")
	private String timeFormat;

	@ApiModelProperty("检验项目ID")
	private Long inspectItemId;

	@ApiModelProperty("检验项目编码")
	private String inspectItemCode;

	@ApiModelProperty("检验项目名称")
	private String inspectItemName;

	@ApiModelProperty("分析项ID")
	private Long parameterId;

	@ApiModelProperty("分析项编码")
	private String parameterCode;

	@ApiModelProperty("分析项名称")
	private String parameterName;

	@ApiModelProperty("文本值/选项值")
	private String valueText;

	@ApiModelProperty("选项列表（当pointType为OPTION时返回）")
	private List<String> options;

	@ApiModelProperty(value = "选项JSON（内部使用）", hidden = true)
	private String optionsJson;

    @ApiModelProperty("数值型结果（字符串）")
    private String valueNumber;

	@ApiModelProperty("检验时间")
	private java.time.LocalDateTime testTime;

	@ApiModelProperty("录入人ID")
	private String operatorId;

	@ApiModelProperty("录入人姓名")
	private String operatorName;

	@ApiModelProperty("是否判定异常")
	private Boolean abnormal;

	@ApiModelProperty("标准判定规则")
	private String standardRule;


	/**
	 * 分析方法ID
	 */
	private Long recordId;

	@ApiModelProperty("判定结果")
	private String judgedResult;

	@ApiModelProperty("判定时间")
	private java.time.LocalDateTime judgedTime;

	@ApiModelProperty("复核人")
	private String reviewedBy;

	@ApiModelProperty("复核时间")
	private java.time.LocalDateTime reviewedTime;

	@ApiModelProperty("录入时间")
	private java.time.LocalDateTime entryTime;

	@ApiModelProperty("是否可上报")
	private Boolean isReportable;

	@ApiModelProperty("是否可执行")
	private Boolean isExecutable;

	@ApiModelProperty("检验人ID")
	private Long ownerId;

	@ApiModelProperty("检验人名称")
	private String ownerName;

	@ApiModelProperty("任务状态")
	private TaskStatusEnum status;

	@ApiModelProperty("执行方式")
	private ExecuteMethodEnum executeMethod;
}
