package com.bmos.mes.service.process.controller;

import cn.hutool.core.util.StrUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.service.process.dto.task.CheckoutExpressionDTO;
import com.bmos.mes.service.process.service.condition.ITaskConditionCalculator;
import com.bmos.mes.service.process.service.condition.event.EquipmentStatusType;
import com.bmos.mes.service.process.service.condition.event.RoomStatusType;
import com.bmos.mes.service.process.service.task.ProcedureExpressionService;
import com.bmos.mq.listener.Event.StateEvent;
import com.bmos.mq.listener.enums.StateEventTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author renjinguang
 */
@RestController
@RequestMapping("/procedure/expression")
@Api(tags = "工序相关接口")
public class ProcedureExpressionController {

    @Autowired
    private ProcedureExpressionService expressionService;

    @Autowired
    private ITaskConditionCalculator taskConditionCalculator;


    @PostMapping("/checkout/expression")
    @ApiOperation("校验表达式")
    public ResponseInfo<Boolean> checkoutExpression(@RequestBody @Validated CheckoutExpressionDTO dto) {
        return ResponseInfo.success(expressionService.checkoutExpression(dto));
    }

    @PostMapping("/condition/update")
    @ApiOperation("更行设备/房间状态")
    public void conditionUpdate(@RequestBody StateEvent event) {
        // 设备状态变更
        if (StrUtil.equals(event.getType(), StateEventTypeEnum.EQUIPMENT.getCode())) {
            EquipmentStatusType equipmentStatusType = new EquipmentStatusType(null, event.getId(),
                    Integer.valueOf(event.getState()));
            taskConditionCalculator.refreshConditionResult(equipmentStatusType);
        }
        // 房间状态变更
        if (StrUtil.equals(event.getType(), StateEventTypeEnum.ROOM.getCode())) {
            RoomStatusType roomStatusType = new RoomStatusType(null, event.getId(), Integer.valueOf(event.getState()));
            taskConditionCalculator.refreshConditionResult(roomStatusType);
        }
    }

}
