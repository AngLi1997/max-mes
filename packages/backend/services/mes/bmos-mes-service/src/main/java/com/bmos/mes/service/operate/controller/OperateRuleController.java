package com.bmos.mes.service.operate.controller;

import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.common.constant.RecordConstant;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.config.minio.MinioFileClient;
import com.bmos.mes.service.operate.dto.OperateRulePageDTO;
import com.bmos.mes.service.operate.dto.SaveOperateRuleDTO;
import com.bmos.mes.service.operate.service.OperateRuleService;
import com.bmos.mes.service.operate.vo.OperateRulePageVO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author renjinguang
 */
@RestController
@RequestMapping("/operate/rule")
@Api(tags = "操作规程文件管理相关接口")
@Validated
public class OperateRuleController {

    @Autowired
    private OperateRuleService ruleService;

    @GetMapping("/page/list")
    @ApiOperation(value = "查询操作规程管理列表")
    public ResponseInfo<CommonPage<OperateRulePageVO>> getRecordPage(@Validated OperateRulePageDTO dto) {
        return ResponseInfo.success(ruleService.getRecordPage(dto));
    }

    @PostMapping("/save")
    @ApiOperation(value = "新增文件")
    public ResponseInfo<Void> save(@RequestBody SaveOperateRuleDTO dto) {
        ruleService.save(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/upload")
    @ApiOperation(value = "文件上传")
    public ResponseInfo<String> upload(MultipartFile file){
        return ResponseInfo.success(ruleService.upload(file));
    }

    @PostMapping("/preview")
    @ApiOperation(value = "文件浏览")
    public void upload(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
        try (OutputStream outputStream = response.getOutputStream()) {
            String originalFilename = file.getOriginalFilename();
            if (!originalFilename.contains(RecordConstant.UPLOAD)) {
                throw new BmosException(MesResponseCode.OPERATE_UPLOAD_ERROR);
            }
            InputStream inputStream = file.getInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            // 刷新输出流
            outputStream.flush();
            // 关闭流
            inputStream.close();
        } catch (Exception e) {
            throw new BmosException(MesResponseCode.OPERATE_UPLOAD_ERROR);
        }
    }
}
