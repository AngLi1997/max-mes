package com.bmos.mes.service.process.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.service.platform.role.dto.PlatformRoleListQueryDTO;
import com.bmos.mes.service.platform.role.role.PlatformRoleVO;
import com.bmos.mes.service.platform.user.vo.PlatformUserVO;
import com.bmos.mes.service.process.dto.ProcedureModelRoomQueryDTO;
import com.bmos.mes.service.process.dto.ProcedureQueryDTO;
import com.bmos.mes.service.process.dto.ProcedureValidateDTO;
import com.bmos.mes.service.process.dto.query.ProcedureHistoricQueryDTO;
import com.bmos.mes.service.process.dto.query.ProcedurePrincipalQueryDTO;
import com.bmos.mes.service.process.dto.save.ProcedureSaveDTO;
import com.bmos.mes.service.process.dto.save.SaveProcessSortVO;
import com.bmos.mes.service.process.service.ProcedureModelService;
import com.bmos.mes.service.process.service.ProcedureService;
import com.bmos.mes.service.process.vo.HistoricVO;
import com.bmos.mes.service.process.vo.ProcedureModelDetailVO;
import com.bmos.mes.service.process.vo.ProcedureModelRoomVO;
import com.bmos.mes.service.process.vo.ProcedureVO;
import com.bmos.mes.service.process.vo.Task.EquipmentModuleTreeNodeVO;
import com.bmos.mes.service.process.vo.Task.NodeVO;
import io.swagger.annotations.*;
import com.bmos.mes.service.process.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/procedure")
@Api(tags = "工序相关接口")
public class ProcedureController {

    @Autowired
    private ProcedureModelService procedureModelService;

    @Resource
    private ProcedureService procedureService;

    @PostMapping("/detail/save")
    @ApiOperation("保存工序流程")
    public ResponseInfo<String> saveProcedureDetail(@Validated @RequestBody ProcedureSaveDTO dto) {
        dto.validatedProcedureSteps();
        return ResponseInfo.success(procedureModelService.saveDetail(dto));
    }

    @PostMapping("/detail/modify")
    @ApiOperation("更新工序流程")
    public ResponseInfo<Void> modifyProcedureDetail(@Validated @RequestBody ProcedureSaveDTO dto) {
        dto.validatedProcedureSteps();
        procedureModelService.modifyProcedureDetail(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/historic/list")
    @ApiOperation("查询历史工序集合")
    public ResponseInfo<List<HistoricVO>> getHistoricProcedureList(@Validated ProcedureHistoricQueryDTO dto) {
        return ResponseInfo.success(procedureModelService.getHistoricProcedureList(dto));
    }

    @GetMapping("/validate/name")
    @ApiOperation("校验工序名称是否重复")
    public ResponseInfo<Boolean> validateProcedureName(@Validated ProcedureValidateDTO dto) {
        return ResponseInfo.success(procedureService.validateProcedureName(dto));
    }

    @GetMapping("/list")
    @ApiOperation("查询工序集合")
    public ResponseInfo<List<ProcedureVO>> getList(@Validated ProcedureQueryDTO dto) {
        dto.validate();
        return ResponseInfo.success(procedureModelService.getList(dto));
    }


    @GetMapping("/detail/{id}")
    @ApiOperation("查询单个工序详情")
    public ResponseInfo<ProcedureModelDetailVO> getDetail(@PathVariable("id") Long id) {
        return ResponseInfo.success(procedureModelService.getDetail(id));
    }

    @GetMapping("/principal/users")
    @ApiOperation("查询工序负责人集合")
    public ResponseInfo<List<PlatformUserVO>> getPrincipalList(@Validated ProcedurePrincipalQueryDTO dto) {
        return ResponseInfo.success(procedureModelService.getPrincipalList(dto));
    }


    /**
     * 该接口已修改为查询工艺绑定的产线下的工位
     *
     * @param dto
     * @return
     */
    @GetMapping("/rooms")
    @ApiOperation("查询工序绑定房间下的工位集合")
    public ResponseInfo<List<ProcedureModelRoomOrStationVO>> getProcedureModelRoomList(@Validated ProcedureModelRoomQueryDTO dto) {
        return ResponseInfo.success(procedureModelService.getProcedureModelRoomList(dto));
    }

    @GetMapping("/node/list")
    @ApiOperation("查询节点集合")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "工序模型id", required = true),
            @ApiImplicitParam(name = "type", value = "true:步骤节点/false:任务节点"),
    })
    public ResponseInfo<List<NodeVO>> getNodeList(@Valid @NotNull Long id, Boolean type, Long stepModelId) {
        return ResponseInfo.success(procedureModelService.getNodeList(id, type, stepModelId));
    }

    @GetMapping("/complete/node/list")
    @ApiOperation("查询节点集合")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "procedureModelId", value = "工序模型id", required = true)
    })
    public ResponseInfo<List<NodeVO>> getProcedureNodeList(@Valid @NotNull Long procedureModelId) {
        return ResponseInfo.success(procedureModelService.getProcedureNodeList(procedureModelId));
    }

    @GetMapping("/rooms/list")
    @ApiOperation("查询工序绑定的房间信息（含产线）")
    public ResponseInfo<List<ProcedureModelRoomVO>> getProcedureModelRoomInfoTree(@Validated ProcedureModelRoomQueryDTO dto) {
        return ResponseInfo.success(procedureModelService.getProcedureModelRoomInfoTree(dto));
    }

    @GetMapping("/rooms/list/all")
    @ApiOperation("查询工序绑定的房间信息（含产线）")
    public ResponseInfo<List<ProcedureModelRoomVO>> getProcedureModelRoomInfo(@Validated ProcedureModelRoomQueryDTO dto) {
        return ResponseInfo.success(procedureModelService.getProcedureModelRoomInfo(dto));
    }

    @GetMapping("/step/rooms/list/all")
    @ApiOperation("查询工序绑定的房间信息（含产线）")
    public ResponseInfo<List<ProcedureModelRoomVO>> getStepModelRoomInfo(@Validated ProcedureModelRoomQueryDTO dto) {
        return ResponseInfo.success(procedureModelService.getStepModelRoomInfo(dto));
    }

    @GetMapping("/equipment/list")
    @ApiOperation("查询设备信息")
    public ResponseInfo<List<EquipmentModuleTreeNodeVO>> getEquipmentTree() {
        return ResponseInfo.success(procedureModelService.getEquipmentTree());
    }

    @GetMapping("/step/equipment/list")
    @ApiOperation("查询设备信息")
    @ApiImplicitParam(name = "stepModelId", value = "工步id")
    public ResponseInfo<List<EquipmentModuleTreeNodeVO>> getStepEquipmentTree(Long stepModelId) {
        return ResponseInfo.success(procedureModelService.getStepEquipmentTree(stepModelId));
    }

    @GetMapping("/list/process/sort")
    @ApiOperation("查询工艺排序")
    @ApiImplicitParam(value = "processVersionId", name = "工序版本id", required = true)
    public ResponseInfo<List<ProcessSortVO>> listProcessSort(@Validated @NotNull Long processVersionId) {
        return ResponseInfo.success(procedureModelService.listProcessSort(processVersionId));
    }

    @PostMapping("/save/process/sort")
    @ApiOperation("添加工序排序信息")
    public ResponseInfo<Void> saveProcessSort(@Validated @NotEmpty @RequestBody List<SaveProcessSortVO> sortList) {
        procedureModelService.saveProcessSort(sortList);
        return ResponseInfo.success();
    }

    @GetMapping("/model/role/list")
    @ApiOperation("查询负责人集合")
    public ResponseInfo<List<PlatformRoleVO>> getProcedureRoleRoles(PlatformRoleListQueryDTO dto) {
        return ResponseInfo.success(procedureModelService.getProcedureRoleRoles(dto));
    }

    @GetMapping("/get/procedure/model")
    @ApiOperation("查询工序节点集合")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "versionId", name = "工艺版本id", required = true),
            @ApiImplicitParam(value = "stepModelId", name = "工步模型id")
    })
    public ResponseInfo<List<NodeVO>> getProcedureModelList(@Validated @NotNull Long versionId, Long stepModelId) {
        return ResponseInfo.success(procedureModelService.getProcedureModelList(versionId, stepModelId));
    }
}
