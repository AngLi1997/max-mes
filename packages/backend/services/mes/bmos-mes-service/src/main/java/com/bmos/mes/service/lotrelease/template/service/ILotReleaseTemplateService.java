package com.bmos.mes.service.lotrelease.template.service;

import com.bmos.mes.service.lotrelease.template.dto.*;
import com.bmos.mes.service.lotrelease.template.vo.*;
import com.bmos.mybatis.page.CommonPage;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/8/26 18:45
 */
public interface ILotReleaseTemplateService {

    CommonPage<LotReleaseTemplatePageVO> queryPage(LotReleaseTemplatePageQuery pageQuery);

    CommonPage<LotReleaseTemplateVersionPageVO> queryVersionPage(LotReleaseTemplateVersionPageQuery pageQuery);


    void bindProcess(LotReleaseTemplateBindProcessDTO dto);

    String uploadTemplate(MultipartFile file) throws Exception;

    void downloadTemplate(HttpServletResponse response, Long id) throws Exception;

    void makeSure(Long id);

    void makeDefault(Long id);

    void scrap(Long id);

    List<LogReleaseTemplateVersionHistoryVO> showHistory(Long id);

    void createTemplate(LotReleaseTemplateCreateDTO dto);

    void createTemplateVersion(LotReleaseTemplateVersionCreateDTO dto);

    void updateTemplateFile(LotReleaseTemplateEditDTO dto);

    List<LotReleaseTemplateLinkVO> listByProcessId(Long processId);

    List<Long> listProcessIdByTemplateId(Long templateId);

    List<LotReleaseTemplateVersionItemVO> listVersionByTemplateId(Long templateId);
}
