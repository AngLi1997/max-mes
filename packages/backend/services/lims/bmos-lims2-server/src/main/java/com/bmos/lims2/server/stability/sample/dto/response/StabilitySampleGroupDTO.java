package com.bmos.lims2.server.stability.sample.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 稳定性样品管理分组DTO（以考察计划为分组单元）
 */
@Data
public class StabilitySampleGroupDTO {

    // ─── 计划级信息 ─────────────────────────────────────────────────
    private Long planId;
    private String planCode;
    private Long materialId;
    private String materialName;
    private String materialCode;
    private String materialSpec;
    private String planCreateBy;
    private String planCreateByUserName;
    private LocalDateTime planCreateTime;
    private LocalDate startDate;
    private LocalDate planEndDate;

    // ─── 该计划下的样品行列表 ────────────────────────────────────────
    private List<StabilitySampleRowDTO> samples;
}
