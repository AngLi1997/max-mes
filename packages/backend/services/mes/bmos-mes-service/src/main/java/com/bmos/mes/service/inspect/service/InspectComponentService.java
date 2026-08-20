package com.bmos.mes.service.inspect.service;

import com.bmos.mes.service.inspect.controller.vo.InspectPageVO;
import com.bmos.mes.service.inspect.service.dto.InspectComponentConfirmFillDTO;
import com.bmos.mes.service.inspect.service.dto.InspectResultPageQueryDTO;
import com.bmos.mybatis.page.CommonPage;

public interface InspectComponentService {


    /**
     * 查询非退回请验单分页
     * @param dto
     * @return
     */
    CommonPage<InspectPageVO> queryNotRejectInspectResultPage(InspectResultPageQueryDTO dto);

    /**
     * 确认回填请验单结果
     * @param dto
     * @return
     */
    void confirmFillFormData(InspectComponentConfirmFillDTO dto);
}
