package com.bmos.mes.service.weigh.free.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.ingredient.weigh.vo.WeighBalanceEquipment;
import com.bmos.mes.service.weigh.free.dto.FreeWeighDTO;
import com.bmos.mes.service.weigh.free.dto.FreeWeighHistoryPageQuery;
import com.bmos.mes.service.weigh.free.service.IFreeWeighService;
import com.bmos.mes.service.weigh.free.vo.FreeWeighHistoryPage;
import com.bmos.mes.service.weigh.free.vo.FreeWeighResult;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 自由称量
 * @author liang
 * @version 1.0.0
 * @date 2025/2/25 09:54
 */
@RestController
@RequestMapping("/free/weigh")
@Api(tags = "自由称量接口")
public class FreeWeighController {

    @Resource
    private IFreeWeighService freeWeighService;


    @PostMapping("/weighAndPrint")
    @ApiOperation("称量打码")
    @OperationLog
    public ResponseInfo<FreeWeighResult> weighAndPrint(@Validated @RequestBody FreeWeighDTO dto){
        return ResponseInfo.success(freeWeighService.weighAndPrint(dto));
    }

    @GetMapping("/getBalanceList")
    @ApiOperation("获取所有秤具列表")
    public ResponseInfo<List<WeighBalanceEquipment>> getBalanceList() {
        return ResponseInfo.success(freeWeighService.getBalanceList());
    }

    @GetMapping("/queryHistory")
    @ApiOperation("查询称量历史分页")
    @OperationLog
    public ResponseInfo<CommonPage<FreeWeighHistoryPage>> queryHistory(@Validated FreeWeighHistoryPageQuery pageQuery){
        return ResponseInfo.success(freeWeighService.queryHistoryPage(pageQuery));
    }
}
