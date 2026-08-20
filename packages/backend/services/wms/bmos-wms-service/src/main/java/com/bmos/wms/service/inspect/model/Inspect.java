package com.bmos.wms.service.inspect.model;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.wms.common.enums.inspect.InspectStatusEnum;
import com.bmos.wms.common.enums.inspect.MaterialQualityStatusEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * WMS 请验单（mirror MES bm_inspect，去掉生产相关字段）
 */
@TableName("bw_inspect")
@Getter
@Setter
public class Inspect extends BaseDO {

    /** LIMS 检验单号 */
    private String inspectNo;

    /** 请验状态 1-请验中 2-已完成 3-已退回 */
    private InspectStatusEnum status;

    /** 汇总检验结果（与 MES 同字段名 inspectResult） */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private MaterialQualityStatusEnum inspectResult;

    /** 退回原因 / 重新发起原因 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String reason;

    /** 请验人id */
    private String inspectorId;
    /** 请验人登录名 */
    private String inspector;
    /** 请验时间 */
    private LocalDateTime inspectTime;

    /** 请验单配置id（LIMS 请验单 / templateId） */
    private Long inspectConfigId;

    /** 检验方案id（自研 LIMS 路径） */
    private Long schemeId;
    /** 检验方案版本id（自研 LIMS 路径） */
    private Long schemeVersionId;

    // ---- WMS 特有字段 ----

    /** 货品id（bw_cargo.id） */
    private Long cargoId;
    /** 货品批次id（bw_inventory_batch.id），便于回写质量状态 */
    private Long batchId;
    /** 货品批号 = bw_inventory_batch.batch_no */
    private String materialBatchNo;
    /** 原厂批号 */
    private String factoryBatchNo;
    /** 货品名称（冗余，便于列表展示） */
    private String cargoName;
    /** 货品合并编码（冗余） */
    private String mergeCode;
    /** 单位id */
    private Long unitId;
}
