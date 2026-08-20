package com.bmos.lims2.server.inspect.retention.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @Description: 留样销毁台账实体类
 * @Author: yigaohui
 * @Date: 2026/02/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("lm_retention_destruction_ledger")
public class RetentionDestructionLedger extends BaseDO {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 样品ID
     */
    private Long sampleId;

    /**
     * 样品编号
     */
    private String sampleNo;

    /**
     * 批号
     */
    private String batchNo;

    /**
     * 物料ID
     */
    private Long materialId;

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 物料编码
     */
    private String materialCode;

    /**
     * 物料规格
     */
    private String materialSpec;

    /**
     * 销毁数量
     */
    private String quantity;

    /**
     * 单位ID
     */
    private Long unitId;

    /**
     * 销毁原因
     */
    private String destructionReason;

    /**
     * 销毁方式
     */
    private String destructionMethod;

    /**
     * 销毁地点
     */
    private String destructionLocation;

    /**
     * 销毁时间
     */
    private LocalDateTime destructionTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 销毁人ID
     */
    private String destructorId;

    /**
     * 销毁人名称
     */
    private String destructorName;

    /**
     * 监督人ID
     */
    private String supervisorId;

    /**
     * 监督人名称
     */
    private String supervisorName;
}
