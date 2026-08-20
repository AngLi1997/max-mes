package com.bmos.mes.common.enums.process.task;

import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConditionTypeEnum implements CommonEnum<String> {

    STEP_NODE_COMPLETE("步骤节点完成","step_node_complete"),
    TASK_NODE_COMPLETE("任务节点完成", "task_node_complete"),
    EQUIPMENT_USE_STATE("设备使用状态", "equipment_use_state"),
    ROOM_STATE("房间状态", "room_state"),
    MATERIAL_RESERVE_NUMBER("物料预定量","material_reserve_number"),
    DOSING_SIGNATURE("配料称量签名","dosing_signature"),
    OUTPUT_SIGNATURE("中间品产出签名","output_signature");

    private final String name;
    private final String value;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
