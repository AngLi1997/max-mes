package com.bmos.mes.service.storage.log.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.service.storage.log.dto.StorageMaterialPositionLogPageQuery;
import com.bmos.mes.service.storage.log.service.IStorageMaterialPositionLogService;
import com.bmos.mes.service.storage.log.vo.StorageMaterialPositionLogVO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 货位日志
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/6 17:45
 */
@RestController
@RequestMapping("/storage/log")
@Validated
@Api(tags = "货位日志")
public class StorageMaterialPositionLogController {

    @Resource
    private IStorageMaterialPositionLogService storageMaterialPositionLogService;

    @GetMapping("/page")
    @ApiOperation("分页查询货位日志")
    public ResponseInfo<CommonPage<StorageMaterialPositionLogVO>> queryPage(StorageMaterialPositionLogPageQuery pageQuery) {
        return ResponseInfo.success(storageMaterialPositionLogService.queryPage(pageQuery));
    }

}
