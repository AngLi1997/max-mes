package com.bmos.lims2.server.inspect.entry.dto;

import com.bmos.lims2.server.platform.util.UserUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description: APP 检验单录入（母列表）
 * @Author: yigaohui
 * @Date: 2025/11/27 00:00
 */
@Getter
@Setter
@ApiModel("APP-检验单录入（母列表）")
public class AppInspectionOrderEntryDTO {

    @ApiModelProperty("检验单ID")
    private Long inspectionOrderId;

    @ApiModelProperty("检验单编号")
    private String orderNo;

    @ApiModelProperty("批号")
    private String batchNo;

    @ApiModelProperty("请验时间")
    private LocalDateTime requestTime;

    @ApiModelProperty("请验人ID")
    private String requestUserId;

    @ApiModelProperty("请验人名称")
    private String requestUserName;

    @ApiModelProperty("检品名称")
    private String materialName;

    @ApiModelProperty("检品编码")
    private String materialCode;

    @ApiModelProperty("检品规格")
    private String materialSpec;

    @ApiModelProperty("该检验单下的任务列表")
    private List<AppTaskEntryItemDTO> inspectionTasks;

    @ApiModelProperty("请验单自定义字段")
    private List<com.bmos.lims2.server.inspect.order.dto.CustomFieldValueDTO> customFields;

    @ApiModelProperty("按检验项目分组的任务列表")
    private List<InspectItemTaskGroupDTO> inspectItemTaskGroups;

    public String getRequestUserName() {
        if (requestUserId == null) {
            return null;
        }
        if ("system".equalsIgnoreCase(requestUserId)) {
            return "system";
        }
        return UserUtils.getUserDisplayName(requestUserId);
    }

    @Getter
    @Setter
    @ApiModel("APP-检验项目任务分组")
    public static class InspectItemTaskGroupDTO {

        @ApiModelProperty("检验项目ID")
        private Long inspectItemId;

        @ApiModelProperty("检验项目名称")
        private String inspectItemName;

        @ApiModelProperty("检验项目编码")
        private String inspectItemCode;

        @ApiModelProperty("该检验项目下的任务列表")
        private List<AppTaskEntryItemDTO> tasks;
    }
}

