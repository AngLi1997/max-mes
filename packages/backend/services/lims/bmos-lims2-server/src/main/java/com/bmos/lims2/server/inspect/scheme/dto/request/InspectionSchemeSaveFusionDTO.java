package com.bmos.lims2.server.inspect.scheme.dto.request;

import lombok.Data;

import java.util.List;

/**
 * @Description: 检验方案保存融合请求DTO（基础信息+项目配置更新+取样配置更新）
 * @Author: yigaohui
 * @Date: 2025/10/24 10:00
 */
@Data
public class InspectionSchemeSaveFusionDTO {

    /** 基础信息保存DTO，对应 save-basic */
    private InspectionSchemeBasicSaveDTO basic;

    /** 项目配置更新DTO列表，对应 update-items */
    private List<InspectionSchemeItemUpdateDTO> itemUpdates;

    /** 取样配置更新DTO列表，对应 update-samplings */
    private List<InspectionSchemeSamplingUpdateDTO> samplingUpdates;
}


