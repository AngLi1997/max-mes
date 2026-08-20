package com.bmos.lims2.web.inspect.scheme;

import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.inspect.scheme.dto.request.ComponentConfigDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeParameterComponentConfigSaveDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.ProcedureStepRecordItemQueryDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.SchemeParameterComponentConfigListQueryDTO;
import com.bmos.lims2.server.inspect.scheme.dto.response.ProcedureStepRecordItemDTO;
import com.bmos.lims2.server.inspect.scheme.service.InspectionSchemeParameterComponentConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 方案分析项方法组件配置接口
 *
 *
 * @className: InspectionSchemeParameterConfigController
 * @author: yigaohui
 * @date: 2025/11/10 10:29
 * @Version: 1.0
 * @description:
 */

@RestController
@RequestMapping("/inspection/scheme/parameter/method")
@Api(tags = "方案分析项方法组件配置接口")
public class InspectionSchemeParameterConfigController {


    @Autowired
    private InspectionSchemeParameterComponentConfigService inspectionSchemeParameterComponentConfigService;

    @GetMapping("/config/list")
    @ApiOperation("查询批记录组件配置集合")
    public ResponseInfo<List<ComponentConfigDTO>> getConfigList(@Validated SchemeParameterComponentConfigListQueryDTO dto) {
        return ResponseInfo.success(inspectionSchemeParameterComponentConfigService.getConfigList(dto));
    }

    @PostMapping("/config/save")
    @ApiOperation("保存工序步骤记录项配置")
    public ResponseInfo<Void> saveConfig(@Validated @RequestBody InspectionSchemeParameterComponentConfigSaveDTO dto) {
        inspectionSchemeParameterComponentConfigService.saveConfig(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/record/item")
    @ApiOperation("查询工序步骤关联的记录项")
    public ResponseInfo<ProcedureStepRecordItemDTO> getRecordItem(@Validated ProcedureStepRecordItemQueryDTO dto) {
        return ResponseInfo.success(inspectionSchemeParameterComponentConfigService.getRecordItem(dto));
    }
}
