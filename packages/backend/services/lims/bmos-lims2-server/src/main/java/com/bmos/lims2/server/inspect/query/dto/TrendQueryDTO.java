package com.bmos.lims2.server.inspect.query.dto;

/**
 * @Description: 趋势查询-请求DTO
 * @Author: yigaohui
 * @Date: 2025/09/05 11:30
 */
public class TrendQueryDTO {

    private Long materialId;
    private Long schemeId;
    private Long inspectItemId;
    private Long parameterId;
    private String dataPointName;
    private java.time.LocalDateTime requestStartTime;
    private java.time.LocalDateTime requestEndTime;

    public Long getMaterialId() { return materialId; }
    public void setMaterialId(Long materialId) { this.materialId = materialId; }
    public Long getSchemeId() { return schemeId; }
    public void setSchemeId(Long schemeId) { this.schemeId = schemeId; }
    public Long getInspectItemId() { return inspectItemId; }
    public void setInspectItemId(Long inspectItemId) { this.inspectItemId = inspectItemId; }
    public Long getParameterId() { return parameterId; }
    public void setParameterId(Long parameterId) { this.parameterId = parameterId; }
    public String getDataPointName() { return dataPointName; }
    public void setDataPointName(String dataPointName) { this.dataPointName = dataPointName; }
    public java.time.LocalDateTime getRequestStartTime() { return requestStartTime; }
    public void setRequestStartTime(java.time.LocalDateTime requestStartTime) { this.requestStartTime = requestStartTime; }
    public java.time.LocalDateTime getRequestEndTime() { return requestEndTime; }
    public void setRequestEndTime(java.time.LocalDateTime requestEndTime) { this.requestEndTime = requestEndTime; }
}


