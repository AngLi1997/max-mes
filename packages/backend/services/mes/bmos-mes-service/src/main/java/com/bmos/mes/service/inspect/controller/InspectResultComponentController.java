package com.bmos.mes.service.inspect.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.service.inspect.controller.vo.InspectPageVO;
import com.bmos.mes.service.inspect.service.InspectComponentService;
import com.bmos.mes.service.inspect.service.dto.InspectComponentConfirmFillDTO;
import com.bmos.mes.service.inspect.service.dto.InspectResultPageQueryDTO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inspect/component")
@Api(tags = "请验结果组件")
@Validated
public class InspectResultComponentController {

    @Resource
    private InspectComponentService inspectComponentService;

    @GetMapping("/notRejectPage")
    @ApiOperation("请验结果组件:获取非退回请验单分页")
    public ResponseInfo<CommonPage<InspectPageVO>> queryNotRejectInspectResultPage(InspectResultPageQueryDTO dto) {
        return ResponseInfo.success(inspectComponentService.queryNotRejectInspectResultPage(dto));
    }

    @PostMapping("/confirm")
    @ApiOperation("请验结果组件:确定回填表单数据")
    public ResponseInfo<Void> confirmFillFormData(@RequestBody InspectComponentConfirmFillDTO dto) {
        inspectComponentService.confirmFillFormData(dto);
        return ResponseInfo.success();
    }
    


}
