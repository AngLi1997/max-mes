package com.bmos.platform.facade.system.role.feign;

import com.bmos.common.response.ResponseInfo;
import com.bmos.platform.facade.system.role.vo.FeignRoleVO;
import com.bmos.platform.facade.system.user.vo.FeignUserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 角色feign接口
 */
@FeignClient(value = "bmos-platform-service", contextId = "platform-system-role")
public interface RoleFeign {

    /**
     * 获取具有对应权限的用户列表
     * @param authCode
     * @return
     */
    @GetMapping("/api/app/platform/feign/role/authUserList")
    ResponseInfo<List<FeignUserVO>> authUserList(@RequestParam("authCode") String authCode);

    /**
     * 根据角色id查询已删除的角色信息
     * @param ids 角色id集合
     * @return
     */
    @GetMapping("/api/app/platform/feign/role/get/list/ids")
    ResponseInfo<List<FeignRoleVO>> getListByIds(@RequestParam("ids") List<Long> ids);

}
