package com.bmos.mes.service.dataset.handle.data;

import com.bmos.mes.common.enums.dataset.GenerateSourceEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 批记录/批签发所需数据
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PlanBatchDocumentData {

    /**
     * 需要生成批记录/批签发的批次id
     */
    private Long planId;

    /**
     * 需要渲染的模板版本id
     */
    private List<RenderTemplateData> renderTemplateDataList;

    /**
     * 需要生成批记录/批签发的批次id关联的所有批次信息
     */
    private List<PlanLoadingData> planLoadingData;

    /**
     * 生成哪一种文档
     * 批签发 / 批记录
     */
    private GenerateSourceEnum sourceEnum;

    /**
     * 动态渲染的数据
     */
    private List<DynamicRenderingData> dynamicRenderingData;

    /**
     * planId的排序列表
     */
    private List<Long> sortPlanIdList;


}
