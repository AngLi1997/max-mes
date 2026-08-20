package com.bmos.lims2.server.stability.sample.service;

import com.bmos.lims2.server.stability.sample.dto.request.StabilitySampleDestroyDTO;
import com.bmos.lims2.server.stability.sample.dto.request.StabilitySampleManagementQueryDTO;
import com.bmos.lims2.server.stability.sample.dto.request.StabilitySampleMoveDTO;
import com.bmos.lims2.server.stability.sample.dto.response.StabilitySampleGroupDTO;
import com.bmos.lims2.server.stability.sample.dto.response.StabilitySamplePrintTagResultDTO;
import com.bmos.lims2.server.audit.operationlog.vo.ListLogVO;
import com.bmos.mybatis.page.CommonPage;

import java.util.List;

/**
 * 稳定性样品管理服务接口
 */
public interface StabilitySampleManagementService {

    /**
     * 分页查询稳定性样品（按计划分组）
     */
    CommonPage<StabilitySampleGroupDTO> pageStabilitySamples(StabilitySampleManagementQueryDTO queryDTO);

    /**
     * 移动样品储存位置
     */
    void moveSampleLocation(StabilitySampleMoveDTO dto);

    /**
     * 批量销毁样品
     */
    void batchDestroySamples(List<Long> ids, StabilitySampleDestroyDTO dto);
    /**
     * 根据样品编号查询打印标签信息
     */
    StabilitySamplePrintTagResultDTO getPrintTagResult(String sampleNo);

    /**
     * 根据时间点子样品编号查询周期任务取样打印标签信息
     */
    StabilitySamplePrintTagResultDTO getTimepointPrintTagResult(String sampleNo);
}
