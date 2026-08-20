package com.bmos.lims2.server.inspect.retention.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @Description: 留样接收台账实体类
 * @Author: yigaohui
 * @Date: 2026/02/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("lm_retention_receive_ledger")
public class RetentionReceiveLedger extends BaseDO {

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
     * 样品数量
     */
    private String quantity;

    /**
     * 单位ID
     */
    private Long unitId;

    /**
     * 取样人ID
     */
    private String samplerId;

    /**
     * 取样人名称
     */
    private String samplerName;

    /**
     * 取样时间
     */
    private LocalDateTime samplingTime;

    /**
     * 接收人ID
     */
    private String receiverId;

    /**
     * 接收人名称
     */
    private String receiverName;

    /**
     * 接收时间
     */
    private LocalDateTime receiveTime;

    /**
     * 储存位置
     */
    private String storageLocation;
}
