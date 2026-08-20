package com.bmos.mes.service.process.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.service.platform.user.vo.PlatformUserVO;
import com.bmos.mes.service.process.dto.*;
import com.bmos.mes.service.process.dto.query.ProcedureStepHistoricQueryDTO;
import com.bmos.mes.service.process.dto.save.ProcedureStepConfigSaveDTO;
import com.bmos.mes.service.process.service.ProcedureStepModelService;
import com.bmos.mes.service.process.service.ProcedureStepService;
import com.bmos.mes.service.process.vo.*;
import com.bmos.mes.service.process.vo.Task.ProcedureStepAndTaskVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/procedure/step")
@Api(tags = "工序步骤相关接口")
@Validated
public class ProcedureStepController {

    @Autowired
    private ProcedureStepModelService procedureStepModelService;

    @Resource
    private ProcedureStepService procedureStepService;

    @GetMapping("/list")
    @ApiOperation("查询工序步骤集合")
    @ApiParam(name = "procedureId", value = "工序id", required = true)
    public ResponseInfo<ProcedureStepAndTaskVO> getStepList(@Validated Long procedureId, String recordVersionIds) {
        return ResponseInfo.success(procedureStepModelService.getByProcedureModelId(recordVersionIds,procedureId));
    }


    @GetMapping("/config/list")
    @ApiOperation("查询批记录组件配置集合")
    public ResponseInfo<List<ComponentConfigVO>> getConfigList(@Validated ProcedureStepConfigListQueryDTO dto) {
        return ResponseInfo.success(procedureStepModelService.getConfigList(dto));
    }

    @PostMapping("/config/save")
    @ApiOperation("保存工序步骤记录项配置")
    public ResponseInfo<Void> saveConfig(@Validated @RequestBody ProcedureStepConfigSaveDTO dto) {
        procedureStepModelService.saveConfig(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/historic/list")
    @ApiOperation("查询历史工序步骤")
    public ResponseInfo<List<HistoricVO>> getHistoricStepList(@Validated ProcedureStepHistoricQueryDTO dto) {
        return ResponseInfo.success(procedureStepModelService.getHistoricStepList(dto));
    }

    @GetMapping("/validate/name")
    @ApiOperation("校验工序步骤名称是否重复")
    public ResponseInfo<Boolean> validateProcedureStepName(@Validated ProcedureStepValidateDTO dto) {
        return ResponseInfo.success(procedureStepService.validateProcedureStepName(dto));
    }

    @GetMapping("/record/item")
    @ApiOperation("查询工序步骤关联的记录项")
    public ResponseInfo<ProcedureStepRecordItemVO> getRecordItem(@Validated ProcedureStepRecordItemQueryDTO dto) {
        return ResponseInfo.success(procedureStepModelService.getRecordItem(dto));
    }


    @GetMapping("/listByProcess")
    @ApiOperation("查询工艺版本下的工序步骤")
    public ResponseInfo<List<ProcessStepVO>> getListByProcess(@Validated ProcessStepQueryDTO dto) {
        return ResponseInfo.success(procedureStepModelService.getListByProcess(dto));
    }

    @GetMapping("/group/users")
    @ApiOperation("查询工序步骤班组人员集合")
    public ResponseInfo<List<PlatformUserVO>> getGroupUserList(@Validated ProcedureStepGroupUserDTO dto){
        return ResponseInfo.success(procedureStepModelService.getGroupUserList(dto));
    }

    @GetMapping("/listByProcedureModelId")
    @ApiOperation("根据工序模型id查询步骤列表")
    public ResponseInfo<List<ProcedureStepModelListVO>> getListByProcedureModelId(@NotNull Long procedureModelId) {
        return ResponseInfo.success(procedureStepModelService.getListByProcedureModelId(procedureModelId));
    }
}
