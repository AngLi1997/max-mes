package com.bmos.mes.service.tareweigh.config.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.tareweigh.config.dto.TareWeighConfigCreateDTO;
import com.bmos.mes.service.tareweigh.config.dto.TareWeighConfigEditDTO;
import com.bmos.mes.service.tareweigh.config.dto.TareWeighConfigQuery;
import com.bmos.mes.service.tareweigh.config.service.ITareWeighConfigService;
import com.bmos.mes.service.tareweigh.config.vo.TareWeighConfigVO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/9/23 10:33
 */
@RestController
@RequestMapping("/tareWeigh/config")
@Api(tags = "皮重配置接口")
public class TareWeighConfigController {

    @Resource
    private ITareWeighConfigService tareWeighConfigService;

    @GetMapping("/page")
    @ApiOperation("分页查询皮重配置")
    public ResponseInfo<CommonPage<TareWeighConfigVO>> queryPage(@Validated TareWeighConfigQuery query){
        return ResponseInfo.success(tareWeighConfigService.queryPage(query));
    }

    @GetMapping("/queryById")
    @ApiOperation("根据id查询皮重配置")
    @ApiImplicitParam(name = "id", value = "皮重配置id", required = true)
    public ResponseInfo<TareWeighConfigVO> queryTareWeighConfigById(@RequestParam Long id){
        return ResponseInfo.success(tareWeighConfigService.queryTareWeighConfigById(id));
    }

    @PostMapping("/create")
    @ApiOperation("创建皮重配置")
    @OperationLog
    public ResponseInfo<Void> createTareWeighConfig(@Validated @RequestBody TareWeighConfigCreateDTO dto){
        tareWeighConfigService.createTareWeighConfig(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/edit")
    @ApiOperation("编辑皮重配置")
    @OperationLog
    public ResponseInfo<Void> editTareWeighConfig(@Validated @RequestBody TareWeighConfigEditDTO dto){
        tareWeighConfigService.editTareWeighConfig(dto);
        return ResponseInfo.success();
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除皮重配置")
    @ApiImplicitParam(name = "id", value = "皮重配置id", required = true)
    @OperationLog
    public ResponseInfo<Void> deleteTareWeighConfig(@RequestParam Long id){
        tareWeighConfigService.deleteTareWeighConfig(id);
        return ResponseInfo.success();
    }
}
