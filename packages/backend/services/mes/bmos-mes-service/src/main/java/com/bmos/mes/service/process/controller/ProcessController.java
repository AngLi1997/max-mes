package com.bmos.mes.service.process.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.common.enums.process.ProcedureStepNodeFunctionEnum;
import com.bmos.mes.service.process.dto.ProcessTodoPageDTO;
import com.bmos.mes.service.process.dto.ProcessTreeQueryDTO;
import com.bmos.mes.service.process.dto.ProcessVersionAuditDTO;
import com.bmos.mes.service.process.dto.SaveDashboardConfigDTO;
import com.bmos.mes.service.process.dto.modify.ProcessCopyDTO;
import com.bmos.mes.service.process.dto.modify.ProcessModifyDTO;
import com.bmos.mes.service.process.dto.modify.ProcessSaveVersionDTO;
import com.bmos.mes.service.process.dto.modify.ProcessVersionChangeStateDTO;
import com.bmos.mes.service.process.dto.query.*;
import com.bmos.mes.service.process.dto.save.ProcessRecordOrderSaveDTO;
import com.bmos.mes.service.process.dto.save.ProcessRelationSaveDTO;
import com.bmos.mes.service.process.dto.save.ProcessSaveDTO;
import com.bmos.mes.service.process.service.ProcessService;
import com.bmos.mes.service.process.vo.*;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/process")
@Validated
@Api(tags = "工艺相关接口")
public class ProcessController {

    @Autowired
    private ProcessService processService;

    @GetMapping("/page")
    @ApiOperation("工艺分页查询")
    public ResponseInfo<CommonPage<ProcessPageVO>> getPage(@Validated ProcessPageQueryDTO dto) {
        return ResponseInfo.success(processService.getPage(dto));
    }

    @GetMapping("/list")
    @ApiOperation("查询所有工艺的集合")
    public ResponseInfo<List<ProcessListItemVO>> getList(@Validated ProcessListQueryDTO dto) {
        return ResponseInfo.success(processService.getList(dto));
    }

    @GetMapping("/list/tree")
    @ApiOperation("查询关联工艺树")
    public ResponseInfo<List<ProcessListItemTreeVO>> getListTree(ProcessTreeQueryDTO dto){
        return ResponseInfo.success(processService.getListTree(dto));
    }

    @GetMapping("/product/tree")
    @ApiOperation("查询工艺产品树")
    public ResponseInfo<List<ProductProcessTreeNodeVO>> getProcessProductTree() {
        return ResponseInfo.success(processService.getProcessProductTree());
    }

    @GetMapping("/relation/processes")
    @ApiOperation("查询关联的工艺集合")
    public ResponseInfo<List<ProcessListItemVO>> getRelationProcessList(@Validated ProcessRelationQueryDTO dto) {
        return ResponseInfo.success(processService.getRelationProcessList(dto));
    }

    @GetMapping("/relation/processes/materials")
    @ApiOperation("查询关联的工艺物料集合")
    public ResponseInfo<List<ProcessRelationVO>> getRelationProcessMaterial(@NotNull Long processId) {
        return ResponseInfo.success(processService.getProcessRelation(processId));
    }

    @GetMapping("/recursion/relation/processes")
    @ApiOperation("递归查询关联的工艺集合")
    public ResponseInfo<List<ProcessListItemVO>> getRecursionRelationProcessList(@Validated ProcessRelationQueryDTO dto) {
        return ResponseInfo.success(processService.getRecursionRelationProcessList(dto));
    }

    @PostMapping("/save/relations")
    @ApiOperation("保存关联关系")
    public ResponseInfo<Void> saveRelations(@Validated @RequestBody ProcessRelationSaveDTO dto){
        processService.saveProcessRelation(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/detail")
    @ApiOperation("工艺完整详情查询")
    public ResponseInfo<ProcessDetailVO> getDetail(@Validated ProcessDetailQueryDTO dto) {
        return ResponseInfo.success(processService.getDetail(dto));
    }


    @PostMapping("/save")
    @ApiOperation("保存完整工艺")
    @OperationLog(remark = "getDescription")
    public ResponseInfo<ProcessVersionVO> save(@RequestBody @Validated ProcessSaveDTO dto) {
        dto.validatedProcedures();
        return ResponseInfo.success(processService.save(dto));
    }


    @PostMapping("/modify")
    @ApiOperation("编辑工艺")
    @OperationLog(remark = "getDescription")
    public ResponseInfo<ProcessVersionVO> modify(@Validated @RequestBody ProcessModifyDTO dto) {
        dto.validatedProcedures();
        return ResponseInfo.success(processService.modifyProcess(dto));
    }


    @GetMapping("/version/page")
    @ApiOperation("版本分页查询")
    public ResponseInfo<CommonPage<ProcessVersionPageVO>> getVersionPage(@Validated ProcessVersionPageQueryDTO dto) {
        return ResponseInfo.success(processService.getVersionPage(dto));
    }

    @GetMapping("/version/list")
    @ApiOperation("查询工艺版本集合")
    public ResponseInfo<List<ProcessVO>> getVersionList(@Validated ProcessQueryDTO dto) {
        return ResponseInfo.success(processService.getVersionList(dto));
    }


    @PutMapping("/version/changeState")
    @ApiOperation("更改工艺版本状态")
    @OperationLog
    public ResponseInfo<Void> changeState(@RequestBody @Validated ProcessVersionChangeStateDTO dto) {
        processService.changeProcessVersionState(dto);
        return ResponseInfo.success();
    }


    @PostMapping("/version/save")
    @ApiOperation("新增工艺版本")
    @OperationLog
    public ResponseInfo<ProcessVersionVO> saveProcessVersion(@Validated @RequestBody ProcessSaveVersionDTO dto) {
        dto.validatedProcedures();
        return ResponseInfo.success(processService.saveProcessVersion(dto));
    }

    @PostMapping("/version/copy")
    @ApiOperation("复制工艺")
    @OperationLog
    public ResponseInfo<ProcessVersionVO> copyProcessVersion(@Validated @RequestBody ProcessCopyDTO dto) {
        dto.validatedProcedures();
        return ResponseInfo.success(processService.copyProcessVersion(dto));
    }


    @GetMapping("/version/record/order")
    @ApiOperation("查询工艺记录项顺序")
    public ResponseInfo<List<ProcessRecordOrderVO>> getRecordOrder(@Validated ProcessRecordOrderQueryDTO dto) {
        dto.setNodeFunction(ProcedureStepNodeFunctionEnum.SUB_RECORD.getValue());
        return ResponseInfo.success(processService.getRecordOrder(dto));
    }

    @PostMapping("/version/save/order")
    @ApiOperation("保存工艺记录项顺序")
    public ResponseInfo<Void> saveRecordOrder(@Validated @RequestBody ProcessRecordOrderSaveDTO dto) {
        processService.saveRecordOrder(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/version/audit")
    @ApiOperation("提交审批")
    @OperationLog
    public ResponseInfo<Void> auditVersion(@Validated @RequestBody ProcessVersionAuditDTO dto) {
        processService.auditVersion(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/audit/todo/page")
    @ApiOperation("审核待办列表")
    public ResponseInfo<CommonPage<ProcessTodoPageVO>> getAuditTodoPage(@Validated ProcessTodoPageDTO dto) {
        return ResponseInfo.success(processService.getAuditTodoPage(dto));
    }

    @GetMapping("/product/line")
    @ApiOperation("获取产线")
    public ResponseInfo<List<ProductLineVO>> getProductLine() {
        return ResponseInfo.success(processService.getProductLine());
    }

    @GetMapping("/product/line/tree")
    @ApiOperation("获取产线")
    public ResponseInfo<List<ProductLineModuleTreeNodeVO>> getProductLineTree() {
        return ResponseInfo.success(processService.getProductLineTree());
    }

    @GetMapping("/product/line/room/{lineId}")
    @ApiOperation("根据产线id获取房间")
    public ResponseInfo<List<ProductLineRoomVO>> getLineRoom(@PathVariable Long lineId) {
        return ResponseInfo.success(processService.getLineRoom(lineId));
    }

    @GetMapping("/instruction/process/list")
    @ApiOperation("查询所有工艺的集合")
    public ResponseInfo<List<ProcessListItemVO>> getInstructionProcessList(@Validated ProcessListQueryDTO dto) {
        return ResponseInfo.success(processService.getInstructionProcessList(dto));
    }

    @GetMapping("/getDashboardConfig")
    @ApiOperation("查询工艺看板配置数据")
    public ResponseInfo<ProcessDashboardVO> getDashboardConfig(@RequestParam Long processId) {
        return ResponseInfo.success(processService.getDashBoardConfig(processId));
    }

    @PostMapping("/saveDashboardConfig")
    @ApiOperation("保存工艺看板配置数据")
    public ResponseInfo<Void> saveDashboardConfig(@RequestBody @Validated SaveDashboardConfigDTO dto) {
        processService.saveDashboardConfig(dto);
        return ResponseInfo.success();
    }
}
