package com.bmos.mes.service.process.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.service.process.dto.ConfirmUpdateDTO;
import com.bmos.mes.service.process.dto.query.ProcedureConfirmQueryDTO;
import com.bmos.mes.service.process.service.ProcedureConfirmService;
import com.bmos.mes.service.process.vo.ProcedureConfirmVO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Set;

@RestController
@RequestMapping("/procedure/confirm")
@Validated
@Api(tags = "工序结论填报控制器")
public class ProcedureConfirmController {

    @Autowired
    private ProcedureConfirmService service;

    @GetMapping("/page")
    @ApiOperation(value = "查询工序审批列表页")
    public ResponseInfo<CommonPage<ProcedureConfirmVO>> queryProcedurePageByProcessId(@Validated ProcedureConfirmQueryDTO dto){
        return ResponseInfo.success(service.queryProcedurePageByProcessId(dto));
    }

    @PostMapping("/update/procedure")
    @ApiOperation(value = "工序审批结论")
    public ResponseInfo<Boolean> updateProcedureById(@Validated @RequestBody ConfirmUpdateDTO dto){
        return ResponseInfo.success(service.updateProcedureById(dto));
    }

    @GetMapping("/list/procedure/name")
    @ApiOperation(value = "根据工艺id查询工序名称")
    public ResponseInfo<Set<String>> queryProcedureNameByProcessId(@Validated @NotNull Long processId){
        return ResponseInfo.success(service.queryProcedureNameByProcessId(processId));
    }
}
