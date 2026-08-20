package com.bmos.lims2.server.eln.record.service;


import com.bmos.lims2.server.eln.record.dto.RecordListQueryDTO;
import com.bmos.lims2.server.eln.record.vo.RecordListVO;
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
