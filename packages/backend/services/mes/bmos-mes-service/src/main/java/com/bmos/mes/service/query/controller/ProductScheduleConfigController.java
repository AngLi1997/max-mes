package com.bmos.mes.service.query.controller;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.service.query.dto.ProductScheduleProcedureConfigDTO;
import com.bmos.mes.service.process.service.ProcedureService;
import com.bmos.mes.service.query.vo.ProductScheduleProcedureConfigVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @className: ProductScheduleConfig
 * @author: yigaohui
 * @date: 2024/12/4 18:12
 * @Version: 1.0
 * @description:
 */

@Api(tags = "生产进度展示配置")
@RequestMapping("/product/schedule/config")
@RestController
public class ProductScheduleConfigController {

    @Autowired
    private ProcedureService procedureService;

    @ApiOperation("配置生产进度展示工序-康盛科泰")
    @PostMapping("/config/product/schedule/procedure")
    public ResponseInfo<Void> configProductScheduleProcedure(@RequestBody List<ProductScheduleProcedureConfigVO> configVOS) {
        procedureService.saveProductScheduleProcedureConfig(BeanUtil.copyToList(configVOS, ProductScheduleProcedureConfigDTO.class));
        return ResponseInfo.success();
    }


    @ApiOperation("查询生产进度展示工序-康盛科泰")
    @GetMapping("/config/product/schedule/procedure")
    public ResponseInfo<List<ProductScheduleProcedureConfigVO>> getProductScheduleProcedureConfig() {
        return ResponseInfo.success(BeanUtil.copyToList(procedureService.getProductScheduleProcedureConfig(), ProductScheduleProcedureConfigVO.class));
    }
}
