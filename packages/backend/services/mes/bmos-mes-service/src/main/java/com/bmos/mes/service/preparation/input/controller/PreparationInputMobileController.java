package com.bmos.mes.service.preparation.input.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.preparation.input.controller.vo.PreparationPlanItemVO;
import com.bmos.mes.service.preparation.input.controller.vo.PreparationInputComponentInstanceVO;
import com.bmos.mes.service.preparation.input.controller.vo.PreparationInputPlanVO;
import com.bmos.mes.service.preparation.input.service.PreparationInputService;
import com.bmos.mes.service.preparation.input.service.dto.PreparationCompleteDTO;
import com.bmos.mes.service.preparation.input.service.dto.PreparationInputBindPlanDTO;
import com.bmos.mes.service.preparation.input.service.dto.PreparationInputComponentInstanceDTO;
import com.bmos.mes.service.preparation.input.service.dto.PreparationInputDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 配液投入控制器
 */
@RestController
@RequestMapping("/mobile/preparation/input")
@Api(tags = "【移动端】配液投入")
@Validated
public class PreparationInputMobileController {

    @Autowired
    private PreparationInputService preparationInputService;

    /**
     * 根据配液投入组件实例DTO查询配液投入组件实例VO
     * @param dto
     * @return
     */
    @GetMapping("/instance")
    @ApiOperation("获取配液投入组件实例(获取当前组件绑定的配液单)")
    public ResponseInfo<PreparationInputComponentInstanceVO> getInputComponentInstance(PreparationInputComponentInstanceDTO dto) {
        return ResponseInfo.success(preparationInputService.getInputComponentInstance(dto));
    }

    /**
     * 根据生产批号查询未投入的配液单信息
     * @param productPlanId
     * @return
     */
    @GetMapping("/queryPendingInputPlanList")
    @ApiOperation("获取未投入的配液单列表")
    public ResponseInfo<List<PreparationPlanItemVO>> queryPendingInputPlanList(@RequestParam("productPlanId") Long productPlanId) {
        return ResponseInfo.success(preparationInputService.queryPendingInputPlanList(productPlanId));
    }

    /**
     * 绑定配液单
     * @param dto
     * @return: 组件实例id
     */
    @PostMapping("/bind")
    @ApiOperation("绑定配液单")
    @OperationLog
    public ResponseInfo<Long> bindPreparationPlan(@RequestBody PreparationInputBindPlanDTO dto){
        return ResponseInfo.success(preparationInputService.bindPreparationPlan(dto));
    }

    /**
     * 根据配液投入组件实例id查询当前配液投入组件绑定的配液单下的投料列表
     * @param componentInstanceId: 组件实例id
     * @return
     */
    @GetMapping("/queryInputList")
    @ApiOperation("根据配液投入组件实例id查询当前配液投入组件绑定的配液单下的投料列表")
    @ApiImplicitParam(name = "id", value = "配液单id", required = true)
    public ResponseInfo<PreparationInputPlanVO> queryInputListByPlanId(@RequestParam Long componentInstanceId) {
        return ResponseInfo.success(preparationInputService.queryInputListByPlanId(componentInstanceId));
    }

    @PostMapping("/operate")
    @ApiOperation("配液投入操作")
    @OperationLog
    public ResponseInfo<String> input(@RequestBody PreparationInputDTO dto) {
        preparationInputService.input(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/complete")
    @ApiOperation("完成配液投入")
    public ResponseInfo<Void> complete(@RequestBody @Validated PreparationCompleteDTO dto) {
        preparationInputService.complete(dto);
        return ResponseInfo.success();
    }


}
