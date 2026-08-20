package com.bmos.lims2.server.inspect.sample.service;

import com.bmos.lims2.server.inspect.sample.dto.*;
import com.bmos.mybatis.page.CommonPage;

/**
 * @Description: 样品处理服务接口
 * @Author: yigaohui
 * @Date: 2025/02/01 10:50
 */
public interface SampleHandleService {

    /**
     * 分页查询可进行样品处理的样品列表
     */
    CommonPage<SampleHandleListDTO> getHandlePageList(SampleHandlePageQueryDTO queryDTO);

    /**
     * 批量回收
     */
    void batchRecycle(SampleBatchRecycleDTO dto);

    /**
     * 批量处理
     */
    void batchProcess(SampleBatchProcessDTO dto);

    /**
     * 查看样品详情（含回收/处理状态）
     */
    SampleScanResultDTO getSampleDetail(Long sampleId);

    /**
     * 统计样品处理状态数量（待回收/待处理/已处理）
     * 复用分页查询的条件
     */
    SampleHandleStatusCountDTO getHandleStatusCount(SampleHandlePageQueryDTO queryDTO);
}


