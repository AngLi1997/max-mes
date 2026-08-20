package com.bmos.lims2.web.eln.entry.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.eln.entry.dto.*;
import com.bmos.lims2.server.eln.entry.service.ExecuteAttachmentService;
import com.bmos.lims2.server.eln.entry.service.ExecuteFormDataAnnotationService;
import com.bmos.lims2.server.eln.entry.service.ExecuteFormDataService;
import com.bmos.lims2.server.eln.entry.vo.FormDataAnnotationVO;
import com.bmos.lims2.server.eln.entry.vo.FormDataItemVO;
import com.bmos.lims2.server.eln.entry.vo.FormDataVO;
import com.bmos.lims2.server.inspect.entry.dto.AppInspectionOrderEntryDTO;
import com.bmos.lims2.server.inspect.entry.service.AppTaskQueryService;
import com.bmos.lims2.server.inspect.scheme.service.TimeDurationService;
import com.bmos.lims2.web.eln.entry.vo.request.FormDataAnnotationSaveReqVO;
import com.bmos.lims2.web.eln.entry.vo.request.FormDataBatchSaveReqVO;
import com.bmos.lims2.web.eln.entry.vo.request.FormDataModifyReqVO;
import com.bmos.lims2.web.inspect.scheme.vo.request.TimeDurationPreviewReqVO;
import com.bmos.lims2.web.inspect.scheme.vo.response.TimeDurationPreviewRespVO;
import com.bmos.web.validation.InsertValidation;
import com.bmos.web.validation.UpdateValidation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @className: ElnEntryController
 * @author: yigaohui
 * @date: 2025/10/31 15:20
 * @Version: 1.0
 * @description:
 */

@RestController
@RequestMapping("/app/eln/entry")
@Api(tags = "eln-执行数据录入")
public class ElnEntryController {

    @Autowired
    private TimeDurationService timeDurationService;

    @Autowired
    private ExecuteAttachmentService executeAttachmentService;

    @Autowired
    private ExecuteFormDataService executeFormDataService;
    @Autowired
    private ExecuteFormDataAnnotationService executeFormDataAnnotationService;
    @Autowired
    private AppTaskQueryService appTaskQueryService;


    @PostMapping("/time/preview")
    @ApiOperation("时间类型数据点时长预览计算")
    public ResponseInfo<TimeDurationPreviewRespVO> previewTimeDuration(@RequestBody @Validated TimeDurationPreviewReqVO reqVO) {
        com.bmos.lims2.server.eln.record.dto.DateCalculateVO vo = timeDurationService.preview(
                reqVO.getStartTime(), reqVO.getEndTime(), reqVO.getCalculateType(), reqVO.getRoundingUp());
        TimeDurationPreviewRespVO resp = new TimeDurationPreviewRespVO();
        resp.setCalculateResult(vo.getCalculateResult());
        resp.setTimeSeconds(vo.getTimeSeconds());
        return ResponseInfo.success(resp);
    }

    @GetMapping("/server/time")
    @ApiOperation("查询执行服务器时间")
    public ResponseInfo<String> getServerTime() {
        return ResponseInfo.success(LocalDateTimeUtil.formatNormal(LocalDateTime.now()));
    }


    @PostMapping("/addRemark")
    @ApiOperation("拍照取证编辑备注")
    public ResponseInfo<Void> addRemark(@Validated @RequestBody ExecuteAttachmentAddRemarkDTO dto){
        executeAttachmentService.addRemark(dto);
        return ResponseInfo.success();
    }


    @PostMapping("/upload")
    @ApiOperation("生产执行附件上传")
    public ResponseInfo<AttachmentDTO> upload(@Validated ExecuteAttachmentUploadDTO dto){
        return ResponseInfo.success(executeAttachmentService.upload(dto));
    }

    @GetMapping("/download")
    @ApiOperation("生产执行附件下载")
    public void download(@Validated ExecuteAttachmentDownloadDTO dto, HttpServletResponse response) {
        executeAttachmentService.download(dto, response);
    }


    @GetMapping("/list")
    @ApiOperation("查询记录项附件")
    public ResponseInfo<List<AttachmentDTO>> getList(@Validated ExecuteAttachmentQueryDTO dto) {
        return ResponseInfo.success(executeAttachmentService.getList(dto));
    }


    @PostMapping("/batch/save")
    @ApiOperation("批量保存")
    public ResponseInfo<Void> saveBatch(@Validated(value = {InsertValidation.class}) @RequestBody FormDataBatchSaveReqVO vo) {
        FormDataBatchSaveDTO dto = BeanUtil.copyProperties(vo, FormDataBatchSaveDTO.class);
        if (vo.getItems() != null) {
            dto.setItems(BeanUtil.copyToList(vo.getItems(), FormDataBatchSaveItemDTO.class));
        }
        executeFormDataService.saveBatch(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/business/save/batch")
    @ApiOperation("业务组件数据批量保存")
    public ResponseInfo<Void> saveBusinessComponentsData(@RequestBody @Validated BusinessComponentBatchSaveDTO dto) {
//        executeFormDataService.saveBusinessComponentsData(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/calculation/preview")
    @ApiOperation("计算预览")
    public ResponseInfo<List<FormDataItemVO>> calculationPreview(@Validated(value = {UpdateValidation.class}) @RequestBody FormDataBatchSaveReqVO vo) {
        FormDataBatchSaveDTO dto = BeanUtil.copyProperties(vo, FormDataBatchSaveDTO.class);
        if (vo.getItems() != null) {
            dto.setItems(BeanUtil.copyToList(vo.getItems(), FormDataBatchSaveItemDTO.class));
        }
        return ResponseInfo.success(executeFormDataService.getCalculationPreview(dto));
    }

    @PostMapping("/modify")
    @ApiOperation("修改组件值")
    public ResponseInfo<Void> modify(@Validated @RequestBody FormDataModifyReqVO vo) {
        FormDataModifyDTO dto = BeanUtil.copyProperties(vo, FormDataModifyDTO.class);
        executeFormDataService.modify(dto);
        return ResponseInfo.success();
    }


    @GetMapping("/field/data/list")
    @ApiOperation("查询组件值集合")
    public ResponseInfo<List<FormDataVO>> getList(@Validated FormDataListQueryDTO dto) {
        return ResponseInfo.success(executeFormDataService.getFieldList(dto));
    }

    @PostMapping("/annotation/save")
    @ApiOperation("异常批注保存")
    public ResponseInfo<Void> saveAnnotation(@RequestBody @Validated FormDataAnnotationSaveReqVO vo) {
        FormDataAnnotationSaveDTO dto = BeanUtil.copyProperties(vo, FormDataAnnotationSaveDTO.class);
        executeFormDataAnnotationService.save(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/field/annotation/list")
    @ApiOperation("查询组件批注集合")
    public ResponseInfo<List<FormDataAnnotationVO>> getAnnotationList(@Validated FormDataListQueryDTO dto) {
        return ResponseInfo.success(executeFormDataAnnotationService.getAnnotationList(dto));
    }

    @GetMapping("/item/latest/data")
    @ApiOperation("查询某个记录项最新值")
    public ResponseInfo<List<FormDataItemVO>> getRecordItemLatestData(@Validated RecordItemLatestDataQueryDTO dto) {
        return ResponseInfo.success(executeFormDataService.getRecordItemLatestData(dto));
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

    /**
     * 通过检验单ID查询该单下所有ELN任务，返回"检验项目-分析项"的树结构
     * 子节点任务结构与ELN分析项录入时的任务结构一致
     */
    @ApiOperation("按检验单ID查询ELN任务树（检验项目-分析项）")
    @GetMapping("/tree/{inspectionOrderId}")
    public ResponseInfo<List<AppInspectionOrderEntryDTO.InspectItemTaskGroupDTO>> elnTaskTreeByInspectionOrder(
            @ApiParam(value = "检验单ID", required = true) @PathVariable Long inspectionOrderId,
            @ApiParam(value = "当前任务ID（可选，如果传入则确保该任务包含在返回结果中）") @RequestParam(required = false) Long taskId) {
        List<AppInspectionOrderEntryDTO.InspectItemTaskGroupDTO> data =
                appTaskQueryService.listElnTaskTreeByInspectionOrder(inspectionOrderId, taskId);
        return ResponseInfo.success(data);
    }

}
