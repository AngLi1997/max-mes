package com.bmos.mes.common.enums.storage;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 暂存物料操作类型枚举
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/18 10:14
 */
@AllArgsConstructor
@Getter
public enum StorageOperateTypeEnum implements CommonEnum<String> {

    /**
     * 入库
     */
    INBOUND("INBOUND", StorageOperateTypeShowEnum.INBOUND.getOperate(), "物料入库", new String[]{"递交", "接收"}),

    /**
     * 出库
     */
    OUTBOUND("OUTBOUND", StorageOperateTypeShowEnum.OUTBOUND.getOperate(), "物料出库", new String[]{"发放", "领用"}),

    /**
     * 入库
     */
    SEND_BACK("SEND_BACK", StorageOperateTypeShowEnum.INBOUND.getOperate(), "物料入库", new String[]{"递交", "接收"}),

    /**
     * 盘增
     */
    PLUS("PLUS", StorageOperateTypeShowEnum.PLUS.getOperate(), "物料盘点", new String[]{"盘增", "复核"}),

    /**
     * 盘减
     */
    MINUS("MINUS", StorageOperateTypeShowEnum.MINUS.getOperate(), "物料盘点", new String[]{"盘减", "复核"}),

    /**
     * 移库
     */
    MOVE("MOVE", StorageOperateTypeShowEnum.OUTBOUND.getOperate(), "物料移库", new String[]{"移出", "移入"}),

    /**
     * 预定
     */
    RESERVE("RESERVE", null, "物料预定", new String[]{"操作", "复核"}),

    /**
     * 取消预定
     */
    CANCEL_RESERVE("CANCEL_RESERVE", null, "物料取消预定", new String[]{"操作", "复核"}),

    /**
     * 拆包入库
     */
    SPLIT_PACKAGE("SPLIT_PACKAGE", StorageOperateTypeShowEnum.OUTBOUND.getOperate(), "拆包出库", new String[]{"发放", "领用"}),

    /**
     * 拆包出库(拆出来的物料件)
     */
    SPLIT_PACKAGE_NEW("SPLIT_PACKAGE_NEW", null, "拆包出库", new String[]{"发放", "领用"}),

    /**
     * 配料消耗
     */
    WEIGH_CONSUME("WEIGH_CONSUME", null, "配料称量", new String[]{"消耗"}),

    /**
     * 物料称量消耗
     */
    MATERIAL_WEIGH_CONSUME("MATERIAL_WEIGH_CONSUME", null, "物料称量", new String[]{"消耗"}),

    /**
     * 余料称量
     */
    MATERIAL_ODD_WEIGH_CONSUME("MATERIAL_ODD_WEIGH_CONSUME", null, "余料称量", new String[]{"消耗"}),

    /**
     * 配液消耗
     */
    MEASURE_CONSUME("MEASURE_CONSUME", null, "配液量取", new String[]{"消耗"}),

    /**
     * 配液量取
     */
    MEASURE_WEIGH("MEASURE_WEIGH", StorageOperateTypeShowEnum.INBOUND.getOperate(), "物料入库", new String[]{"量取", "复核"}),

    /**
     * 余液量取
     */
    MEASURE_ODD("MEASURE_ODD", StorageOperateTypeShowEnum.INBOUND.getOperate(), "物料入库", new String[]{"量取", "复核"}),

    /**
     * 新增
     */
    ADD("ADD", null, "新增物料", new String[]{"新增"}),

    /**
     * 投料
     */
    CHARGE("CHARGE", null, "投料", new String[]{"生产投料"}),

    /**
     * 回收
     */
    RECYCLE("RECYCLE", null, "回收", new String[]{"物料回收"}),

    /**
     * 称量作废
     */
    WEIGH_SCRAP("WEIGH_SCRAP", StorageOperateTypeShowEnum.OUTBOUND.getOperate(), "产出作废", new String[]{"", "复核"}),

    /**
     * 领料接收
     */
    REQUISITION_RECEIVE("REQUISITION_RECEIVE",StorageOperateTypeShowEnum.INBOUND.getOperate(),"领料接收",new String[]{"接收","递交"}),

    /**
     * 配料称量
     */
    INGREDIENT_WEIGHT("INGREDIENT_WEIGHT", StorageOperateTypeShowEnum.INBOUND.getOperate(), "物料入库", new String[]{"称量", "复核"}),

    /**
     * 物料称量
     */
    MATERIAL_WEIGHT("MATERIAL_WEIGHT", StorageOperateTypeShowEnum.INBOUND.getOperate(), "物料入库", new String[]{"称量", "复核"}),

    /**
     * 余料称量
     */
    MATERIAL_ODD_WEIGHT("MATERIAL_ODD_WEIGHT", StorageOperateTypeShowEnum.INBOUND.getOperate(), "物料入库", new String[]{"称量", "复核"}),

    /**
     * 产出称量
     */
    OUTPUT_WEIGHT("OUTPUT_WEIGHT", StorageOperateTypeShowEnum.INBOUND.getOperate(), "物料入库", new String[]{"称量", "复核"}),
    /**
     * 手动产出
     */
    MANUAL_OUTPUT("MANUAL_OUTPUT", StorageOperateTypeShowEnum.INBOUND.getOperate(), "物料入库", new String[]{"产出", "复核"}),

    /**
     * 暂存物料预定
     */
    MATERIAL_RESERVE("MATERIAL_RESERVE", null,"暂存物料预定", new String[]{"物料预定"}),

    /**
     * 暂存物料取消预定
     */
    MATERIAL_CANCEL_RESERVE("MATERIAL_CANCEL_RESERVE", null,"暂存物料取消预定", new String[]{"物料取消预定"}),

    /**
     * 配料投入
     */
    BATCHING_INPUT("BATCHING_INPUT",null, "配料投入",new String[]{"配料投入"}),

    /**
     * 配液投入
     */
    PREPARATION_INPUT("PREPARATION_INPUT",null, "配液投入",new String[]{"配液投入"}),

    /**
     * 配液产出作废
     */
    PREPARATION_SCRAP("PREPARATION_SCRAP", StorageOperateTypeShowEnum.OUTBOUND.getOperate(), "产出作废", new String[]{"", "复核"}),

    /**
     * 物料投入
     */
    MATERIAL_INPUT("MATERIAL_INPUT",null, "物料投入",new String[]{"物料投入"}),
    /**
     * 配液产出
     */
    PREPARATION_PRODUCE("PREPARATION_PRODUCE",StorageOperateTypeShowEnum.INBOUND.getOperate(), "物料入库",new String[]{"产出", "复核"}),

    /**
     * 退库并消耗
     */
    SEND_BACK_AND_CONSUME("SEND_BACK_AND_CONSUME", StorageOperateTypeShowEnum.SEND_BACK.getOperate(), "物料退库", new String[]{"", "复核"}),

    /**
     * 销毁并消耗
     */
    DESTROY_AND_CONSUME("DESTROY_AND_CONSUME", StorageOperateTypeShowEnum.DESTROY.getOperate(), "物料销毁", new String[]{"", "复核"}),

    /**
     * 使用并消耗
     */
    USE_AND_CONSUME("USE_AND_CONSUME", StorageOperateTypeShowEnum.USE.getOperate(), "物料使用", new String[]{"", "复核"}),

    /**
     * 移动端物料接收
     */
    RECEIVE("RECEIVE", StorageOperateTypeShowEnum.INBOUND.getOperate(), "物料接收", new String[]{"递交", "接收"}),

    /**
     * 称量工单执行货位日志
     */
    WEIGH_TICKET_POSITION_EXECUTE("WEIGH_TICKET_POSITION_EXECUTE", StorageOperateTypeShowEnum.INBOUND.getOperate(), "物料入库", new String[]{"称量", "复核"}),

    /**
     * 称量工单执行 余料称量
     */
    WEIGH_TICKET_POSITION_ODD_EXECUTE("WEIGH_TICKET_POSITION_ODD_EXECUTE", StorageOperateTypeShowEnum.INBOUND.getOperate(), "物料入库", new String[]{"称量", "复核"});

    @EnumValue
    private final String value;

    private final String name;

    private final String prefix;

    private final String[] subTypes;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }

    public static List<StorageOperateTypeEnum> getByName(String name){
        return Arrays.stream(StorageOperateTypeEnum.values())
                .filter(e-> Objects.equals(e.getName(), name))
                .collect(Collectors.toList());
    }

    public static StorageOperateTypeEnum getByValue(String value){
        return Arrays.stream(StorageOperateTypeEnum.values())
                .filter(e-> Objects.equals(e.getValue(), value))
                .findFirst().orElse(null);
    }
}
