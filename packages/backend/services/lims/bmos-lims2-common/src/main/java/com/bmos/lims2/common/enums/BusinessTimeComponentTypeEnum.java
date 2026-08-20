package com.bmos.lims2.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum BusinessTimeComponentTypeEnum implements CommonEnum<String> {


    /**
     * 批次量领料 有效期至
     */
    BATCH_QUANTITY_PICK_EXPIRATION_DATE("BATCH_QUANTITY_PICK_EXPIRATION_DATE", "有效日期", "yyyy-MM-dd", "yMd", false),

    /**
     * 配料计划
     */
    INGREDIENTS_PLAN_MATERIAL_EXPIRATION_DATE("INGREDIENTS_PLAN_MATERIAL_EXPIRATION_DATE", "有效日期", "yyyy-MM-dd", "yMd", true),

    /**
     * 领料接收
     */
    PICKING_RECEIVING_BATCH_EXPIRATION_DATE("PICKING_RECEIVING_BATCH_EXPIRATION_DATE", "有效日期", "yyyy-MM-dd", "yMd", false),

    /**
     * 数采时间由于非固定格式
     * 配置还来源于工艺配置
     * 不在此处理
     */
//    EQUIPMENT_DATA_ACQUISITION_TIME("EQUIPMENT_DATA_ACQUISITION_TIME", "数采时间", "yyyy-MM-dd HH:mm:ss", "yMdHms", false),

    /**
     * 生产投料
     */
    FEED_RECYCLE_FEEDING_DETAILS_OPERATION_TIME("FEED_RECYCLE_FEEDING_DETAILS_OPERATION_TIME", "操作时间", "yyyy-MM-dd " +
            "HH:mm:ss", "yMdHms", false),

    /**
     * 成品产出
     */
    PRODUCT_OUTPUT_DETAILS_OPERATE_TIME("PRODUCT_OUTPUT_DETAILS_OPERATE_TIME", "操作时间", "yyyy-MM-dd HH:mm:ss", "yMdHms", false),

    /**
     * 配料投入
     */
    INGREDIENTS_INPUT_FEEDING_DETAILS_FEEDING_TIME("INGREDIENTS_INPUT_FEEDING_DETAILS_FEEDING_TIME", "投料时间", "yyyy-MM" +
            "-dd HH:mm:ss", "yMdHms", false),

    /**
     * 配料称量
     */
    WEIGHING_INGREDIENTS_DETAILS_WEIGHING_TIME("WEIGHING_INGREDIENTS_DETAILS_WEIGHING_TIME", "称量时间", "yyyy-MM-dd " +
            "HH:mm:ss", "yMdHms", false),

    /**
     * 中间品产出
     */
    OUTPUT_WEIGHING_DETAILS_WEIGHING_TIME("OUTPUT_WEIGHING_DETAILS_WEIGHING_TIME", "称量时间", "yyyy-MM-dd HH:mm:ss",
            "yMdHms", false),

    /**
     * 清场信息
     */
    CLEAN_INFO_START_TIME("CLEAN_INFO_START_TIME", "清场开始时间", "yyyy-MM-dd HH:mm", "yMdHm", false),
    CLEAN_INFO_END_TIME("CLEAN_INFO_END_TIME", "清场结束时间", "yyyy-MM-dd HH:mm", "yMdHm", false),
    CLEAN_INFO_CLEAN_DATE("CLEAN_INFO_CLEAN_DATE", "清场日期", "yyyy-MM-dd", "yMd", false),
    CLEAN_INFO_EXPIRATION_DATE("CLEAN_INFO_EXPIRATION_DATE", "有效期至", "yyyy-MM-dd HH:mm", "yMdHm", false),
    CLEAN_INFO_ROOM_QUALITY_INSPECTION_DATE("CLEAN_INFO_ROOM_QUALITY_INSPECTION_DATE", "质检日期", "yyyy-MM-dd HH:mm:ss", "yMdHms", false),

    /**
     * 清场执行
     */
    CLEAN_IMPLEMENT_START_TIME("CLEAN_IMPLEMENT_START_TIME", "清场开始时间", "yyyy-MM-dd HH:mm", "yMdHm", false),
    CLEAN_IMPLEMENT_END_TIME("CLEAN_IMPLEMENT_END_TIME", "清场结束时间", "yyyy-MM-dd HH:mm", "yMdHm", false),
    CLEAN_IMPLEMENT_CLEAN_DATE("CLEAN_IMPLEMENT_CLEAN_DATE", "清场日期", "yyyy-MM-dd", "yMd", false),
    CLEAN_IMPLEMENT_EXPIRATION_DATE("CLEAN_IMPLEMENT_EXPIRATION_DATE", "有效期至", "yyyy-MM-dd HH:mm", "yMdHm", false),
    CLEAN_IMPLEMENT_ROOM_QUALITY_INSPECTION_DATE("CLEAN_IMPLEMENT_ROOM_QUALITY_INSPECTION_DATE", "质检日期", "yyyy-MM-dd HH:mm:ss"
            , "yMdHms", false),
    // *************************** 清场检查组件 ********************************
    CLEAN_CHECK_START_TIME("CLEAN_CHECK_START_TIME", "清场开始时间", "yyyy-MM-dd HH:mm", "yMdHm", false),
    CLEAN_CHECK_END_TIME("CLEAN_CHECK_END_TIME", "清场结束时间", "yyyy-MM-dd HH:mm", "yMdHm", false),
    CLEAN_CHECK_CLEAN_DATE("CLEAN_CHECK_CLEAN_DATE", "清场日期", "yyyy-MM-dd", "yMd", false),
    CLEAN_CHECK_EXPIRATION_DATE("CLEAN_CHECK_EXPIRATION_DATE", "有效期至", "yyyy-MM-dd HH:mm", "yMdHm", false),
    CLEAN_CHECK_ROOM_QUALITY_INSPECTION_DATE("CLEAN_CHECK_ROOM_QUALITY_INSPECTION_DATE", "质检日期", "yyyy-MM-dd HH:mm:ss", "yMdHms", false),

    /**
     * 物料预定
     */
    MATERIAL_RESERVE_BATCH_EXPIRATION_DATE("MATERIAL_RESERVE_BATCH_EXPIRATION_DATE", "有效期至", "yyyy-MM-dd", "yMd", false),
    MATERIAL_RESERVE_EXPIRATION_DATE("MATERIAL_RESERVE_EXPIRATION_DATE","有效期至","yyyy-MM-dd", "yMd", true),

    /**
     * 物料投入
     */
    MATERIAL_INPUT_DETAILS_FEEDING_TIME("MATERIAL_INPUT_DETAILS_FEEDING_TIME", "投料时间", "yyyy-MM-dd HH:mm:ss", "yMdHms", false),

    /**
     * 配液计划
     */
    LIQUID_PREPARATION_PLAN_BATCH_EXPIRY_DATE("LIQUID_PREPARATION_PLAN_BATCH_EXPIRY_DATE", "有效期至", "yyyy-MM-dd", "yMd", false),

    LIQUID_PREPARATION_PLAN_SUMMARY_EXPIRY_DATE("LIQUID_PREPARATION_PLAN_SUMMARY_EXPIRY_DATE", "有效期至", "yyyy-MM-dd", "yMd", true),

    /**
     * 配液量取
     */
    LIQUID_PREPARATION_MEASURE_DETAIL_OPERATION_TIME("LIQUID_PREPARATION_MEASURE_DETAIL_OPERATION_TIME", "操作时间",
            "yyyy-MM-dd HH:mm:ss", "yMdHms", false),

    /**
     * 配液投入
     */
    LIQUID_PREPARATION_INPUT_DETAIL_INPUT_TIME("LIQUID_PREPARATION_INPUT_DETAIL_INPUT_TIME", "操作时间", "yyyy-MM-dd " +
            "HH:mm:ss", "yMdHms", false),


    /**
     * 配液产出
     */
    LIQUID_PREPARATION_OUTPUT_DETAILS_OUTPUT_TIME("LIQUID_PREPARATION_OUTPUT_DETAILS_OUTPUT_TIME", "操作时间", "yyyy-MM" +
            "-dd HH:mm:ss", "yMdHms", false),
    /**
     * 称量数据-称量时间
     */
    WEIGHING_DATA_DETAIL_TIME("WEIGHING_DATA_DETAIL_TIME", "称量时间", "yyyy-MM-dd HH:mm:ss", "yMdHms", false),

    /**
     * 请验结果-请验时间
     */
    INSPECT_VERIFY_DATE("INSPECT_VERIFY_DATE", "请验时间", "yyyy-MM-dd HH:mm:ss", "yMdHms", false),
    ;

    @EnumValue
    private final String value;

    private final String name;

    private final String defaultPattern;

    /**
     * 参数格式
     * 例如: yMdHms表示年月日时分秒并从平台参数配置platform.sys.time-format的json中根据该属性获取格式
     */
    private final String patternProperty;

    /**
     * 是否是多行数据
     */
    private final boolean multiLine;


    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static BusinessTimeComponentTypeEnum getEnumByValue(String value) {
        return Arrays.stream(BusinessTimeComponentTypeEnum.values())
                .filter(statusEnum -> statusEnum.getValue().equals(value))
                .findFirst()
                .orElse(null);
    }

}
