package com.bmos.mes.common.enums.material;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MaterialSpecificOperationTypeEnum implements CommonEnum<Integer> {

    IN_STORAGE_SUBMIT(1, "物料入库-递交"),
    IN_STORAGE_RECEIVE(2, "物料入库-接收"),
    SEND_BACK_STORAGE_SUBMIT(3, "物料退库-递交"),
    SEND_BACK_STORAGE_RECEIVE(4, "物料退库-接收"),
    OUT_STORAGE_PROVIDE(5, "物料出库-发放"),
    OUT_STORAGE_USE(6, "物料出库-领用"),
    CHECK_STORAGE_CHECK(7, "盘点-盘点"),
    CHECK_STORAGE_RECHECK(8, "盘点-复核"),
    MOVE_OUT(9, "物料移库-移出"),
    MOVE_IN(10, "物料移库-移入"),
    RESERVE_CHECK(11, "物料预定-操作"),
    RESERVE_RECHECK(12, "物料预定-复核"),
    CANCEL_RESERVE_CHECK(13, "物料取消预定-操作"),
    CANCEL_RESERVE_RECHECK(14, "物料取消预定-复核"),
    SPLIT_PACKAGE_PROVIDE(15, "物料拆包出库-发放"),
    SPLIT_PACKAGE_USE(16, "物料拆包出库-领用"),
    WEIGH_CONSUME(17, "配料称量-消耗"),
    ADD(18, "新增物料-新增"),
    WEIGH_SCRAP(19, "产出作废"),
    WEIGH_SCRAP_RECHECK(20, "产出作废-复核"),
    CHARGE(21, "生产投料"),
    RECYCLE(22,"物料回收"),
    REQUISITION_RECEIVE_RECEIVE(23,"领料接收-接收"),
    REQUISITION_RECEIVE_SEND(24,"领料接收-递交"),
    REQUISITION_RESERVE(25,"物料预定"),
    INGREDIENT_WEIGHT_WEIGHT(26,"配料称量-称量"),
    INGREDIENT_WEIGHT_RECHECK(27,"配料称量-复核"),
    BATCHING_INPUT(28,"配料投入"),
    OUTPUT_WEIGHT_WEIGHT(29,"产出称量-称量"),
    OUTPUT_WEIGHT_RECHECK(30,"产出称量-复核"),
    MATERIAL_CANCEL_RESERVE(31, "物料取消预定"),
    MATERIAL_WEIGHT_WEIGHT(32,"物料称量-称量"),
    MATERIAL_WEIGHT_RECHECK(33,"物料称量-复核"),
    LIQUID_MEASURE_CONSUME(34, "配液量取-消耗"),
    PREPARATION_INPUT(35,"配液投入"),
    PREPARATION_PRODUCE(36,"配液产出-产出"),
    PREPARATION_PRODUCE_RECHECK(37,"配液产出-复核"),
    MATERIAL_CENTRE_WEIGHT(38,"物料称量-称量"),
    MATERIAL_CENTRE_RECHECK(39,"物料称量-复核"),
    MATERIAL_INPUT(40,"物料投入"),
    SEND_BACK(41,"物料退库"),
    SEND_BACK_RECHECK(42,"物料退库-复核"),
    DESTROY(43,"物料销毁"),
    DESTROY_RECHECK(44,"物料销毁-复核"),
    USE(45,"物料使用"),
    USE_RECHECK(46,"物料使用-复核"),
    RECEIVE(47, "物料接收-接收"),
    RECEIVE_RECHECK(48, "物料接收-递交"),
    INTERMEDIATE_OUTPUT(49, "中间品产出-产出"),
    INTERMEDIATE_RECHECK(50, "中间品产出-复核"),
    PREPARATION_PRODUCE_SCRAP(51,"产出作废"),
    PREPARATION_PRODUCE_SCRAP_RECHECK(52, "产出作废-复核"),
    MATERIAL_WEIGH_CONSUME(53, "物料称量-消耗"),
    PREPARATION_MEASURE(54, "配液量取-量取"),
    PREPARATION_MEASURE_RECHECK(55, "配液量取-复核"),
    PREPARATION_MEASURE_ODD(56, "余液量取-量取"),
    PREPARATION_MEASURE_ODD_RECHECK(57, "余液量取-复核"),
    MATERIAL_ODD_WEIGHT_WEIGHT(58, "余料称量-称量"),
    MATERIAL_ODD_WEIGHT_RECHECK(59, "余料称量-复核"),
    MATERIAL_ODD_WEIGH_CONSUME(60, "余料称量-消耗"),
    WEIGH_TICKET_EXECUTE_WEIGH(61, "称量工单执行-称量"),
    WEIGH_TICKET_EXECUTE_RECHECK(62, "称量工单执行-复核"),
    WEIGH_TICKET_EXECUTE_ODD_WEIGH(63, "称量工单余料称量-称量"),
    WEIGH_TICKET_EXECUTE_ODD_RECHECK(64, "称量工单余料称量-复核"),
    ;

    @EnumValue
    private final Integer value;
    private final String name;

    public static MaterialSpecificOperationTypeEnum getEnumByValue(Integer value) {
        return Arrays.stream(MaterialSpecificOperationTypeEnum.values())
                .filter(typeEnum -> typeEnum.getValue().equals(value))
                .findFirst()
                .orElse(null);
    }

}
