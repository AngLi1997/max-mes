package com.bmos.mes.service.execute.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.service.execute.dto.ExecuteAttachmentQueryDTO;
import com.bmos.mes.service.execute.dto.ExecuteAttachmentAddRemarkDTO;
import com.bmos.mes.service.execute.dto.ExecuteAttachmentUploadDTO;
import com.bmos.mes.service.execute.service.ExecuteAttachmentService;
import com.bmos.mes.service.execute.vo.AttachmentVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/execute/attachment")
@Api(tags = "生产执行附件")
@Validated
public class ExecuteAttachmentController {

    @Autowired
    private ExecuteAttachmentService executeAttachmentService;

    @PostMapping("/upload")
    @ApiOperation("生产执行附件上传")
    public ResponseInfo<AttachmentVO> upload(@Validated ExecuteAttachmentUploadDTO dto){
        return ResponseInfo.success(executeAttachmentService.upload(dto));
    }

    @PostMapping("/addRemark")
    @ApiOperation("拍照取证编辑备注")
    public ResponseInfo<Void> addRemark(@Validated @RequestBody ExecuteAttachmentAddRemarkDTO dto){
        executeAttachmentService.addRemark(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/list")
    @ApiOperation("查询记录项附件")
    public ResponseInfo<List<AttachmentVO>> getList(@Validated ExecuteAttachmentQueryDTO dto) {
        return ResponseInfo.success(executeAttachmentService.getList(dto));
    }
}
