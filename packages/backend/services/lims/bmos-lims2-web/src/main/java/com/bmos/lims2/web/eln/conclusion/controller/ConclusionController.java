package com.bmos.lims2.web.eln.conclusion.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.eln.conclusion.dto.ConclusionComponentSaveDTO;
import com.bmos.lims2.server.eln.conclusion.service.ConclusionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Description: 结论组件接口
 * @Author: yigaohui
 * @Date: 2025/11/19 10:30
 */
@RestController
@RequestMapping("/app/conclusion")
@Validated
@Api(tags = "结论组件接口")
public class ConclusionController {

    @Autowired
    private ConclusionService conclusionService;

    @PostMapping("/save")
    @ApiOperation("保存结论组件（根据布尔值匹配配置生成值）")
    public ResponseInfo<Void> saveConclusion(@RequestBody @Valid ConclusionComponentSaveDTO dto) {
        conclusionService.saveOrUpdateConclusionComponent(dto);
        return ResponseInfo.success();
    }
}

