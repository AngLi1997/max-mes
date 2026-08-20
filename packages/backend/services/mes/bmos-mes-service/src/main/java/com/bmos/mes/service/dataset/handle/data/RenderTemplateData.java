package com.bmos.mes.service.dataset.handle.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RenderTemplateData {

    /**
     * 渲染的模板url
     */
    private String renderTemplateUrl;

    /**
     * 扩展字段 透传
     */
    private String extInfo;

}
