package com.bmos.mes.service.process.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 工艺-批记录关联实体
 */
@Getter
@Setter
@ToString
@TableName("bm_process_batch_record")
public class ProcessBatchRecordRelation {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 工艺版本
     */
    private String processVersion;

    /**
     * 工艺版本id
     */
    private Long processVersionId;

    /**
     * 批记录id
     */
    private Long batchRecordId;

    /**
     * 批记录版本id
     */
    private Long batchRecordVersionId;

    /**
     * 批记录版本
     */
    private String batchRecordVersion;
}
