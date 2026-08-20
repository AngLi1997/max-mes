package com.bmos.mes.service.weigh.data.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.weigh.data.dto.WeighDataDTO;
import com.bmos.mes.service.weigh.data.service.IWeighDataService;
import com.bmos.mes.service.weigh.data.vo.WeighDataVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.simpleframework.xml.core.Validate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 称量数据组件接口
 * @author liang
 * @version 1.0.0
 * @date 2024/11/12 17:58
 */
@RestController
@RequestMapping("/weigh/data")
@Api(tags = "称量数据组件接口")
public class WeighDataController {

    @Resource
    private IWeighDataService weighDataService;

    @ApiOperation("保存称量数据")
    @PostMapping("/saveData")
    @OperationLog
    public ResponseInfo<Void> saveData(@Validate @RequestBody WeighDataDTO dto){
        weighDataService.saveData(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/getWeighList")
    @ApiOperation("查询称量记录")
    @ApiImplicitParam(name = "componentInstanceId", value = "组件实例id", required = true)
    public ResponseInfo<List<WeighDataVO>> getWeighList(@RequestParam Long componentInstanceId){
        return ResponseInfo.success(weighDataService.getWeighList(componentInstanceId));
    }
}
