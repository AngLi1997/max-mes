package com.bmos.mes.service.trace.material.service;

import com.bmos.mes.service.trace.material.dto.MaterialTraceTemplateCreateDTO;
import com.bmos.mes.service.trace.material.dto.MaterialTraceTemplateEditDTO;
import com.bmos.mes.service.trace.material.dto.MaterialTraceTemplatePageQuery;
import com.bmos.mes.service.trace.material.vo.MaterialTraceTemplateDetailVO;
import com.bmos.mes.service.trace.material.vo.MaterialTraceTemplatePageVO;
import com.bmos.mybatis.page.CommonPage;

import javax.annotation.Nullable;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/11/19 18:20
 */
public interface IMaterialTraceTemplateService {

    void createTemplate(MaterialTraceTemplateCreateDTO dto);

    CommonPage<MaterialTraceTemplatePageVO> queryPage(MaterialTraceTemplatePageQuery pageQuery);

    @Nullable
    MaterialTraceTemplateDetailVO queryDetail(Long id);

    void enableTemplate(Long id);

    void disableTemplate(Long id);

    void editTemplate(MaterialTraceTemplateEditDTO dto);

    void deleteTemplate(Long id);
}
