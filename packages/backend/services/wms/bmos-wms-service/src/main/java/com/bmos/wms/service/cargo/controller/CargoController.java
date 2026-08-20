package com.bmos.wms.service.cargo.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.wms.service.cargo.dto.*;
import com.bmos.wms.service.cargo.service.ICargoService;
import com.bmos.wms.service.cargo.vo.CargoPageVO;
import com.bmos.wms.service.cargo.vo.CargoVO;
import com.bmos.wms.service.platform.material.dto.SyncTreeQueryDTO;
import com.bmos.wms.service.platform.material.vo.SyncTreeNodeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 货品相关接口
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/25 17:54
 */
@RestController
@RequestMapping("/cargo")
@Api(tags = "货品相关接口")
public class CargoController {

    @Resource
    private ICargoService cargoService;

    @GetMapping("/queryPage")
    @ApiOperation(value = "查询货品分页")
    public ResponseInfo<CommonPage<CargoPageVO>> queryPage(@Validated CargoPageQuery pageQuery) {
        return ResponseInfo.success(cargoService.queryPage(pageQuery));
    }

    @GetMapping("/queryInfo")
    @ApiOperation(value = "根据id查询货品信息")
    @ApiImplicitParam(name = "id", value = "id", required = true, example = "1")
    public ResponseInfo<CargoVO> queryInfo(@RequestParam @Validated Long id) {
        return ResponseInfo.success(cargoService.queryInfoById(id));
    }

    @PostMapping("/sync")
    @ApiOperation("同步货品")
    @OperationLog
    public ResponseInfo<Void> syncMaterialAndCategory(@RequestBody @Validated SyncCargoDTO dto) {
        cargoService.syncMaterialAndCategory(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/syncTree")
    @ApiOperation("获取同步分类物料树")
    @OperationLog
    public ResponseInfo<List<SyncTreeNodeVO>> getSyncTree(@Validated SyncTreeQueryDTO dto) {
        return ResponseInfo.success(cargoService.getSyncTree(dto));
    }

    @GetMapping("/syncTreeAll")
    @ApiOperation("获取同步分类全量树")
    @OperationLog
    public ResponseInfo<List<SyncTreeNodeVO>> getSyncTreeAll() {
        return ResponseInfo.success(cargoService.getSyncTreeAll());
    }

    /**
     * 平台下发物料及分类的接口
     *
     * @return
     */
    @PostMapping("/issueMaterialAndCategory")
    public ResponseInfo<Void> issueMaterialAndCategory(@RequestBody RemoteIssueFeignDTO dto) {
        cargoService.issueMaterialAndCategory(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/create")
    @ApiOperation(value = "创建货品")
    @OperationLog
    public ResponseInfo<CargoPageVO> create(@RequestBody @Validated CargoCreateDTO dto) {
        cargoService.create(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/edit")
    @ApiOperation(value = "编辑货品")
    @OperationLog
    public ResponseInfo<CargoPageVO> edit(@RequestBody @Validated CargoEditDTO dto) {
        cargoService.edit(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/enable")
    @ApiOperation("启用货品")
    @ApiImplicitParam(value = "货品id", name = "id", required = true, example = "1")
    @OperationLog
    public ResponseInfo<Void> enable(@RequestParam Long id) {
        cargoService.enable(id);
        return ResponseInfo.success();
    }

    @PutMapping("/disable")
    @ApiOperation("停用货品")
    @ApiImplicitParam(value = "货品id", name = "id", required = true, example = "1")
    @OperationLog
    public ResponseInfo<Void> disable(@RequestParam Long id) {
        cargoService.disable(id);
        return ResponseInfo.success();
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除货品")
    @ApiImplicitParam(value = "货品id", name = "id", required = true, example = "1")
    @OperationLog
    public ResponseInfo<Void> delete(@RequestParam Long id) {
        cargoService.delete(id);
        return ResponseInfo.success();
    }

    @GetMapping("/queryNotMemberListByCategoryId")
    @ApiOperation(value = "根据货品分类id查询货品列表")
    @ApiImplicitParam(value = "货品分类id", name = "categoryId", required = true, example = "1")
    public ResponseInfo<List<CargoVO>> queryNotMemberListByCategoryId(@RequestParam Long categoryId) {
        return ResponseInfo.success(cargoService.queryNotMemberListByCategoryId(categoryId));
    }
}
