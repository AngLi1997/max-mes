package com.bmos.mes.service.execute.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.service.execute.dto.*;
import com.bmos.mes.service.execute.service.ExecuteFormDataService;
import com.bmos.mes.service.execute.service.impl.ExecuteRecordCopyServiceImpl;
import com.bmos.mes.service.execute.vo.*;
import com.bmos.mes.service.utils.DateCalculateVO;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.web.validation.InsertValidation;
import com.bmos.web.validation.UpdateValidation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/execute")
@Api(tags = "生产执行相关接口")
@Validated
public class ExecuteFormDataController {

    @Autowired
    private ExecuteFormDataService executeFormDataService;

    @Autowired
    private ExecuteRecordCopyServiceImpl executeRecordCopyService;

    @PostMapping("/batch/save")
    @ApiOperation("批量保存")
    public ResponseInfo<Void> saveBatch(@Validated(value = {InsertValidation.class}) @RequestBody FormDataBatchSaveDTO dto) {
        executeFormDataService.saveBatch(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/calculation/preview")
    @ApiOperation("计算预览")
    public ResponseInfo<List<FormDataItemVO>> calculationPreview(@Validated(value = {UpdateValidation.class}) @RequestBody FormDataBatchSaveDTO dto) {
        return ResponseInfo.success(executeFormDataService.getCalculationPreview(dto));
    }

    @PostMapping("/modify")
    @ApiOperation("修改组件值")
    public ResponseInfo<Void> modify(@Validated @RequestBody FormDataModifyDTO dto) {
        executeFormDataService.modify(dto);
        return ResponseInfo.success();
    }

    /**
     * 更新组件数据值 操作记录为更新
     * 提供给 签名组件的继续签名操作使用
     */
    @PostMapping("/update")
    @ApiOperation("更新组件值(修订用修订接口)")
    public ResponseInfo<Void> update(@Validated @RequestBody FormDataUpdateDTO dto) {
        executeFormDataService.update(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/discard")
    @ApiOperation("作废记录项")
    public ResponseInfo<Void> discardRecordItem(@Validated @RequestBody FormDataDiscardDTO dto) {
        executeFormDataService.discardRecordItem(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/field/data/list")
    @ApiOperation("查询组件值集合")
    public ResponseInfo<List<FormDataVO>> getList(@Validated FormDataListQueryDTO dto) {
        return ResponseInfo.success(executeFormDataService.getFieldList(dto));
    }

    @GetMapping("/item/latest/data")
    @ApiOperation("查询某个记录项最新值")
    public ResponseInfo<List<FormDataItemVO>> getRecordItemLatestData(@Validated RecordItemLatestDataQueryDTO dto) {
        return ResponseInfo.success(executeFormDataService.getRecordItemLatestData(dto));
    }

    @PutMapping("/lock/step")
    @ApiOperation("锁定工序步骤")
    public ResponseInfo<Void> lockProcedureStep(@Validated @RequestBody LockStepDTO dto) {
        executeFormDataService.lockProcedureStep(dto);
        return ResponseInfo.success();
    }

    @DeleteMapping("/unLock/step")
    @ApiOperation("解锁工序步骤")
    public ResponseInfo<Void> unLockProcedureStep(@Validated @RequestBody LockStepDTO dto) {
        executeFormDataService.unLockProcedureStep(dto);
        return ResponseInfo.success();
    }


    @GetMapping("/copyVersion/list")
    @ApiOperation("查询记录复制版本列表")
    public ResponseInfo<List<CopyRecordItemVO>> getCopyVersionList(@Validated RecordCopyQueryDTO dto) {
        return ResponseInfo.success(executeFormDataService.getCopyVersionList(dto));
    }

    @PostMapping("/copy/recordItem")
    @ApiOperation("复制记录项")
    public ResponseInfo<Long> copyRecordItem(@Validated @RequestBody RecordCopySaveDTO dto) {
        executeFormDataService.copyRecordItem(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/intact/merge/list")
    @ApiOperation("查询完整合并的批记录")
    public ResponseInfo<List<IntactFormDataVO>> getIntactMergedList(@Validated IntactMergeListQueryDTO dto) {
        return ResponseInfo.success(executeFormDataService.getIntactMergedList(dto));
    }

    @GetMapping("/subsidiary/list")
    @ApiOperation("查询辅助记录归档")
    public ResponseInfo<List<SubsidiaryRecordDocVO>> getSubsidiaryDocList(@NotNull Long id) {
        return ResponseInfo.success(executeFormDataService.getSubsidiaryDocList(id));
    }

    @GetMapping("/server/time")
    @ApiOperation("查询执行服务器时间")
    public ResponseInfo<String> getServerTime() {
        return ResponseInfo.success(executeFormDataService.getServerTime());
    }

    @PostMapping("/business/save/batch")
    @ApiOperation("业务组件数据批量保存")
    public ResponseInfo<Void> saveBusinessComponentsData(@RequestBody @Validated BusinessComponentBatchSaveDTO dto) {
        executeFormDataService.saveBusinessComponentsData(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/business/saved")
    @ApiOperation("校验是否已触发过业务组件")
    public ResponseInfo<Boolean> checkBusinessComponentsSaved(@Validated BusinessComponentsCheckSavedDTO dto){
        return ResponseInfo.success(executeFormDataService.checkBusinessComponentsSaved(dto));
    }

    @GetMapping("/calculate/date")
    @ApiOperation("计算时间差")
    public ResponseInfo<DateCalculateVO> getCalculateDate(@Validated CalculateDateDTO dto){
        return ResponseInfo.success(executeFormDataService.getCalculateDate(dto));
    }

    @PostMapping("/upload")
    @ApiOperation("拍照组件拍照上传")
    @ApiImplicitParam(value = "文件流",name = "file",required = true)
    public ResponseInfo<AttachmentVO> upload(MultipartFile file){
        return ResponseInfo.success(executeFormDataService.upload(file));
    }

    @GetMapping("/picture/list")
    @ApiOperation("查询拍照组件上传详情数据")
    @ApiImplicitParam(value = "value数据",name = "value")
    public ResponseInfo<String> pictureList(String value){
        return ResponseInfo.success(executeFormDataService.pictureList(value));
    }

    @GetMapping("/field/trend/analysis")
    @ApiOperation("数值组件趋势分析")
    public ResponseInfo<TrendAnalysisVO> componentTrendAnalysis(@Validated ComponentTrendAnalysisDTO dto){
        return ResponseInfo.success(executeFormDataService.componentTrendAnalysis(dto));
    }


    @GetMapping("/procedure/view")
    @ApiOperation("查看工序")
    public ResponseInfo<List<ProcedureViewVO>> queryProcedureViewVO(@Validated ProcedureViewQueryDTO dto) {
        return ResponseInfo.success(executeFormDataService.queryProcedureViewVO(dto));
    }

    @GetMapping("/stepVersionList")
    @ApiOperation("查看工序:工步存在的班次列表")
    public ResponseInfo<List<ChangeTeamRecordCopyChangeTeamVO>> queryStepChangeTeamList(@Validated ProcedureStepChangeNumberQueryDTO dto) {
        return ResponseInfo.success(executeRecordCopyService.queryStepChangeTeamList(dto));
    }

    @GetMapping("/copyVersion/existedList")
    @ApiOperation("查询已存在的记录复制版本列表")
    public ResponseInfo<List<CopyRecordItemVO>> getExistsCopyVersionList(@Validated RecordCopyQueryDTO dto) {
        return ResponseInfo.success(executeFormDataService.getExistedCopyVersionList(dto));
    }
    @GetMapping("/plan/modify/list")
    @ApiOperation("查询生产计划修订记录")
    public ResponseInfo<CommonPage<PlanFieldModifyVO>> queryPlanModifyList(@Validated PlanFieldModifyQueryDTO dto) {
        return ResponseInfo.success(executeFormDataService.queryPlanModifyList(dto));
    }
}
