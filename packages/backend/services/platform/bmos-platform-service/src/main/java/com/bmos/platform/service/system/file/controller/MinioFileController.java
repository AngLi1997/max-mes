package com.bmos.platform.service.system.file.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.unit.DataSizeUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.util.id.IdUtils;
import com.bmos.platform.service.config.web.MinioProperties;
import com.bmos.platform.service.system.file.vo.FileVO;
import io.minio.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

@Api(tags = "管理后台 - 文件上传")
@RestController
@RequestMapping("/file")
public class MinioFileController {
    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinioProperties minioProperties;

    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public ResponseInfo<FileVO> upload(@RequestPart("file") MultipartFile file) throws Exception {
        // 上传
        String path = IdUtils.getSimpleUUID(); // 文件名，使用 UUID 随机
        String yyyyMM = LocalDate.now().format(DatePattern.SIMPLE_MONTH_FORMATTER);
        String url = String.format("%s/%s", yyyyMM, path);
        minioClient.putObject(PutObjectArgs.builder()
            .bucket(minioProperties.getBucket()) // 存储桶
            .object(url) // 文件名
            .stream(file.getInputStream(), file.getSize(), -1) // 文件内容
            .contentType(file.getContentType()) // 文件类型
            .build());
        // 拼接路径
        return ResponseInfo.success(
            FileVO.builder()
                .url(url)
                .newFilemame(path)
                .oldFilename(file.getOriginalFilename())
                .fileSize(DataSizeUtil.format(file.getSize()))
                .build()
        );
    }

    @DeleteMapping("/delete")
    @ApiOperation("文件删除")
    public ResponseInfo<Void> delete(@RequestParam("path") String path) throws Exception {
        minioClient.removeObject(RemoveObjectArgs.builder()
            .bucket(minioProperties.getBucket()) // 存储桶
            .object(path) // 文件名
            .build());
        return ResponseInfo.success();
    }

    @GetMapping("/download")
    @ApiOperation("文件下载")
    public void download(@RequestParam("path") String path, HttpServletResponse response) throws Exception{
        GetObjectResponse object = minioClient.getObject(GetObjectArgs.builder()
                .bucket(minioProperties.getBucket())
                .object(path)
                .build());
        IoUtil.copy(object, response.getOutputStream());
    }
}
