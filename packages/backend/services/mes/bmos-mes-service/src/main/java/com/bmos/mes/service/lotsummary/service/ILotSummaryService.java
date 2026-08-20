package com.bmos.mes.service.lotsummary.service;

import com.bmos.mes.service.lotsummary.dto.LotSummaryCreateDTO;
import com.bmos.mes.service.lotsummary.dto.LotSummaryEditDTO;
import com.bmos.mes.service.lotsummary.dto.LotSummaryPageQuery;
import com.bmos.mes.service.lotsummary.dto.LotSummaryProductDataPageQuery;
import com.bmos.mes.service.lotsummary.vo.LotSummaryDetailVO;
import com.bmos.mes.service.lotsummary.vo.LotSummaryPageVO;
import com.bmos.mes.service.lotsummary.vo.LotSummaryProductData;
import com.bmos.mes.service.lotsummary.vo.LotSummaryProductListData;
import com.bmos.mybatis.page.CommonPage;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/9/5 14:05
 */
public interface ILotSummaryService {
    void createLotSummary(LotSummaryCreateDTO dto);

    CommonPage<LotSummaryPageVO> queryPage(LotSummaryPageQuery pageQuery);

    LotSummaryDetailVO queryDetail(Long id);

    void editLotSummary(LotSummaryEditDTO dto);

    void deleteLotSummary(Long id);

    LotSummaryProductData queryProductDataPage(LotSummaryProductDataPageQuery pageQuery);

    void exportProductDataPage(HttpServletResponse response, LotSummaryProductDataPageQuery pageQuery) throws IOException;

    LotSummaryProductListData queryProductDataList(LotSummaryProductDataPageQuery pageQuery);
}
