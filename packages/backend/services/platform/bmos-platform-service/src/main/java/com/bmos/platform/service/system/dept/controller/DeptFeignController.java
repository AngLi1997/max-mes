package com.bmos.platform.service.system.dept.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.platform.facade.system.dept.feign.DeptFeign;
import com.bmos.platform.facade.system.dept.vo.DeptTreeUserAllVO;
import com.bmos.platform.facade.system.dept.vo.DeptTreeVO;
import com.bmos.platform.facade.system.dept.vo.DeptUserTreeVO;
import com.bmos.platform.service.system.dept.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 部门feign接口实现
 */
@RestController
@RequestMapping("/feign/dept")
@Validated
public class DeptFeignController implements DeptFeign {

    @Autowired
    private DeptService deptService;

    @Override
    @GetMapping("/tree-unassigned")
    public ResponseInfo<DeptTreeUserAllVO> getUnassignedUsers(@RequestParam("name") String name) {
        return ResponseInfo.success(deptService.unassigned(name));
    }

    @Override
    @GetMapping("/tree-all")
    public ResponseInfo<List<DeptTreeVO>> getDeptTree() {
        return ResponseInfo.success(deptService.treeAll());
    }

    @Override
    @GetMapping("/user/tree")
    public ResponseInfo<List<DeptUserTreeVO>> getDeptUserTree(@RequestParam("parentDeptCode") String parentDeptCode) {
        return ResponseInfo.success(deptService.getDeptUserTree(parentDeptCode));
    }
}
