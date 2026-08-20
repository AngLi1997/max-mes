package com.bmos.mes.service.product.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.service.product.dto.MaterialLogPageQueryDTO;
import com.bmos.mes.service.product.service.MaterialLogService;
import com.bmos.mes.service.product.vo.MaterialLogPageVO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/material/log")
@Api(tags = "物料日志")
public class MaterialLogController {

    @Autowired
    private MaterialLogService materialLogService;

    @GetMapping("/page")
    @ApiOperation("物料日志分页")
    public ResponseInfo<CommonPage<MaterialLogPageVO>> getMaterialLogPage(@Validated MaterialLogPageQueryDTO dto){
        return ResponseInfo.success(materialLogService.getMaterialLogPage(dto));
    }

}
