package com.bmos.adaptor.platform.feign;

import com.bmos.adaptor.platform.dto.ValidatePwd;
import com.bmos.adaptor.platform.vo.UserInfoVO;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.tree.CommonTreeVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "bmos-platform-service", contextId = "user")
public interface PlatformOpenFeign {

    @GetMapping("/api/app/platform/role/id")
    ResponseInfo<List<Long>> getRolesIds();

    @GetMapping("/api/app/platform/dept/id")
    ResponseInfo<List<Long>> getDeptIds();

    @GetMapping("/api/app/platform/dept/tree")
    ResponseInfo<List<CommonTreeVO>> getDeptTree();

    @GetMapping("/api/app/platform/dept/partition/tree")
    ResponseInfo<List<CommonTreeVO>> getDeptPartitionTree();

    @GetMapping("/api/app/platform/dept/mine/id")
    ResponseInfo<List<Long>> getMineDeptIds();

    @GetMapping("/api/app/platform/user/{userId}")
    ResponseInfo<UserInfoVO> getUser(@PathVariable("userId")String userId);

    @PostMapping("/api/app/platform/user/validatePwd")
    ResponseInfo<UserInfoVO> validatePassword(@RequestBody ValidatePwd dto);
}
