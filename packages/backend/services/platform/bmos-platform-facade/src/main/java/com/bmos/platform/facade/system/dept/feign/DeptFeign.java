package com.bmos.platform.facade.system.dept.feign;

import com.bmos.common.response.ResponseInfo;
import com.bmos.platform.facade.system.dept.vo.DeptTreeUserAllVO;
import com.bmos.platform.facade.system.dept.vo.DeptTreeVO;
import com.bmos.platform.facade.system.dept.vo.DeptUserTreeVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 部门feign接口
 */
@FeignClient(value = "bmos-platform-service", contextId = "platform-system-dept")
public interface DeptFeign {

    @GetMapping("/api/app/platform/feign/dept/tree-unassigned")
    ResponseInfo<DeptTreeUserAllVO> getUnassignedUsers(@RequestParam("name") String name);

    @GetMapping("/api/app/platform/feign/dept/tree-all")
    ResponseInfo<List<DeptTreeVO>> getDeptTree();

    @GetMapping("/api/app/platform/feign/dept/user/tree")
    ResponseInfo<List<DeptUserTreeVO>> getDeptUserTree(@RequestParam("parentDeptCode") String parentDeptCode);

}
