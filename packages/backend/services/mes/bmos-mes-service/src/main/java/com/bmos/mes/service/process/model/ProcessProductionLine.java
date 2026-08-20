package com.bmos.mes.service.process.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 工艺-产线关联实体
 */
@Getter
@Setter
@ToString
@TableName("bm_process_production_line")
public class ProcessProductionLine {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 工艺id
     */
    private Long processId;

    /**
     * 工艺版本
     */
    private String processVersion;

    /**
     * 工艺版本id
     */
    private Long processVersionId;

    /**
     * 产线id
     */
    private Long productionLineId;
}
