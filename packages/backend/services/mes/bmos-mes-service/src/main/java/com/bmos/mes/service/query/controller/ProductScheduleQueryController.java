package com.bmos.mes.service.query.controller;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.service.query.dto.ProcedureInProductionDTO;
import com.bmos.mes.service.query.service.IProductScheduleQueryService;
import com.bmos.mes.service.query.vo.ProcedureInProductionVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @className: ProductScheduleQuerController
 * @author: yigaohui
 * @date: 2024/12/4 18:14
 * @Version: 1.0
 * @description:
 */

@RestController
@RequestMapping("/product/schedule")
@Api(tags = "生产进度查询")
public class ProductScheduleQueryController {

    @Autowired
    private IProductScheduleQueryService productScheduleQueryService;
    @GetMapping
    @ApiOperation("查询生产进度-康盛科泰")
    public ResponseInfo<List<ProcedureInProductionVO>> getProcedureInProduction() {
        List<ProcedureInProductionDTO> res= productScheduleQueryService.inProduction();
        return ResponseInfo.success(BeanUtil.copyToList(res,ProcedureInProductionVO.class));
    }
}
