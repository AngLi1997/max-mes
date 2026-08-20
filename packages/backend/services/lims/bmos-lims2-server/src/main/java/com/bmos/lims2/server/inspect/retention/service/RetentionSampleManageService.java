package com.bmos.lims2.server.inspect.retention.service;

import com.bmos.lims2.server.audit.operationlog.vo.ListLogVO;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.lims2.server.inspect.retention.dto.RetentionSampleManageListDTO;
import com.bmos.lims2.server.inspect.retention.dto.RetentionSampleManagePageQueryDTO;
import com.bmos.lims2.server.inspect.retention.dto.RetentionSamplePrintTagResultDTO;
import com.bmos.lims2.server.inspect.retention.dto.SampleDestructionDTO;

import java.time.LocalDate;
import java.util.List;

/**
 * @Description: 留样样品管理Service接口
 * @Author: yigaohui
 * @Date: 2026/02/09
 */
public interface RetentionSampleManageService {

    /**
     * 分页查询留样样品管理列表
     * @param queryDTO 查询条件
     * @return 留样样品管理列表分页数据
     */
    CommonPage<RetentionSampleManageListDTO> getManagePageList(RetentionSampleManagePageQueryDTO queryDTO);

    /**
     * 延期操作
     * @param sampleId 样品ID
     * @param newExpiryDate 新的留样期限
     */
    void extendRetention(Long sampleId, LocalDate newExpiryDate);

    /**
     * 领用操作
     * @param sampleId 样品ID
     * @param collectQuantity 领用数量
     * @param unitId 单位ID
     * @param collectReason 领用原因
     * @param collectorId 领样人ID
     * @param collectorName 领样人名称
     */
    void collectSample(Long sampleId, String collectQuantity, Long unitId,
                      String collectReason, String collectorId, String collectorName);

    /**
     * 查询样品操作历史
     * @param sampleId 样品ID
     * @return 操作历史列表
     */
    List<ListLogVO> getOperationHistory(Long sampleId);

    /**
     * 销毁样品（单个）
     * @param destructionDTO 销毁信息
     */
    void destroySample(SampleDestructionDTO destructionDTO);

    /**
     * 批量销毁样品
     * @param sampleIds 样品ID列表
     * @param destructionDTO 销毁信息（不含样品ID）
     */
    void batchDestroySamples(List<Long> sampleIds, SampleDestructionDTO destructionDTO);

    /**
     * 查询留样样品打印标签信息
     * @param sampleNo 样品编号
     * @return 留样样品打印标签信息
     */
    RetentionSamplePrintTagResultDTO getRetentionSamplePrintTag(String sampleNo);
}
