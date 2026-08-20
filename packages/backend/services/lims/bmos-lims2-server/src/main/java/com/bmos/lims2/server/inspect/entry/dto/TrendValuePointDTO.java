package com.bmos.lims2.server.inspect.entry.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Description: 趋势查询-数值点DTO
 * @Author: yigaohui
 * @Date: 2025/09/05 11:20
 */
public class TrendValuePointDTO {

    private Long inspectionOrderId;
    private String inspectionOrderNo;
    private String batchNo;
    private LocalDateTime requestTime;
    private Long schemeId;
    private Long schemeVersionId;
    private Long inspectItemId;
    private Long parameterId;
    private String dataPointName;
    private BigDecimal valueNumber;

    public Long getInspectionOrderId() {
        return inspectionOrderId;
    }

    public void setInspectionOrderId(Long inspectionOrderId) {
        this.inspectionOrderId = inspectionOrderId;
    }

    public String getInspectionOrderNo() {
        return inspectionOrderNo;
    }

    public void setInspectionOrderNo(String inspectionOrderNo) {
        this.inspectionOrderNo = inspectionOrderNo;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(LocalDateTime requestTime) {
        this.requestTime = requestTime;
    }

    public Long getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(Long schemeId) {
        this.schemeId = schemeId;
    }

    public Long getSchemeVersionId() {
        return schemeVersionId;
    }

    public void setSchemeVersionId(Long schemeVersionId) {
        this.schemeVersionId = schemeVersionId;
    }

    public Long getInspectItemId() {
        return inspectItemId;
    }

    public void setInspectItemId(Long inspectItemId) {
        this.inspectItemId = inspectItemId;
    }

    public Long getParameterId() {
        return parameterId;
    }

    public void setParameterId(Long parameterId) {
        this.parameterId = parameterId;
    }

    public String getDataPointName() {
        return dataPointName;
    }

    public void setDataPointName(String dataPointName) {
        this.dataPointName = dataPointName;
    }

    public BigDecimal getValueNumber() {
        return valueNumber;
    }

    public void setValueNumber(BigDecimal valueNumber) {
        this.valueNumber = valueNumber;
    }
}


