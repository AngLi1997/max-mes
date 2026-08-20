package com.bmos.mes.service.components.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.service.components.convert.BusinessComponentInstanceConvert;
import com.bmos.mes.service.components.dto.BusinessComponentQuery;
import com.bmos.mes.service.components.model.BusinessComponentInstance;
import com.bmos.mes.service.components.service.IBusinessComponentService;
import com.bmos.mes.service.components.vo.BusinessComponentInstanceVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 业务组件实例接口
 * @author liang
 * @version 1.0.0
 * @date 2024/7/18 15:41
 */
@RestController
@RequestMapping("/components")
@Api(tags = "业务组件实例接口")
public class BusinessComponentController {

    @Resource
    private IBusinessComponentService businessComponentService;

    @GetMapping("/getInstance")
    @ApiOperation(value = "根据组件实例id获取组件实例详情")
    @ApiImplicitParam(name = "componentInstanceId", value = "业务组件实例id", required = true)
    private ResponseInfo<BusinessComponentInstanceVO> getInstance(@RequestParam Long componentInstanceId) {
        BusinessComponentInstance businessComponentInstance = businessComponentService.selectById(componentInstanceId);
        return ResponseInfo.success(BusinessComponentInstanceConvert.INSTANCE.convertToVO(businessComponentInstance));
    }

    @PostMapping("/getInstanceByProps")
    @ApiOperation(value = "根据组件实例id获取组件实例详情")
    private ResponseInfo<BusinessComponentInstanceVO> getInstance(@RequestBody @Validated BusinessComponentQuery query) {
        BusinessComponentInstance businessComponentInstance = businessComponentService.getOrCreateComponentInstance(query.getProductPlanId(), query.getProcedureStepModelId(), query.getComponentId(), query.getCopyVersion(), query.getReuse());
        return ResponseInfo.success(BusinessComponentInstanceConvert.INSTANCE.convertToVO(businessComponentInstance));
    }
}
