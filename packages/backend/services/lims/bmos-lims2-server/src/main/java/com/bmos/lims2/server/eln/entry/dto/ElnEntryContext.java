package com.bmos.lims2.server.eln.entry.dto;

import com.bmos.lims2.server.eln.entry.vo.FormDataItemVO;
import com.bmos.lims2.server.inspect.order.entity.InspectionOrder;
import com.bmos.lims2.server.inspect.scheme.dto.response.InspectionSchemeVersionFullConfigDTO;
import com.bmos.lims2.server.material.entity.Material;
import com.bmos.unit.service.UnitCache;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
@Accessors(chain = true)
public class ElnEntryContext {

    /**
     * 生产计划
     */
    private InspectionOrder order;

    /**
     * 产品信息
     */
    private Material material;

    /**
     * 工艺信息
     */
    private InspectionSchemeVersionFullConfigDTO schemeVersionFullConfigDTO;

    /**
     * 基础信息
     */
    private BusinessComponentBatchSaveDTO dto;

    /**
     * 已有值组件id列表
     */
    private Collection<FormDataItemVO> formDataCollection;

    /**
     * 单位缓存
     */
    private UnitCache unitCache;


    private LocalDateTime acquisitionTime;

    /**
     * 签名url
     */
    private HandleSignInfo signInfo;

    /**
     * 结论结果：true=符合规定，false=不符合规定
     */
    private Boolean conclusionResult;

}
