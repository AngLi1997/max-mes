package com.bmos.lims2.server.stability.plan.dto.response;

import com.bmos.lims2.common.enums.StabilityPlanSampleStatusEnum;
import com.bmos.lims2.common.enums.StabilityTimepointTaskStatusEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 稳定性考察计划详情页矩阵DTO
 *
 * 矩阵结构：按试验类型分组，每组内行=批次，列=时间点
 */
@Data
public class StabilityInspectPlanMatrixDTO {

    /**
     * 试验类型分组列表（每组对应方案中的一个检验计划）
     */
    private List<ExperimentGroupDTO> experimentGroups;

    /**
     * 试验类型分组（对应一个 lm_stability_scheme_plan）
     */
    @Data
    public static class ExperimentGroupDTO {
        /**
         * 方案检验计划ID
         */
        private Long schemePlanId;

        /**
         * 试验类型
         */
        private String experimentType;

        /**
         * 试验类型中文名称
         */
        private String experimentTypeName;

        /**
         * 储存条件
         */
        private String storageCondition;

        /**
         * 时间点列头（来自方案，按sort排序）
         */
        private List<TimepointHeaderDTO> timepoints;

        /**
         * 每批次一行
         */
        private List<BatchRowDTO> batchRows;
    }

    /**
     * 时间点列头信息
     */
    @Data
    public static class TimepointHeaderDTO {
        /**
         * 方案时间点ID
         */
        private Long schemeTimepointId;

        /**
         * 时间点数值
         */
        private Integer timeValue;

        /**
         * 时间单位（DAY/MONTH/YEAR）
         */
        private String timeUnit;
    }

    /**
     * 批次行数据
     */
    @Data
    public static class BatchRowDTO {
        /**
         * 批次ID
         */
        private Long batchId;

        /**
         * 批号
         */
        private String batchNo;

        /**
         * 批次生产日期
         */
        private LocalDate productionDate;

        /**
         * 样品接收日期
         */
        private LocalDate sampleReceiveDate;

        /**
         * 样品ID（lm_stability_plan_sample.id，该批次×该试验类型的样品）
         */
        private Long sampleId;

        /**
         * 样品状态
         */
        private StabilityPlanSampleStatusEnum sampleStatus;

        /**
         * 计划取样量
         */
        private String plannedSampleAmount;

        /**
         * 该批次在各时间点的任务（与 timepoints 列头一一对应，未生成时 status=NOT_STARTED）
         */
        private List<TimepointTaskDTO> tasks;
    }

    /**
     * 时间点任务单元格
     */
    @Data
    public static class TimepointTaskDTO {
        /**
         * 任务ID（null 表示任务尚未生成）
         */
        private Long taskId;

        /**
         * 对应的方案时间点ID
         */
        private Long schemeTimepointId;

        /**
         * 计划检验日期
         */
        private LocalDate plannedDate;

        /**
         * 任务状态
         */
        private StabilityTimepointTaskStatusEnum status;

        /**
         * 关联检验单ID
         */
        private Long inspectionOrderId;

        /**
         * 实际完成日期
         */
        private LocalDate completedDate;

        /**
         * 0月候选检验单列表（仅当 timeValue=0 且同批号存在多条常规检验单时返回；
         * 前端根据此列表是否有值及 inspectionOrderId 是否已选，自行决定是否展示选择框）
         */
        private List<ZeroMonthCandidateDTO> candidateOrders;
    }

    /**
     * 0月候选检验单简要信息
     */
    @Data
    public static class ZeroMonthCandidateDTO {
        /** 检验单ID */
        private Long id;
        /** 检验单号 */
        private String orderNo;
        /** 批号 */
        private String batchNo;
        /** 检验单状态 */
        private com.bmos.lims2.common.enums.InspectionOrderStatusEnum orderStatus;
        /** 是否已完成 */
        private Boolean finished;
        /** 请验人（格式：姓名-登录名） */
        private String requestUserName;
        /** 请验时间 */
        private java.time.LocalDateTime requestTime;
        /** 检验方案（格式：方案名称-版本号，无方案时为空） */
        private String schemeInfo;
    }
}
