package com.bmos.mes.service.process.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.service.platform.dict.vo.DictVO;
import com.bmos.mes.service.process.dto.ConfirmUpdateDTO;
import com.bmos.mes.service.process.dto.query.AuditOpinionQueryDTO;
import com.bmos.mes.service.process.dto.query.ProcessConfirmQueryDTO;
import com.bmos.mes.service.process.service.ProcessConfirmService;
import com.bmos.mes.service.process.vo.AuditOpinionVO;
import com.bmos.mes.service.process.vo.ProcessConfirmVO;
import com.bmos.mes.service.process.vo.StatisticsVO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/confirm")
@Validated
@Api(tags = "工艺结论填报控制器")
public class ProcessConfirmController {

    @Autowired
    private ProcessConfirmService processConfirmService;

    @GetMapping("/page")
    @ApiOperation(value = "查询工艺审批填报列表页")
    public ResponseInfo<CommonPage<ProcessConfirmVO>> getProcessConfirmPageList(@Validated ProcessConfirmQueryDTO dto) {
        return ResponseInfo.success(processConfirmService.getProcessConfirmPageList(dto));
    }

    @GetMapping("/list")
    @ApiOperation(value = "查询工艺名称下拉框")
    public ResponseInfo<Set<DictVO>> getProcessNameList() {
        return ResponseInfo.success(processConfirmService.getProcessNameList());
    }

    @PostMapping("/update/process")
    @ApiOperation(value = "填写审批结论接口")
    public ResponseInfo<Boolean> updateProcessOpinion(@Validated @RequestBody ConfirmUpdateDTO dto) {
        return ResponseInfo.success(processConfirmService.updateProcessOpinion(dto));
    }

    @GetMapping("/list/process/opinion")
    @ApiOperation(value = "查询工艺审批列表页")
    public ResponseInfo<CommonPage<AuditOpinionVO>> listProcessOpinionPage(@Validated AuditOpinionQueryDTO dto) {
        return ResponseInfo.success(processConfirmService.listProcessOpinionPage(dto));
    }

    @GetMapping("/process/statistics")
    @ApiOperation(value = "工艺报表统计")
    public ResponseInfo<List<StatisticsVO>> processStatistics(@Validated AuditOpinionQueryDTO dto) {
        return ResponseInfo.success(processConfirmService.processStatistics(dto));
    }
}
