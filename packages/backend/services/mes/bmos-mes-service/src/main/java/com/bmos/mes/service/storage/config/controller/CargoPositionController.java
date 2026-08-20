package com.bmos.mes.service.storage.config.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.permission.service.ResourcePermissionService;
import com.bmos.mes.service.platform.user.vo.PlatformUserVO;
import com.bmos.mes.service.storage.config.dto.CargoPositionCreateDTO;
import com.bmos.mes.service.storage.config.dto.CargoPositionPageQuery;
import com.bmos.mes.service.storage.config.dto.MaterialPositionEditDTO;
import com.bmos.mes.service.storage.config.service.ICargoPositionService;
import com.bmos.mes.service.storage.config.vo.CargoPositionVO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 货位配置
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/5 10:47
 */
@RestController
@RequestMapping("/material/position")
@Validated
@Api(tags = "货位配置")
public class CargoPositionController {

    @Resource
    private ICargoPositionService cargoPositionService;

    @Resource
    private ResourcePermissionService resourcePermissionService;

    @GetMapping("/page")
    @ApiOperation("分页查询暂存货位")
    public ResponseInfo<CommonPage<CargoPositionVO>> queryPage(@Validated CargoPositionPageQuery pageQuery) {
        return ResponseInfo.success(cargoPositionService.queryPage(pageQuery));
    }

    @GetMapping("/info")
    @ApiOperation("根据id查询暂存货位详情")
    @ApiImplicitParam(value = "暂存货位id", name = "id", required = true, example = "1")
    public ResponseInfo<CargoPositionVO> queryInfoById(@RequestParam Long id) {
        return ResponseInfo.success(cargoPositionService.queryInfoById(id));
    }

    @GetMapping("/infoByPositionCode")
    @ApiOperation("根据暂存货位编码查询暂存货位详情")
    @ApiImplicitParam(value = "暂存货位编码", name = "code", required = true, example = "WH05")
    public ResponseInfo<CargoPositionVO> queryInfoById(@RequestParam String code) {
        return ResponseInfo.success(cargoPositionService.queryInfoByCode(code));
    }

    @GetMapping("/listDataPermission")
    @ApiOperation("根据id查询部门权限")
    @ApiImplicitParam(value = "暂存货位id", name = "id", required = true, example = "1")
    public ResponseInfo<List<Long>> listDataPermission(@RequestParam Long id) {
        return ResponseInfo.success(resourcePermissionService.getDeptListByResourceId(id));
    }


    @PostMapping("/create")
    @ApiOperation("新建暂存货位")
    @OperationLog
    public ResponseInfo<Void> createCargoPosition(@Validated @RequestBody CargoPositionCreateDTO dto) {
        cargoPositionService.createCargoPosition(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/edit")
    @ApiOperation("编辑暂存货位")
    @OperationLog
    public ResponseInfo<Void> editCargoPosition(@Validated @RequestBody MaterialPositionEditDTO dto) {
        cargoPositionService.editCargoPosition(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/enable")
    @ApiOperation("启用暂存货位")
    @ApiImplicitParam(value = "暂存货位id", name = "id", required = true, example = "1")
    @OperationLog
    public ResponseInfo<Void> enableCargoPosition(@RequestParam Long id) {
        cargoPositionService.enableCargoPosition(id);
        return ResponseInfo.success();
    }

    @PutMapping("/disable")
    @ApiOperation("停用暂存货位")
    @ApiImplicitParam(value = "暂存货位id", name = "id", required = true, example = "1")
    @OperationLog
    public ResponseInfo<Void> disableCargoPosition(@RequestParam Long id) {
        cargoPositionService.disableCargoPosition(id);
        return ResponseInfo.success();
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除暂存货位")
    @ApiImplicitParam(value = "暂存货位id", name = "id", required = true, example = "1")
    @OperationLog
    public ResponseInfo<Void> deleteCargoPosition(@RequestParam Long id) {
        cargoPositionService.deleteCargoPosition(id);
        return ResponseInfo.success();
    }

    @GetMapping("/listBoundUser")
    @ApiOperation("根据暂存间货位id查询绑定的用户")
    @ApiImplicitParam(name = "positionId", value = "暂存间货位id", required = true)
    public ResponseInfo<List<PlatformUserVO>> queryPositionBoundUserList(@NotNull Long positionId){
        return ResponseInfo.success(cargoPositionService.queryPositionBoundUserList(positionId));
    }

    @GetMapping("/queryPositionBoundUserListByPermissionCode")
    @ApiOperation("根据暂存间货位id和权限码查询绑定的用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "positionId", value = "暂存间货位id", required = true),
            @ApiImplicitParam(name = "permissionCode", value = "权限码", required = false)
    })
    public ResponseInfo<List<PlatformUserVO>> queryPositionBoundUserListByPermissionCode(@RequestParam Long positionId, @RequestParam(required = false) Long permissionCode){
        return ResponseInfo.success(cargoPositionService.queryPositionBoundUserListByPermissionCode(positionId, permissionCode));
    }
}
