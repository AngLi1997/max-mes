package com.bmos.lims2.server.inspect.sample.service;

import com.bmos.lims2.server.inspect.sample.dto.SampleScanResultDTO;
import com.bmos.lims2.server.inspect.sample.dto.*;
import com.bmos.mybatis.page.CommonPage;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description: 样品管理Service接口
 * @Author: yigaohui
 * @Date: 2025/01/29 10:30
 */
public interface SampleService {

    /**
     * 根据取样计划生成样品
     * @param inspectionOrderId 检验单ID
     * @return 生成的样品列表
     */
    List<SampleDTO> generateSamplesFromSampling(Long inspectionOrderId, List<Long> samplingIds);

    /**
     * 根据检验单ID查询样品列表
     * @param inspectionOrderId 检验单ID
     * @return 样品列表
     */
    List<SampleDTO> getSamplesByInspectionOrderId(Long inspectionOrderId);

    /**
     * 标记样品为已取样
     * @param sampleUpdateDTO 取样确认信息
     */
    void updateSampleInfo(SampleUpdateDTO sampleUpdateDTO);

    /**
     * 标记样品为已接收
     * @param sampleId 样品ID
     * @param receiveTime 接收时间
     */
    void markSampleAsReceived(Long sampleId, LocalDateTime receiveTime);


    /**
     * 批量更新样品信息（支持新增、更新、删除）
     * @param batchUpdateDTO 批量更新数据
     */
    void batchUpdateSamples(SampleBatchUpdateDTO batchUpdateDTO);

    /**
     * 新增样品
     * @param orderId
     * @param sampleAddDTOS 样品新增信息
     */
    List<SampleDTO> addSamples(Long orderId, List<SampleAddDTO> sampleAddDTOS);

    /**
     * 取样确认
     * @param sampleUpdateDTO 确认信息
     */
    void samplingConfirm(SampleUpdateDTO sampleUpdateDTO);

    /**
     * 标签打印确认（批量）
     * @param sampleUpdateDTO 确认信息
     */
    void confirmTagPrinted(SampleUpdateDTO sampleUpdateDTO);



    /**
     * 扫描样品二维码获取样品信息
     * @param sampleNo 样品编号
     * @return 样品扫码结果信息
     */
    SampleScanResultDTO scanSample(String sampleNo);


    /**
     * 删除未取样样品
     * 仅允许删除尚未进行实际取样（sampled = false）的样品
     * @param sampleId 样品ID
     */
    void deleteUnsampledSample(Long sampleId);


    /**
     * 扫描样品二维码获取样品信息
     * @param sampleNo 样品编号
     * @return 样品扫码结果信息
     */
    SamplePrintTagResultDTO samplePrintTag(String sampleNo);

    /**
     * 根据样品编号查询请验单下所有样品（修改后的扫码接口）
     * @param sampleNo 样品编号
     * @return 请验单下所有样品的详细信息
     */
    List<SampleScanResultDTO> getSamplesByOrderFromSampleNo(String sampleNo);

    /**
     * 分页查询可领取的样品列表
     * @param queryDTO 查询条件
     * @return 样品领取列表
     */
    CommonPage<SampleCollectionListDTO> getCollectionPageList(SampleCollectionPageQueryDTO queryDTO);

    /**
     * 批量领取样品
     * @param batchCollectionDTO 批量领取信息
     */
    void batchCollectSamples(SampleBatchCollectionDTO batchCollectionDTO);
}
