package com.bmos.mes.service.platform.controller;

import cn.hutool.core.util.StrUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.common.enums.plan.CodeRuleTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.plan.rule.model.CodeRule;
import com.bmos.mes.service.plan.rule.service.CodeRuleService;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.platform.dict.DictClient;
import com.bmos.mes.service.platform.dict.vo.DictVO;
import com.bmos.mes.service.platform.plan.PlatformCodeRuleClient;
import com.bmos.mes.service.platform.plan.dto.BatchNextUseCodeDTO;
import com.bmos.mes.service.platform.plan.dto.NextUseCodeDTO;
import com.bmos.mes.service.platform.plan.vo.BatchNextCodeVO;
import com.bmos.mes.service.platform.plan.vo.CodeRuleVO;
import com.bmos.mes.service.platform.plan.vo.NextCodeVO;
import com.bmos.mes.service.platform.role.dto.PlatformRoleListQueryDTO;
import com.bmos.mes.service.platform.role.feign.PlatformRoleOpenFeign;
import com.bmos.mes.service.platform.role.role.PlatformRoleVO;
import com.bmos.mes.service.platform.role.service.PlatformRoleService;
import com.bmos.mes.service.platform.user.feign.PlatformUserOpenFeign;
import com.bmos.mes.service.platform.user.vo.PlatformUserVO;
import com.bmos.platform.facade.system.dept.feign.DeptFeign;
import com.bmos.platform.facade.system.dept.vo.DeptTreeUserAllVO;
import com.bmos.platform.facade.system.dept.vo.DeptTreeVO;
import com.bmos.platform.facade.system.dept.vo.DeptUserTreeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/platform/query")
@Validated
@Api(tags = "平台相关接口")
@Slf4j
public class PlatformController {

    @Autowired
    private PlatformRoleService platformRoleService;

    @Autowired
    private PlatformRoleOpenFeign platformRoleOpenFeign;

    @Autowired
    private DeptFeign deptFeign;

    @Autowired
    private PlatformCodeRuleClient platformCodeRuleClient;

    @Autowired
    private CodeRuleService codeRuleService;

    @Autowired
    private PlatformUserOpenFeign platformUserOpenFeign;

    @Autowired
    private DictClient dictClient;

    @PostMapping("/codeRule/getNextUseNo")
    @ApiOperation("获取下一个编号 未确认使用的编号会重复返回")
    public ResponseInfo<NextCodeVO> getNextUseNo(@RequestBody @Validated NextUseCodeDTO dto) {
        return ResponseInfo.success(codeRuleService.getNextUseNo(dto));
    }

    @PostMapping("/codeRule/getBatchNextUseNo")
    @ApiOperation("批量获取下一个编号 未确认使用的编号会重复返回")
    public ResponseInfo<BatchNextCodeVO> getBatchNextUseNo(@RequestBody @Validated BatchNextUseCodeDTO dto) {
        return ResponseInfo.success(codeRuleService.getBatchNextUserNo(dto));
    }

    @GetMapping("/role/list")
    @ApiOperation("查询平台角色集合")
    public ResponseInfo<List<PlatformRoleVO>> getPlatformRoles(PlatformRoleListQueryDTO dto) {
        return ResponseInfo.success(platformRoleService.getRoles(dto));
    }

    @GetMapping("/role/detail/{id}")
    @ApiOperation("查询平台角色")
    public ResponseInfo<PlatformRoleVO> getPlatformRole(@PathVariable("id") Long id) {
        return FeignUtils.handleRequest(data -> platformRoleOpenFeign.getRole(data), id);
    }

    @GetMapping("/codeRule/list")
    @ApiOperation("编码规则列表")
    public ResponseInfo<List<CodeRuleVO>> codeRuleList() {
        return FeignUtils.handleRequest(data -> platformCodeRuleClient.codeRuleList(data), null);
    }

    @GetMapping("/dept/tree")
    @ApiOperation("查询部门树（全量）")
    public ResponseInfo<List<DeptTreeVO>> getDeptTree() {
        return FeignUtils.handleRequest((data) -> deptFeign.getDeptTree(), null);
    }

    @GetMapping("/dept/user/unassigned")
    @ApiOperation("查询未分配部门的用户")
    public ResponseInfo<DeptTreeUserAllVO> getUnassignedUsers() {
        return FeignUtils.handleRequest((data) -> deptFeign.getUnassignedUsers(data), StrUtil.EMPTY);
    }

    @GetMapping("/dept/user/tree")
    @ApiOperation("查询部门用户树")
    public ResponseInfo<List<DeptUserTreeVO>> getDeptUserTree() {
        return FeignUtils.handleRequest((data) -> deptFeign.getDeptUserTree(data), StrUtil.EMPTY);
    }

    @GetMapping("/user/listByRole")
    @ApiOperation("根据角色查用户")
    @ApiImplicitParam(name = "roleId",value = "角色id",required = true)
    public ResponseInfo<List<PlatformUserVO>> getUserByRole(@NotNull Long roleId) {
        return FeignUtils.handleRequest((data) -> platformUserOpenFeign.getListByRole(data), roleId);
    }

    @GetMapping("/list/dict/down")
    @ApiOperation(value = "查询字典下拉框")
    @ApiParam(name = "dictId", value = "字典表id,查询字典数据时使用")
    public ResponseInfo<List<DictVO>> listDictDown(Long dictId) {
        return FeignUtils.handleRequest((data) -> dictClient.listDictDown(data), dictId);
    }
}
