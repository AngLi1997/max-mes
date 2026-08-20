package com.bmos.lims2.server.inspect.receive.service;

import com.bmos.mybatis.page.CommonPage;
import com.bmos.lims2.server.inspect.receive.dto.RetentionReceivePageQueryDTO;
import com.bmos.lims2.server.inspect.sample.dto.SampleCollectionListDTO;

import java.util.List;

/**
 * @Description: 样品接收Service接口
 * @Author: yigaohui
 * @Date: 2025/01/29 16:30
 */
public interface SampleReceiveService {


    /**
     * 批量接收不同检验单的样品
     * @param sampleIds 样品ID列表
     */
    void batchReceiveSamples(List<Long> sampleIds);

    /**
     * 批量查询多个请验单的班组人员交集（用于领样人下拉）
     * 说明：每个请验单关联的班组下的人员取交集，仅保留所有请验单共同可领样的人员
     * @param orderIds 请验单ID集合
     * @return 人员ID列表（交集，去重后按首次出现顺序返回）
     */
    List<String> listReceiversByInspectionOrders(List<Long> orderIds);

    /**
     * 批量查询多个样品对应的班组人员交集（用于领样人下拉）
     * 样品 -> 检验单 -> 方案版本 + 样品绑定的检验项目 -> 班组 -> 人员
     * @param sampleIds 样品ID集合
     * @return 人员ID列表（交集，去重后按首次出现顺序返回）
     */
    List<String> listReceiversBySamples(List<Long> sampleIds);

    /**
     * 分页查询留样接收列表
     * @param queryDTO 查询条件
     * @return 留样接收列表分页数据
     */
    CommonPage<SampleCollectionListDTO> getRetentionReceivePageList(RetentionReceivePageQueryDTO queryDTO);

    /**
     * 批量接收留样样品
     * @param sampleIds 样品ID列表
     * @param storageLocation 储存位置
     */
    void batchReceiveRetentionSamples(List<Long> sampleIds, String storageLocation);

    /**
     * 扫描留样样品二维码获取样品信息
     * @param sampleNo 样品编号
     * @return 留样样品信息
     */
    SampleCollectionListDTO scanRetentionSample(String sampleNo);
}
