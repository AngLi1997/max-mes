package com.bmos.lims2.web.eln.pic;

import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.eln.entry.service.ExecuteAttachmentService;
import com.bmos.lims2.server.eln.entry.service.ExecuteFormDataService;
import com.bmos.lims2.server.eln.entry.vo.AttachmentVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @className: PictureComponentController
 * @author: yigaohui
 * @date: 2025/11/20 14:12
 * @Version: 1.0
 * @description:
 */

@RestController
@RequestMapping("/app/eln/pic")
@Api(tags = "拍照组件相关接口")
public class PictureComponentController {

    @Autowired
    private ExecuteAttachmentService executeAttachmentService;

    @Autowired
    private ExecuteFormDataService executeFormDataService;

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
}
