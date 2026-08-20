package com.bmos.platform.facade.system.user.feign;

import com.bmos.common.response.ResponseInfo;
import com.bmos.platform.facade.system.user.dto.UserSignSaveFeignDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * 手写签名feign接口
 */
@FeignClient(name = "bmos-platform-service", contextId = "platform-system-user-sign")
public interface UserSignFeign {


    /**
     * 保存当前登陆人的手写签名组件
     *
     * @param dto
     * @return
     */
    @PostMapping("/api/app/platform/feign/user/sign/save")
    ResponseInfo<String> saveUserSign(@RequestBody UserSignSaveFeignDTO dto) ;

    /**
     * 获取当前登录人的签名地址
     * @return
     */
    @GetMapping("/api/app/platform/feign/user/sign/info")
    ResponseInfo<String> getUserSign(@RequestParam("userId") String userId);

}
