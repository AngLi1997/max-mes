package com.bmos.mes.service.ingredient.input.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.ingredient.input.dto.IngredientInputDTO;
import com.bmos.mes.service.ingredient.input.dto.InputComponentInstanceQueryDTO;
import com.bmos.mes.service.ingredient.input.dto.PendingInputPlanListQueryListDTO;
import com.bmos.mes.service.ingredient.input.service.IIngredientInputService;
import com.bmos.mes.service.ingredient.input.vo.IngredientInputPlanVO;
import com.bmos.mes.service.ingredient.input.vo.InputComponentInstanceVO;
import com.bmos.mes.service.ingredient.weigh.vo.IngredientPlanItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 投料相关接口
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/25 22:13
 */
@RestController
@RequestMapping("/ingredient/input")
@Api(tags = "[移动端]配料投入")
public class IngredientInputController {

    @Resource
    private IIngredientInputService ingredientInputService;

    @GetMapping("/queryPendingInputPlanList")
    @ApiOperation("获取未投料的配料单列表")
    public ResponseInfo<List<IngredientPlanItemVO>> queryPendingInputPlanList(@Validated PendingInputPlanListQueryListDTO dto) {
        return ResponseInfo.success(ingredientInputService.queryPendingInputPlanList(dto));
    }

    @GetMapping("/queryInputListByPlanId")
    @ApiOperation("根据配料计划id查询待投料列表")
    @ApiImplicitParam(name = "id", value = "配料单id", required = true)
    public ResponseInfo<IngredientInputPlanVO> queryInputListByPlanId(@RequestParam Long ingredientPlanId, @RequestParam Long componentInstanceId) {
        return ResponseInfo.success(ingredientInputService.queryInputListByPlanId(ingredientPlanId, componentInstanceId));
    }

    @PostMapping("/input")
    @ApiOperation("投料")
    @OperationLog
    public ResponseInfo<Void> input(@RequestBody @Validated IngredientInputDTO dto) {
        ingredientInputService.input(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/instance")
    @ApiOperation("获取组件唯一实例id")
    public ResponseInfo<InputComponentInstanceVO> getInputComponentInstance(@Validated InputComponentInstanceQueryDTO dto){
        return ResponseInfo.success(ingredientInputService.getInputComponentInstance(dto));
    }
}
