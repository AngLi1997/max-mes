package com.bmos.lims2.server.inspect.scheme.dto.response;

/**
 * @Description: 方案版本下拉项
 * @Author: yigaohui
 * @Date: 2025/09/05 11:56
 */
public class SchemeVersionOptionDTO {
    private Long id;
    private String versionNo;
    private String status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getVersionNo() { return versionNo; }
    public void setVersionNo(String versionNo) { this.versionNo = versionNo; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}


