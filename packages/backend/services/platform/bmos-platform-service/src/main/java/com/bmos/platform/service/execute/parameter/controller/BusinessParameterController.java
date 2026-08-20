package com.bmos.platform.service.execute.parameter.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.service.equipment.datasource.config.HubProperties;
import com.bmos.platform.service.equipment.datasource.impl.DataSourceHubStrategy;
import com.bmos.platform.service.execute.parameter.dto.BusinessParameterPageDTO;
import com.bmos.platform.service.execute.parameter.dto.UpdateBusinessParameterDTO;
import com.bmos.platform.service.execute.parameter.service.BusinessParameterService;
import com.bmos.platform.service.execute.parameter.vo.BusinessParameterDetailVO;
import com.bmos.platform.service.execute.parameter.vo.BusinessParameterPageVO;
import com.bmos.platform.service.execute.parameter.vo.BusinessParameterVO;
import com.bmos.platform.service.material.dto.MaterialPageQueryDTO;
import com.bmos.platform.service.material.vo.MaterialPageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/business/parameter")
@Validated
@Api(tags = "参数配置")
public class BusinessParameterController {
    @Autowired
    private BusinessParameterService businessParameterService;

    @GetMapping("/page")
    @ApiOperation("参数信息分页")
    public ResponseInfo<CommonPage<BusinessParameterPageVO>> page(@Validated BusinessParameterPageDTO dto) {
        return ResponseInfo.success(CommonPage.convertPage(businessParameterService.page(dto)));
    }

    @PutMapping("/refresh")
    @ApiOperation("刷新")
    public ResponseInfo<Void> refresh() {
        return ResponseInfo.success();
    }

    @GetMapping("/detailByCode/{code}")
    @ApiParam(value = "code", name = "编码", required = true)
    @ApiOperation("详情")
    public ResponseInfo<BusinessParameterDetailVO> detailByCode(@PathVariable String code) {
        return ResponseInfo.success(businessParameterService.detailByCode(code));
    }

    @PutMapping("/update")
    @ApiOperation("更新")
    @OperationLog(remark = "getDescription")
    public ResponseInfo<Void> update(@RequestBody @Validated UpdateBusinessParameterDTO dto) {
        businessParameterService.update(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/all")
    @ApiOperation("全量参数配置")
    public ResponseInfo<List<BusinessParameterVO>> getAllParam() {
        return ResponseInfo.success(businessParameterService.getAllParam());
    }

}
