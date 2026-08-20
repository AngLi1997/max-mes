package com.bmos.lims2.web.eln.record.controller;


import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.eln.record.dto.RecordListQueryDTO;
import com.bmos.lims2.server.eln.record.dto.SaveFormulaDTO;
import com.bmos.lims2.server.eln.record.service.BatchRecordComponentService;
import com.bmos.lims2.server.eln.record.service.BatchRecordManageService;
import com.bmos.lims2.server.eln.record.vo.RecordListVO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/record/manage")
@Api(tags = "记录管理")
@Validated
public class BatchRecordManageController {

    @Autowired
    private BatchRecordManageService manageService;

    @Resource
    private BatchRecordComponentService componentService;

    @GetMapping("/list/record")
    @ApiOperation(value = "查询批记录列表数据")
    public ResponseInfo<CommonPage<RecordListVO>> getRecordPage(@Validated RecordListQueryDTO dto) {
        return ResponseInfo.success(manageService.getRecordPageWithNoPermission(dto));
    }

    @PostMapping(value = "/save/formula")
    @ApiOperation(value = "添加公式配置")
    public ResponseInfo<Boolean> saveFormula(@Validated @RequestBody SaveFormulaDTO dto) {
        componentService.saveFormula(dto);
        componentService.refreshGraph(dto.getRecordVersionId());
        return ResponseInfo.success(Boolean.TRUE);
    }

    @GetMapping(value = "/delete/formula")
    @ApiOperation(value = "清除公式")
    @ApiParam(name = "componentId", value = "组件表id", required = true)
    public ResponseInfo<Boolean> deleteFormula(@NotNull Long componentId) {
        return ResponseInfo.success(manageService.deleteFormula(componentId));
    }
}
