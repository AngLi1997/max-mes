package com.bmos.lims2.server.inspect.sample.ledger.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.bmos.lims2.server.inspect.sample.ledger.enums.SampleLedgerOperationTypeEnum;

import java.time.LocalDateTime;

/**
 * @Description: 样品台账实体
 * @Author: yigaohui
 * @Date: 2025/09/05 10:00
 */
@TableName("lm_sample_ledger")
public class SampleLedger extends BaseDO {
    private Long inspectionOrderId;
    /** 检品ID快照 */
    private Long materialId;
    private Long sampleId;
    /** 检品编码快照 */
    private String materialCode;
    /** 样品编号快照 */
    private String sampleNo;
    /** 样品数量快照（字符串存储） */
    private String quantity;
    /** 回收量快照（字符串存储） */
    private String recycleQuantity;
    /** 消耗量快照（字符串存储） */
    private String consumedQuantity;
    /** 样品单位ID快照 */
    private Long unitId;
    /** 样品状态（0-未取样，1-取样，2-接收，3-分样，4-领取，5-回收，6-处理） */
    private Integer status;
    private SampleLedgerOperationTypeEnum operationType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operationTime;
    private String operatorId;
    private String operatorName;
    private String remark;

    public Long getInspectionOrderId() {
        return inspectionOrderId;
    }

    public void setInspectionOrderId(Long inspectionOrderId) {
        this.inspectionOrderId = inspectionOrderId;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public Long getSampleId() {
        return sampleId;
    }

    public void setSampleId(Long sampleId) {
        this.sampleId = sampleId;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getSampleNo() {
        return sampleNo;
    }

    public void setSampleNo(String sampleNo) {
        this.sampleNo = sampleNo;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getRecycleQuantity() {
        return recycleQuantity;
    }

    public void setRecycleQuantity(String recycleQuantity) {
        this.recycleQuantity = recycleQuantity;
    }

    public String getConsumedQuantity() {
        return consumedQuantity;
    }

    public void setConsumedQuantity(String consumedQuantity) {
        this.consumedQuantity = consumedQuantity;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public SampleLedgerOperationTypeEnum getOperationType() {
        return operationType;
    }

    public void setOperationType(SampleLedgerOperationTypeEnum operationType) {
        this.operationType = operationType;
    }

    public LocalDateTime getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(LocalDateTime operationTime) {
        this.operationTime = operationTime;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}


