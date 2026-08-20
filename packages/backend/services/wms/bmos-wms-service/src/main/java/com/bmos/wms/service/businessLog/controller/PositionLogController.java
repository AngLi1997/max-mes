package com.bmos.wms.service.businessLog.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.wms.service.businessLog.dto.PositionLogPageQuery;
import com.bmos.wms.service.businessLog.service.IPositionLogService;
import com.bmos.wms.service.businessLog.vo.PositionLogVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 货位日志查询相关接口
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/7 18:22
 */
@RestController
@RequestMapping("/log/position")
@Api(tags = "货位日志查询相关接口")
@Validated
public class PositionLogController {

    @Resource
    private IPositionLogService positionLogService;

    @GetMapping("/page")
    @ApiOperation("仓库查询 - 分页查询货位日志")
    public ResponseInfo<CommonPage<PositionLogVO>> queryPage(@Validated PositionLogPageQuery pageQuery) {
        return ResponseInfo.success(positionLogService.queryPage(pageQuery));
    }
}
