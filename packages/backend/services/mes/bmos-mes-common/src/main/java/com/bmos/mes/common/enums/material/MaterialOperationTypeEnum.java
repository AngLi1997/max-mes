package com.bmos.mes.common.enums.material;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MaterialOperationTypeEnum implements CommonEnum<Integer> {

    IN_STORAGE(0,MaterialOperationTypeShowEnum.INBOUND.getOperate(), new MaterialSpecificOperationTypeEnum[]
            {MaterialSpecificOperationTypeEnum.IN_STORAGE_SUBMIT, MaterialSpecificOperationTypeEnum.IN_STORAGE_RECEIVE}),
    OUT_STORAGE(1, MaterialOperationTypeShowEnum.OUTBOUND.getOperate(), new MaterialSpecificOperationTypeEnum[]
            {MaterialSpecificOperationTypeEnum.OUT_STORAGE_PROVIDE, MaterialSpecificOperationTypeEnum.OUT_STORAGE_USE}),
    CHECK_STORAGE(2, MaterialOperationTypeShowEnum.CHECK.getOperate(), new MaterialSpecificOperationTypeEnum[]
            {MaterialSpecificOperationTypeEnum.CHECK_STORAGE_CHECK, MaterialSpecificOperationTypeEnum.CHECK_STORAGE_RECHECK}),
    /**
     * 入库 记录为入库操作
     */
    SEND_BACK_STORAGE(3, MaterialOperationTypeShowEnum.INBOUND.getOperate(), new MaterialSpecificOperationTypeEnum[]
            {MaterialSpecificOperationTypeEnum.SEND_BACK_STORAGE_RECEIVE, MaterialSpecificOperationTypeEnum.SEND_BACK_STORAGE_SUBMIT}),
    /**
     * 移库 记录出入库
     */
    MOVE_STORAGE(4, MaterialOperationTypeShowEnum.OUTBOUND.getOperate(), new MaterialSpecificOperationTypeEnum[]
            {MaterialSpecificOperationTypeEnum.MOVE_OUT, MaterialSpecificOperationTypeEnum.MOVE_IN}),
    /**
     * 预定
     */
    RESERVE(5, MaterialOperationTypeShowEnum.RESERVE.getOperate(), new MaterialSpecificOperationTypeEnum[]
            {MaterialSpecificOperationTypeEnum.RESERVE_CHECK, MaterialSpecificOperationTypeEnum.RESERVE_RECHECK}),
    /**
     * 取消预定
     */
    CANCEL_RESERVE(6, MaterialOperationTypeShowEnum.CANCEL_RESERVE.getOperate(), new MaterialSpecificOperationTypeEnum[]
            {MaterialSpecificOperationTypeEnum.CANCEL_RESERVE_CHECK, MaterialSpecificOperationTypeEnum.CANCEL_RESERVE_RECHECK}),
    /**
     * 拆包出库
     */
    SPLIT_PACKAGE(7, MaterialOperationTypeShowEnum.OUTBOUND.getOperate(), new MaterialSpecificOperationTypeEnum[]
            {MaterialSpecificOperationTypeEnum.SPLIT_PACKAGE_PROVIDE, MaterialSpecificOperationTypeEnum.SPLIT_PACKAGE_USE}),

    /**
     * 称量
     */
    WEIGH_CONSUME(8, MaterialOperationTypeShowEnum.WEIGH.getOperate(), new MaterialSpecificOperationTypeEnum[]
            {MaterialSpecificOperationTypeEnum.WEIGH_CONSUME}),

    /**
     * 新增
     */
    ADD(9, MaterialOperationTypeShowEnum.ADD.getOperate(), new MaterialSpecificOperationTypeEnum[]
            {MaterialSpecificOperationTypeEnum.ADD}),

    /**
     * 称量作废
     */
    WEIGH_SCRAP(10, MaterialOperationTypeShowEnum.OUTPUT.getOperate(), new MaterialSpecificOperationTypeEnum[]
            {MaterialSpecificOperationTypeEnum.WEIGH_SCRAP, MaterialSpecificOperationTypeEnum.WEIGH_SCRAP_RECHECK}),

    /**
     * 生产投料
     */
    CHARGE(11,MaterialOperationTypeShowEnum.CHARGE.getOperate(), new MaterialSpecificOperationTypeEnum[]{MaterialSpecificOperationTypeEnum.CHARGE}),

    /**
     * 物料回收
     */
    RECYCLE(12,MaterialOperationTypeShowEnum.RECYCLE.getOperate(), new MaterialSpecificOperationTypeEnum[]{MaterialSpecificOperationTypeEnum.RECYCLE}),

    /**
     * 领料接收
     */
    REQUISITION_RECEIVE(13,MaterialOperationTypeShowEnum.INBOUND.getOperate(), new MaterialSpecificOperationTypeEnum[]{MaterialSpecificOperationTypeEnum.REQUISITION_RECEIVE_RECEIVE,
            MaterialSpecificOperationTypeEnum.REQUISITION_RECEIVE_SEND}),

    /**
     * 批量领料-暂存预定
     */
    REQUISITION_RESERVE(14,MaterialOperationTypeShowEnum.RESERVE.getOperate(), new MaterialSpecificOperationTypeEnum[]{MaterialSpecificOperationTypeEnum.REQUISITION_RESERVE}),

    /**
     * 配料称量
     */
    INGREDIENT_WEIGHT(15,MaterialOperationTypeShowEnum.WEIGH.getOperate(), new MaterialSpecificOperationTypeEnum[]{
            MaterialSpecificOperationTypeEnum.INGREDIENT_WEIGHT_WEIGHT,
            MaterialSpecificOperationTypeEnum.INGREDIENT_WEIGHT_RECHECK
    }),

    /**
     * 配料称量
     */
    BATCHING_INPUT(16,MaterialOperationTypeShowEnum.CHARGE.getOperate(), new MaterialSpecificOperationTypeEnum[]{
            MaterialSpecificOperationTypeEnum.BATCHING_INPUT
    }),

    /**
     * 产出称量
     */
    OUTPUT_WEIGHT(17,MaterialOperationTypeShowEnum.OUTPUT.getOperate(), new MaterialSpecificOperationTypeEnum[]{
        MaterialSpecificOperationTypeEnum.OUTPUT_WEIGHT_WEIGHT,
                MaterialSpecificOperationTypeEnum.OUTPUT_WEIGHT_RECHECK
    }),
    /**
     * 物料取消预定 - 无复核操作的取消预定
     */
    MATERIAL_CANCEL_RESERVE(18,MaterialOperationTypeShowEnum.CANCEL_RESERVE.getOperate(), new MaterialSpecificOperationTypeEnum[]{
            MaterialSpecificOperationTypeEnum.MATERIAL_CANCEL_RESERVE
    }),

    /**
     * 物料称量
     */
    MATERIAL_WEIGHT(19,MaterialOperationTypeShowEnum.WEIGH.getOperate(), new MaterialSpecificOperationTypeEnum[]{
            MaterialSpecificOperationTypeEnum.MATERIAL_WEIGHT_WEIGHT,
            MaterialSpecificOperationTypeEnum.MATERIAL_WEIGHT_RECHECK
    }),

    /**
     * 配液量取 消耗
     */
    MEASURE_CONSUME(20,MaterialOperationTypeShowEnum.MEASURE.getOperate(), new MaterialSpecificOperationTypeEnum[]{
            MaterialSpecificOperationTypeEnum.LIQUID_MEASURE_CONSUME
    }),

    /**
     * 配液投入
     */
    PREPARATION_INPUT(21, MaterialOperationTypeShowEnum.CHARGE.getOperate(), new MaterialSpecificOperationTypeEnum[]{
            MaterialSpecificOperationTypeEnum.PREPARATION_INPUT}),
    /**
     * 物料投入
     */
    MATERIAL_INPUT(22,MaterialOperationTypeShowEnum.CHARGE.getOperate(), new MaterialSpecificOperationTypeEnum[]{
            MaterialSpecificOperationTypeEnum.MATERIAL_INPUT
    }),
    /**
     * 配液产出
     */
    PREPARATION_PRODUCE(23,MaterialOperationTypeShowEnum.OUTBOUND.getOperate(), new MaterialSpecificOperationTypeEnum[]{
            MaterialSpecificOperationTypeEnum.PREPARATION_PRODUCE, MaterialSpecificOperationTypeEnum.PREPARATION_PRODUCE_RECHECK}),


    /**
     * 退库并消耗
     */
    SEND_BACK_AND_CONSUME(24,MaterialOperationTypeShowEnum.SEND_BACK.getOperate(), new MaterialSpecificOperationTypeEnum[]{
            MaterialSpecificOperationTypeEnum.SEND_BACK, MaterialSpecificOperationTypeEnum.SEND_BACK_RECHECK}),


    /**
     * 销毁并消耗
     */
    DESTROY_AND_CONSUME(25,MaterialOperationTypeShowEnum.DESTROY.getOperate(), new MaterialSpecificOperationTypeEnum[]{
            MaterialSpecificOperationTypeEnum.DESTROY, MaterialSpecificOperationTypeEnum.DESTROY_RECHECK}),

    /**
     * 使用并消耗
     */
    USE_AND_CONSUME(26,MaterialOperationTypeShowEnum.USE.getOperate(), new MaterialSpecificOperationTypeEnum[]{
            MaterialSpecificOperationTypeEnum.USE, MaterialSpecificOperationTypeEnum.USE_RECHECK}),

    /**
     * 物料接收
     */
    RECEIVE(27, MaterialOperationTypeShowEnum.INBOUND.getOperate(), new MaterialSpecificOperationTypeEnum[]{
            MaterialSpecificOperationTypeEnum.RECEIVE_RECHECK, MaterialSpecificOperationTypeEnum.RECEIVE}),

    /**
     * 拆包出库(拆出来的物料件日志)
     */
    SPLIT_PACKAGE_NEW(28, MaterialOperationTypeShowEnum.ADD.getOperate(), new MaterialSpecificOperationTypeEnum[]
            {MaterialSpecificOperationTypeEnum.SPLIT_PACKAGE_PROVIDE, MaterialSpecificOperationTypeEnum.SPLIT_PACKAGE_USE}),
    /**
     * 手动产出
     */
    MANUAL_OUTPUT(29, MaterialOperationTypeShowEnum.OUTPUT.getOperate(), new MaterialSpecificOperationTypeEnum[]
            {MaterialSpecificOperationTypeEnum.INTERMEDIATE_OUTPUT, MaterialSpecificOperationTypeEnum.INTERMEDIATE_RECHECK}),


    /**
     * 配液产出作废
     */
    PREPARATION_PRODUCE_SCRAP(30,MaterialOperationTypeShowEnum.OUTBOUND.getOperate(), new MaterialSpecificOperationTypeEnum[]{
        MaterialSpecificOperationTypeEnum.PREPARATION_PRODUCE_SCRAP, MaterialSpecificOperationTypeEnum.PREPARATION_PRODUCE_SCRAP_RECHECK}),

    /**
     * 物料称量
     */
    MATERIAL_WEIGH_CONSUME(31, MaterialOperationTypeShowEnum.WEIGH.getOperate(), new MaterialSpecificOperationTypeEnum[]
            {MaterialSpecificOperationTypeEnum.MATERIAL_WEIGH_CONSUME}),
    PREPARATION_MEASURE(32, MaterialOperationTypeShowEnum.MEASURE.getOperate(),
            new MaterialSpecificOperationTypeEnum[]{MaterialSpecificOperationTypeEnum.PREPARATION_MEASURE, MaterialSpecificOperationTypeEnum.PREPARATION_MEASURE_RECHECK}),
    PREPARATION_MEASURE_ODD(33, MaterialOperationTypeShowEnum.MEASURE.getOperate(),
            new MaterialSpecificOperationTypeEnum[]{MaterialSpecificOperationTypeEnum.PREPARATION_MEASURE_ODD, MaterialSpecificOperationTypeEnum.PREPARATION_MEASURE_ODD_RECHECK}),
    /**
     * 余料称量
     */
    MATERIAL_ODD_WEIGHT(34,MaterialOperationTypeShowEnum.WEIGH.getOperate(), new MaterialSpecificOperationTypeEnum[]{
            MaterialSpecificOperationTypeEnum.MATERIAL_ODD_WEIGHT_WEIGHT,
            MaterialSpecificOperationTypeEnum.MATERIAL_ODD_WEIGHT_RECHECK
    }),

    /**
     * 余料称量消耗
     */
    MATERIAL_ODD_WEIGH_CONSUME(35, MaterialOperationTypeShowEnum.WEIGH.getOperate(), new MaterialSpecificOperationTypeEnum[]
            {MaterialSpecificOperationTypeEnum.MATERIAL_ODD_WEIGH_CONSUME}),

    /**
     * 称量工单执行
     */
    WEIGH_TICKET_MATERIAL_EXECUTE(36, MaterialOperationTypeShowEnum.WEIGH.getOperate(), new MaterialSpecificOperationTypeEnum[]{
            MaterialSpecificOperationTypeEnum.WEIGH_TICKET_EXECUTE_WEIGH,
            MaterialSpecificOperationTypeEnum.WEIGH_TICKET_EXECUTE_RECHECK
    }),

    /**
     * 称量工单执行 余料
     */
    WEIGH_TICKET_MATERIAL_ODD_EXECUTE(37, MaterialOperationTypeShowEnum.WEIGH.getOperate(), new MaterialSpecificOperationTypeEnum[]{
        MaterialSpecificOperationTypeEnum.WEIGH_TICKET_EXECUTE_ODD_WEIGH,
                MaterialSpecificOperationTypeEnum.WEIGH_TICKET_EXECUTE_ODD_RECHECK
    })
    ;

    @EnumValue
    private final Integer value;

    private final String name;

    @JsonIgnore
    private final MaterialSpecificOperationTypeEnum[] specificTypes;

    public static List<MaterialOperationTypeEnum> getByName(String name){
        return Arrays.stream(MaterialOperationTypeEnum.values())
                .filter(e-> Objects.equals(e.getName(), name))
                .collect(Collectors.toList());
    }

    public static MaterialOperationTypeEnum getByValue(Integer value) {
        for (MaterialOperationTypeEnum e : MaterialOperationTypeEnum.values()) {
            if (Objects.equals(e.value, value)) {
                return e;
            }
        }
        return null;
    }
}
