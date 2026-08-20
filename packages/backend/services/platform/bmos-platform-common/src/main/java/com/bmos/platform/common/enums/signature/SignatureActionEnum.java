package com.bmos.platform.common.enums.signature;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SignatureActionEnum implements CommonEnum<Integer> {
    RECORD_SAVE(0, "记录保存"),
    OPERATION_SIGNATURE(1, "操作签名"),
    REVIEW_SIGNATURE(2, "复核签名"),
    DATA_REVISION(3, "数据修订"),
    DATA_REVISION_REVIEW(4, "数据修订审核"),
    RECORD_COPYING(5, "记录复制"),
    RECORD_COPYING_REVIEW(6, "记录复制审核"),
    RECORD_INVALIDATION(7, "记录作废"),
    RECORD_INVALIDATION_REVIEW(8, "记录作废审核"),
    PLAN_TERMINATION_SIGNATURE(9, "计划终止签名"),
    PROCESS_REDO_SIGNATURE(10, "工序重做签名"),
    PROCESS_REDO_REVIEW(11, "工序重做审核"),
    AUDIT_SIGNATURE(12, "审核签名"),
    MATERIAL_IN_STORAGE(13, "物料入库"),
    MATERIAL_BACK_STORAGE(14, "物料退库"),
    MATERIAL_OUT_STORAGE(15, "物料出库"),
    MATERIAL_MOVE_STORAGE(16, "物料移库"),
    MATERIAL_CHECK_STORAGE(17, "物料盘点"),
    VERIFY_REQUEST_CONFIRM(18, "请验确认"),
    SAMPLE(19, "取样"),
    INSPECTION_INPUT_SUBMISSION(20, "检验录入提交"),
    INSPECTION_INPUT_SAVE(21, "检验录入保存"),
    INSPECTION_STOP(22, "检验终止"),
    INSPECTION_REPORT_GENERATION(23, "检验报告生成"),
    RE_INSPECTION_OF_REPORT(24, "检验报告重新检验"),
    INSPECTION_REPORT_AUDIT(25, "检验报告审核"),
    INSPECTION_REPORT_RELEASE(26, "检验报告签发"),
    PRODUCT_PLAN_PAUSED(27, "暂停生产"),
    PRODUCT_PLAN_RECOVERY(28, "恢复生产"),
    CARGO_IN_STORAGE(29, "货品入库"),
    CARGO_OUT_STORAGE(30, "货品出库"),
    CARGO_MOVE_STORAGE(31, "货品移库"),
    CARGO_CHECK(32, "货品盘点"),
    NEW_CARGO_BATCH(33, "新增货品批次"),
    EDIT_CARGO_BATCH(34, "编辑货品批次"),
    NEW_CARGO_ITEM(35, "新增货品件"),
    CARGO_BATCH_SEND_OUT(36, "批次发料"),
    CARGO_SEND_OUT(37, "货品发料"),
    CANCEL_CARGO_SEND_OUT(38, "取消发料"),
    NEW_MATERIAL_BATCH(39, "新增物料批次"),
    EDIT_MATERIAL_BATCH(40, "编辑物料批次"),
    NEW_STORAGE_MATERIAL(41, "新增物料件"),
    FINISHED_PRODUCT_OUTPUT(42, "成品产出"),
    INGREDIENT_WEIGH(43, "配料称量-称量"),
    INGREDIENT_WEIGH_RECHECK(44, "配料称量-复核"),
    EQUIPMENT_DATA_ACQUISITION_REVISE_CHECK(45, "设备数采修订-签名"),
    EQUIPMENT_DATA_ACQUISITION_REVISE_RECHECK(46, "设备数采修订-复核"),
    MATERIAL_CHARGE(47, "物料投入"),
    INGREDIENT_MATERIAL_CHARGE(48, "配料投入"),
    MATERIAL_RECYCLE(49, "物料回收"),
    REQUISITION_RECEIVE(50, "领料接收-接收"),
    REQUISITION_SEND(51, "领料接收-递交"),
    FINISH_REQUISITION_PLAN(52, "完成领料计划"),
    MATERIAL_OUT_STORAGE_SEND(53, "物料出库-发放"),
    MATERIAL_OUT_STORAGE_RECEIVE(54, "物料出库-领用"),
    FINISH_INGREDIENT_PLAN(55, "完成配料计划"),
    OUTPUT_WEIGH_WEIGH(56, "中间品产出-操作"),
    OUTPUT_WEIGH_RECHECK(57, "中间品产出-复核"),
    CHANGE_STATUS_CLEAN(58, "变更状态-清洁"),
    CHANGE_STATUS_DISINFECT(59, "变更状态-消毒"),
    CHANGE_STATUS_CALIBRATION(60, "变更状态-校准"),
    CHANGE_STATUS_FAULT(61, "变更状态-故障"),
    CHANGE_STATUS_RELEASE(62, "变更状态-释放"),
    CHANGE_STATUS_OCCUPATION(63, "变更状态-占用"),
    EXCESS_MATERIAL_WEIGHING_CONFIRM_OF_EXCEEDING_RANGE(64, "余料称量-超范围确认"),
    INGREDIENT_WEIGHING_CHANGING_OPERATORS(65, "配料称量-更换操作人"),
    FINISH_INGREDIENT_WEIGH(66, "完成配料称量"),
    PRODUCTION_FEEDING(67, "生产投料"),
    MATERIAL_CANCEL_RESERVE_OPERATOR(68, "物料取消预定-操作"),
    MATERIAL_CANCEL_RESERVE_RECHECK(69, "物料取消预定-复核"),
    UNPACKING_AND_OUTBOUND_SEND(70, "拆包出库-发放"),
    UNPACKING_AND_OUTBOUND_RECEIVE(71, "拆包出库-领用"),
    MATERIAL_RESERVE(72, "物料预定-操作"),
    MATERIAL_RESERVE_RECHECK(73, "物料预定-复核"),
    MATERIAL_CHECK_STORAGE_RECHECK(74, "物料盘点-复核"),
    OUTPUT_WEIGH_CHANGE_OPERATOR(75, "中间品产出-更换操作人"),
    WEIGH_NULLIFY(76, "产出作废"),
    WEIGH_NULLIFY_RECHECK(77, "产出作废-复核"),
    MATERIAL_RETURN_SEND(78, "物料退库"),
    MATERIAL_RETURN_RECEIVE(79, "物料退库-复核"),
    COMPLETE_THE_WEIGH_OF_ODDMENT(80, "完成余料称量"),
    MATERIAL_IN_STORAGE_RECEIVE(81, "物料入库-接收"),
    MATERIAL_IN_STORAGE_SEND(82, "物料入库-递交"),
    HANDWRITTEN_SIGNATURE_SAVE(83, "手写签名保存"),
    HANDWRITTEN_SIGNATURE_RECHECK(84, "手写签名复核"),
    HANDWRITTEN_SIGNATURE_COMMIT(85, "手写签名提交"),
    ROOM_STATUS_MODIFY(86, "房间状态变更签名"),
    ROOM_STATUS_MODIFY_RECHECK(87, "房间状态变更签名复核"),
    ROOM_CLEAN(88, "清场签名"),
    ROOM_CLEAN_RECHECK(89, "清场签名复核"),
    MATERIAL_CENTRE_WEIGHT(90,"物料称量-称量"),
    MATERIAL_CENTRE_RECHECK(91,"物料称量-复核"),
    LIQUID_PREPARATION_COMPLETE(92,"完成配液计划"),
    MATERIAL_CENTRE_WEIGH_FINISHED(93,"完成物料称量"),
    MATERIAL_CENTRE_WEIGH_CHANGE_BATCH(94,"称量中心-更换物料批次"),
    MATERIAL_CENTRE_WEIGH_CHANGE_WEIGHER(95,"物料称量-更换操作人"),
    MATERIAL_CENTRE_ODD_WEIGH_OUT(96,"余料称量-超范围确认"),
    MATERIAL_CENTRE_ODD_FINISHED(97,"完成余料称量"),
    MATERIAL_INPUT(98,"物料投入"),
    LIQUID_PREPARATION_MEASURE_OPERATE(99, "配液量取-操作"),
    LIQUID_PREPARATION_MEASURE_RECHECK(100, "配液量取-复核"),
    LIQUID_PREPARATION_MEASURE_FINISH(101, "完成配液量取"),
    ODD_LIQUID_PREPARATION_MEASURE_FINISH(102, "完成余液量取"),
    LIQUID_PREPARATION_OUTPUT_OPERATE(103, "配液产出-操作"),
    LIQUID_PREPARATION_OUTPUT_RECHECK(104, "配液产出-复核"),
    LIQUID_PREPARATION_INPUT(105, "配液投入"),
    LIQUID_PREPARATION_OUTPUT_SCRAP_OPERATE(106, "产出作废-操作"),
    LIQUID_PREPARATION_OUTPUT_SCRAP_RECHECK(107, "产出作废-复核"),
    LIQUID_PREPARATION_OUTPUT_CHANGE_OPERATOR(108, "配液产出-更换操作人"),
    MATERIAL_RECEPTION_RECEIVE(109, "物料接收-接收"),
    MATERIAL_RECEPTION_SEND(110, "物料接收-递交"),
    STEP_TASK_FORCE_START(111, "步骤/任务强行开启"),
    TERMINATION_OF_PRODUCTION(112, "终止生产"),
    HISTORY_MODIFY_DATA_SAVE(113, "数据保存-保存"),
    HISTORY_MODIFY_DATA_SAVE_RECHECK(114, "数据保存-复核"),
    HISTORY_MODIFY_DATA_MODIFY(115, "数据修订-修订"),
    HISTORY_MODIFY_DATA_MODIFY_RECHECK(116, "数据修订-复核"),
    EXCEPTION_RECORD_SAVE(117, "新增异常记录"),
    EXCEPTION_RECORD_EDIT(118, "编辑异常记录"),
    EXCEPTION_RECORD_HANDLE(119, "处理异常记录"),
    EXCEPTION_RECORD_CANCEL(120, "作废异常记录"),
    EXCEPTION_RECORD_RE_INVESTIGATE(121, "重新调查异常记录"),
    MATERIAL_DESTRUCTION(122, "物料销毁"),
    MATERIAL_DESTRUCTION_RECHECK(123, "物料销毁-复核"),
    MATERIAL_USAGE(124, "物料使用"),
    MATERIAL_USAGE_RECHECK(125, "物料使用-复核"),
    STEP_TASK_FORCE_COMPLETE(126, "步骤/任务强行完成"),
    PROCEDURE_FORCE_COMPLETE(127, "工序强制完成"),
    MEASURE_ODD_OUT_CONFIRM(128, "余液量取-超范围确认"),
    MATERIAL_WEIGHING(130, "物料称量-操作"),
    MEASURE_CHANGE_OPERATOR(131, "配液量取-更换操作人"),
    MATERIAL_WEIGHING_RECHECK(132, "物料称量-复核"),
    WEIGH_TICKET_SIGN_OPERATOR(135, "称量工单执行-称量"),
    WEIGH_TICKET_SIGN_RECHECK(136, "称量工单执行-复核"),
    WEIGH_TICKET_COMPLETE(137, "完成物料称量"),
    WEIGH_TICKET_ODD_OUT_RANGE_CONFIRM(138, " 余料称量-超范围确认"),
    WEIGH_TICKET_ODD_COMPLETE(139, "完成余料称量"),
    MATERIAL_WEIGHING_CHANGE_OPERATOR(140, "称量工单执行-更换操作人"),
    GENERATE_INSPECTION_REPORT(901, "生成检验报告"),
    PROCESS_INSPECTION_REPORT(902, "检验报告审核"),
    GENERATE_QUARANTINE_PERIOD_VERIFICATION_REPORT(903, "创建检疫期核查报告"),
    PROCESS_QUARANTINE_PERIOD_VERIFICATION_REPORT(904, "检疫期核查报告审核"),
    GENERATE_RELEASE_ORDER(905, "创建放行单"),
    PROCESS_RELEASE_ORDER(906, "放行单审核"),
    ISSUE_NON_CONFORMANCE_RECORDS(907, "出具不合格记录"),
    PROCESS_NON_CONFORMANCE_RECORDS(908, "不合格记录审核"),
    ISSUE_NON_CONFORMANCE_VERIFICATION_REPORT(909, "出具不合格核查报告"),
    PROCESS_NON_CONFORMANCE_VERIFICATION_REPORT(910, "不合格核查报告审核"),
    PLASMA_OUTBOUND_VERIFICATION(911, "血浆出库核对"),
    SUBMIT_QUARANTINE_PERIOD_VERIFICATION_REPORT(912, "检疫期核查报告送审"),
    SUBMIT_NON_CONFORMANCE_VERIFICATION_REPORT(913, "不合格核查报告送审"),
    // 集中化lims使用1001到1100
    LIMS_SAMPLE_SYNC(1001, "浆站同步标本"),
    LIMS_SAMPLE_RECEIVE(1002, "标本接收"),
    LIMS_SAMPLE_AUDIT(1003, "接收审核"),
    LIMS_PE_READ(1004, "蛋白电泳读取"),
    LIMS_PE_PUBLISHED(1005, "蛋白电泳发布"),
    LIMS_INSPECT_DATA_PUBLISHED(1006, "检验数据发布"),
    LIMS_INSPECT_DATA_AUDIT(1007, "检验数据审核"),
    LIMS_INSPECT_REPORT_PUBLISHED(1008, "检验报告发布"),
    LIMS_INSPECT_REPORT_AUDIT(1009, "检验报告审核"),
    LIMS_KEY_CONTROL_POINT_INSPECTION(1010, "关键控制点检查"),
    LIMS_INSPECT_REPORT_SING_REVOKE(1011, "检验报告撤回"),
    ;
    @EnumValue
    private final Integer value;
    @JsonValue
    private final String name;

    public static SignatureActionEnum getByValue(Integer value) {
        return Arrays.stream(SignatureActionEnum.values())
                .filter(item -> item.value.equals(value))
                .findFirst()
                .orElse(null);
    }

    @JsonCreator
    public static SignatureActionEnum fromValue(int value) {
        for (SignatureActionEnum action : SignatureActionEnum.values()) {
            if (action.getValue() == value) {
                return action;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}
