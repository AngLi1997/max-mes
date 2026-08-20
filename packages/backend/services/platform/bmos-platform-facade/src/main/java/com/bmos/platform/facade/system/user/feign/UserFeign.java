package com.bmos.platform.facade.system.user.feign;

import com.bmos.common.response.ResponseInfo;
import com.bmos.platform.facade.system.user.dto.UserQueryDTO;
import com.bmos.platform.facade.system.user.dto.UserResourceQueryDTO;
import com.bmos.platform.facade.system.user.vo.FeignUserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 用户feign接口
 */
@FeignClient(value = "bmos-platform-service", contextId = "platform-system-user")
public interface UserFeign {

    /**
     * 根据菜单id和部门id列表查询用户列表
     * 取交集
     * @param dto
     * @return
     */
    @PostMapping("/api/app/platform/feign/user/listByMenuIdAndDeptIds")
    ResponseInfo<List<FeignUserVO>> listByMenuIdAndDeptIds(@Validated @RequestBody UserQueryDTO dto);

    /**
     * 根据菜单id和部门id列表查询用户列表
     * 取交集
     * @param dto
     * @return
     */
    @PostMapping("/api/app/platform/feign/user/listByMenuIdAndResourceId")
    ResponseInfo<List<FeignUserVO>> listByMenuIdAndResourceId(@Validated @RequestBody UserResourceQueryDTO dto);

    /**
     * 根据用户id查询用户信息
     *
     * @param userIds
     * @return
     */
    @PostMapping("/api/app/platform/feign/user/listByUserIds")
    ResponseInfo<Map<String, FeignUserVO>> getByUserIds(@RequestBody Collection<String> userIds);

    /**
     * 根据部门id列表查询用户列表
     * 取交集
     * @param deptIds 部门id集合
     * @return
     */
    @GetMapping("/api/app/platform/feign/user/listUserListByDeptIds")
    ResponseInfo<List<FeignUserVO>> listUserListByDeptIds(@RequestParam("ids") List<Long> deptIds);

    /**
     * 根据角色id列表查询用户列表
     * 取交集
     * @param roles 角色id集合
     * @return
     */
    @GetMapping("/api/app/platform/feign/user/listUserListByRoleIds")
    ResponseInfo<List<FeignUserVO>> listUserListByRoleIds(@RequestParam("ids") List<Long> roles);


    @GetMapping("/api/app/platform/feign/user/getUserByName")
    ResponseInfo<List<FeignUserVO>> getUserByName(@RequestParam("userName") String userName);

}
