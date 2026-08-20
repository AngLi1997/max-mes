package com.bmos.platform.service.permission.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.common.tree.CommonTreeVO;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.platform.service.permission.service.dto.ResourcePermissionSaveDTO;
import com.bmos.platform.service.permission.service.ResourcePermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/resource/permission")
@Validated
@Api(tags = "数据权限相关接口")
public class ResourcePermissionController {

    @Autowired
    private ResourcePermissionService resourcePermissionService;


    @PostMapping("/save")
    @ApiOperation("保存数据权限")
    @OperationLog
    public ResponseInfo<Void> save(@RequestBody @Validated ResourcePermissionSaveDTO dto) {
        resourcePermissionService.save(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/list/dept")
    @ApiOperation("查询该数据关联的部门")
    @ApiParam(name = "resourceId",value = "数据id",required = true)
    public ResponseInfo<List<Long>> getDeptList(@NotNull Long resourceId) {
        return ResponseInfo.success(resourcePermissionService.getDeptListByResourceId(resourceId));
    }

}
