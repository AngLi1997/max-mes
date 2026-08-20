package com.bmos.lims2.web.eln.entry.controller;

import com.bmos.common.holder.SysUserHolder;
import com.bmos.lims2.common.enums.ExecuteMethodEnum;
import com.bmos.lims2.server.inspect.entry.dto.*;
import com.bmos.lims2.server.inspect.entry.service.AppTaskQueryService;
import com.bmos.lims2.server.inspect.review.dto.BatchReviewDTO;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.common.response.ResponseInfo;
import com.bmos.adaptor.platform.vo.UserInfoVO;
import com.bmos.mybatis.dataobject.BaseUserDO;
import com.bmos.lims2.server.platform.util.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

import com.bmos.lims2.server.task.service.TaskService;
import com.bmos.lims2.server.inspect.review.service.InspectionReviewService;

/**
 * @Description: APP任务查询接口（仅返回执行方式为 ELN 的任务）
 * @Author: yigaohui
 * @Date: 2025/11/04 11:20
 */
@RestController
@RequestMapping("/app/task")
@Api(tags = "APP-任务查询（仅ELN）")
@Validated
public class AppTaskQueryController {

    @Autowired
    private AppTaskQueryService appTaskQueryService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private InspectionReviewService inspectionReviewService;

    @ApiOperation("APP-分析项录入列表查询（仅ELN）- 子列表字段口径：与“检验单录入列表查询”的任务子列表一致")
    @PostMapping("/entry/analysis-item/page")
    public ResponseInfo<CommonPage<com.bmos.lims2.server.inspect.entry.dto.AppAnalysisItemMethodGroupDTO>> entryAnalysisItemPage(
            @ApiParam("查询条件") @RequestBody @Valid com.bmos.lims2.server.inspect.entry.vo.AppAnalysisItemEntryQueryVO queryVO) {
        com.bmos.lims2.server.inspect.entry.dto.AppAnalysisItemEntryQueryDTO queryDTO = appTaskQueryService.setAnalysisItemEntryPermission(queryVO);
        queryDTO.setExecuteMethod(ExecuteMethodEnum.ELN);
        CommonPage<com.bmos.lims2.server.inspect.entry.dto.AppAnalysisItemMethodGroupDTO> page =
                appTaskQueryService.pageAnalysisItemMethodGroup(queryDTO);
        return ResponseInfo.success(page);
    }

    @ApiOperation("APP-检验单录入列表查询（仅ELN）- 子列表字段口径：与“分析项录入列表查询”的任务子列表一致")
    @PostMapping("/entry/inspection-order/page")
    public ResponseInfo<CommonPage<com.bmos.lims2.server.inspect.entry.dto.AppInspectionOrderEntryDTO>> entryInspectionOrderPage(
            @ApiParam("查询条件") @RequestBody @Valid com.bmos.lims2.server.inspect.entry.vo.InspectionOrderEntryQueryVO queryVO) {
        InspectionOrderEntryQueryDTO queryDTO = appTaskQueryService.setInspectionOrderEntryPermission(queryVO);
        queryDTO.setExecuteMethod(ExecuteMethodEnum.ELN);
        CommonPage<com.bmos.lims2.server.inspect.entry.dto.AppInspectionOrderEntryDTO> page = appTaskQueryService.pageInspectionOrderEntry(queryDTO);
        return ResponseInfo.success(page);
    }

    @ApiOperation("APP-分析项复核列表查询（仅ELN）- 子列表字段口径：与分析项录入列表查询的任务子列表一致")
    @PostMapping("/review/analysis-item/page")
    public ResponseInfo<CommonPage<com.bmos.lims2.server.inspect.entry.dto.AppAnalysisItemMethodGroupDTO>> reviewAnalysisItemPage(
            @ApiParam("查询条件") @RequestBody @Valid com.bmos.lims2.server.inspect.entry.vo.AppAnalysisItemEntryQueryVO queryVO) {
        com.bmos.lims2.server.inspect.entry.dto.AppAnalysisItemEntryQueryDTO queryDTO = appTaskQueryService.setAnalysisItemReviewPermission(queryVO);
        queryDTO.setExecuteMethod(ExecuteMethodEnum.ELN);
        CommonPage<com.bmos.lims2.server.inspect.entry.dto.AppAnalysisItemMethodGroupDTO> page =
                appTaskQueryService.pageAnalysisItemMethodGroupForReview(queryDTO);
        return ResponseInfo.success(page);
    }

    @ApiOperation("APP-检验单复核列表查询（仅ELN）- 子列表字段口径：与检验单录入列表查询的任务子列表一致")
    @PostMapping("/review/inspection-order/page")
    public ResponseInfo<CommonPage<com.bmos.lims2.server.inspect.entry.dto.AppInspectionOrderEntryDTO>> reviewInspectionOrderPage(
            @ApiParam("查询条件") @RequestBody @Valid com.bmos.lims2.server.inspect.entry.vo.InspectionOrderEntryQueryVO queryVO) {
        InspectionOrderEntryQueryDTO queryDTO = appTaskQueryService.setInspectionOrderReviewPermission(queryVO);
        queryDTO.setExecuteMethod(ExecuteMethodEnum.ELN);
        CommonPage<com.bmos.lims2.server.inspect.entry.dto.AppInspectionOrderEntryDTO> page =
                appTaskQueryService.pageInspectionOrderForReview(queryDTO);
        return ResponseInfo.success(page);
    }

    @ApiOperation("APP-任务统计（未完成/异常/全部）（仅ELN、仅当前登录人）")
    @PostMapping("/stats")
    public ResponseInfo<EntryStatsDTO> taskStats() {
        // 不接收筛选条件，仅按当前登录人统计（服务层内部固定ELN并注入当前用户）
        EntryStatsDTO stats = appTaskQueryService.taskStats();
        return ResponseInfo.success(stats);
    }

    @ApiOperation("APP-复核任务数量统计（仅ELN，班组内待复核总数）")
    @PostMapping("/review/stats")
    public ResponseInfo<Long> reviewTaskStats() {
        return ResponseInfo.success(appTaskQueryService.reviewTaskStats());
    }

    @ApiOperation("APP-ELN完成任务（录入完成，置为待复核）")
    @PostMapping("/entry/complete")
    public ResponseInfo<com.bmos.lims2.server.inspect.entry.vo.AppTaskStatusRespVO> completeElnTask(
            @ApiParam("完成请求") @RequestBody @Valid com.bmos.lims2.server.inspect.entry.vo.AppTaskCompleteReqVO reqVO) {
        com.bmos.lims2.common.enums.TaskStatusEnum status = taskService.completeTaskForAppEln(reqVO.getTaskId());
        com.bmos.lims2.server.inspect.entry.vo.AppTaskStatusRespVO respVO = new com.bmos.lims2.server.inspect.entry.vo.AppTaskStatusRespVO(reqVO.getTaskId(), status);
        return ResponseInfo.success(respVO);
    }

    @ApiOperation("APP-ELN复核任务（支持批量复核通过/不通过）")
    @PostMapping("/review/approve")
    public ResponseInfo<Void> approveElnTask(
            @ApiParam("复核请求") @RequestBody @Valid BatchReviewDTO req) {
        inspectionReviewService.batchReview(req.getTaskIds(), StringUtils.isEmpty(req.getReviewerId()) ? SysUserHolder.getUser().getUserId() : req.getReviewerId(), req.getApprove(), req.getReason());
        return ResponseInfo.success();
    }

    @ApiOperation("APP-任务详情查询（按任务ID，含检验时间、复核人/审核人名称-编码等全量信息）")
    @PostMapping("/detail")
    public ResponseInfo<AppTaskDetailDTO> taskDetail(
            @ApiParam("任务ID") @RequestParam @NotNull Long taskId) {
        return ResponseInfo.success(appTaskQueryService.getTaskDetail(taskId));
    }

    @ApiOperation("APP-查询任务所属班组的所有成员")
    @PostMapping("/team-members")
    public ResponseInfo<List<UserInfoVO>> listTaskTeamMembers(
            @ApiParam("任务ID") @RequestParam @NotNull Long taskId) {
        List<String> userIds = appTaskQueryService.listTeamMembersByTaskId(taskId);
        List<UserInfoVO> result = userIds.stream().map(uid -> {
            BaseUserDO user = UserUtils.getUser(uid);
            UserInfoVO vo = new UserInfoVO();
            vo.setUserId(uid);
            if (user != null) {
                vo.setUserName(user.getUserName());
                vo.setLoginName(user.getLoginName());
            }
            return vo;
        }).collect(Collectors.toList());
        return ResponseInfo.success(result);
    }
}


