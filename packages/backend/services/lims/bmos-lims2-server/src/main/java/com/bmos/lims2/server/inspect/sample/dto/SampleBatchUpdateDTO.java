package com.bmos.lims2.server.inspect.sample.dto;

import lombok.Data;

import java.util.List;

/**
 * 样品批量更新DTO
 *
 * @author yigaohui
 * @since 2025/01/29 18:00
 */
@Data
public class SampleBatchUpdateDTO {

    /**
     * 请验单ID
     */
    private Long inspectionOrderId;

    /**
     * 样品信息列表
     */
    private List<SampleInfoDTO> samples;

    /**
     * 样品信息DTO
     */
    @Data
    public static class SampleInfoDTO {

        /**
         * 样品ID（新增时为空，后端根据此字段判断是新增还是更新）
         */
        private Long sampleId;

        /**
         * 样品编号（新增时可为空，系统自动生成）
         */
        private String sampleNo;

        /**
         * 检验项目ID（可选，为空表示整体取样）
         */
        private Long inspectionItemId;

        /**
         * 检验项目名称
         */
        private String inspectionItemName;

        /**
         * 计划取样量（字符串，整数≤6位，小数≤5位）
         */
        @javax.validation.constraints.Pattern(regexp = "^-?\\d{1,6}(\\.\\d{1,5})?$", message = "计划取样量最多整数6位，小数5位")
        private String planQuantity;

        /**
         * 实际取样量（字符串，整数≤6位，小数≤5位）
         */
        @javax.validation.constraints.Pattern(regexp = "^-?\\d{1,6}(\\.\\d{1,5})?$", message = "实际取样量最多整数6位，小数5位")
        private String actualQuantity;

        /**
         * 取样单位
         */
        private Long unitId;

        /**
         * 备注
         */
        private String remark;
    }
}
