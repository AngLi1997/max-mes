package com.bmos.mes.service.record.service;

import com.bmos.mes.service.record.dto.RecordListQueryDTO;
import com.bmos.mes.service.record.vo.RecordListVO;
import com.bmos.mybatis.page.CommonPage;

public interface BatchRecordManageService {

    Boolean deleteFormula(Long componentId);

    /**
     * 获取记录分页-无数据权限限制
     * @param dto
     * @return
     */
    CommonPage<RecordListVO> getRecordPageWithNoPermission(RecordListQueryDTO dto);
}
