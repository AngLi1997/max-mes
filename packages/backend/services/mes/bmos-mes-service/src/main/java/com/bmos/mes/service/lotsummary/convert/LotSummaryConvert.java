package com.bmos.mes.service.lotsummary.convert;

import com.bmos.mes.service.lotsummary.dto.LotSummaryItemDTO;
import com.bmos.mes.service.lotsummary.model.LotSummary;
import com.bmos.mes.service.lotsummary.model.LotSummaryItem;
import com.bmos.mes.service.lotsummary.vo.LotSummaryDetailItemVO;
import com.bmos.mes.service.lotsummary.vo.LotSummaryDetailVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/9/5 14:24
 */
@Mapper
public interface LotSummaryConvert {

    LotSummaryConvert INSTANCE = Mappers.getMapper(LotSummaryConvert.class);

    List<LotSummaryItem> convertTODO(List<LotSummaryItemDTO> list);

    LotSummaryDetailVO convertTODetailVO(LotSummary lotSummary);

    default LotSummaryDetailVO convertDetailVO(LotSummary lotSummary, List<LotSummaryDetailItemVO> list){
        LotSummaryDetailVO result = convertTODetailVO(lotSummary);
        result.setList(list);
        return result;
    }
}
