package com.bmos.mes.service.lotrelease.manage.service;

import com.bmos.mes.service.dataset.handle.data.AssembleCompleteData;
import com.bmos.mes.service.lotrelease.manage.dto.*;
import com.bmos.mes.service.lotrelease.manage.model.LotRelease;
import com.bmos.mes.service.lotrelease.manage.vo.*;
import com.bmos.mes.service.product.vo.ProductCategoryTreeNodeVO;
import com.bmos.mybatis.page.CommonPage;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/8/27 16:44
 */
public interface ILotReleaseService {

    List<ProductCategoryTreeNodeVO> getLotReleaseProductTreeByTemplateId(Integer categoryType, Long templateId);

    CommonPage<LotReleasePlanVO> queryPlanPage(LotReleasePlanPageQuery pageQuery);

    CommonPage<LotReleasePageVO> queryPage(LotReleasePageQuery pageQuery);

    String generate(LotReleaseGenerateDTO dto);

    CommonPage<LotReleaseVersionPageVO> queryVersionPage(LotReleaseVersionPageQuery pageQuery);

    List<LotReleaseGeneratePreviewVO> getGeneratePreviewList(Long processId);

    String uploadExcel(MultipartFile file) throws Exception;

    void updateExcelFile(LotReleaseUpdateExcelFileDTO dto);

    void scrap(Long id);

    void submit(Long id);

    CommonPage<LotReleaseAuditPageVO> queryAuditPage(LotReleaseAuditPageQuery pageQuery);

    void auditCallback(Long id, Boolean pass, String comment, String auditorId);

    void downloadExcel(HttpServletResponse response, Long id) throws Exception;

    void downloadByUrl(HttpServletResponse response, String url) throws Exception;

    Map<String, String> renderTemplate(AssembleCompleteData assembleCompleteData);

    List<LotReleaseDynamicReportItemVO> getDynamicReportItem(LotReleaseQueryDynamicReportDTO dto);

    List<LogReleaseHistoryVO> showHistory(Long id);

    /**
     * 查询批签发代办数量所需要的条件
     * @param deptIdList
     * @return
     */
    List<String> selectAuditBusinessKey(List<Long> deptIdList);

    LotRelease selectOneById(Long businessId);
}
