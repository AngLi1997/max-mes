package com.bmos.adaptor.file.feign;

import com.bmos.adaptor.file.vo.FileVO;
import com.bmos.common.response.ResponseInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "bmos-platform-service", contextId = "bmos-platform-file")
public interface FileManagerOpenFeign {

    @PostMapping(value = "/api/app/platform/file/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseInfo<FileVO> upload(@RequestPart("file") MultipartFile file);
}
