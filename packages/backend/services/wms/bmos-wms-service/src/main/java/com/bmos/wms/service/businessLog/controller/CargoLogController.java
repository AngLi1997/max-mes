package com.bmos.wms.service.businessLog.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.wms.service.businessLog.dto.CargoLogPageQuery;
import com.bmos.wms.service.businessLog.service.ICargoLogService;
import com.bmos.wms.service.businessLog.vo.CargoLogVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 货品日志查询相关接口
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/7 18:22
 */
@RestController
@RequestMapping("/log/cargo")
@Api(tags = "货品日志查询相关接口")
@Validated
public class CargoLogController {

    @Resource
    private ICargoLogService cargoLogService;

    @GetMapping("/page")
    @ApiOperation("仓库查询 - 分页查询货品日志")
    public ResponseInfo<CommonPage<CargoLogVO>> queryPage(@Validated CargoLogPageQuery pageQuery) {
        return ResponseInfo.success(cargoLogService.queryPage(pageQuery));
    }
}
