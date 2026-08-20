package com.bmos.lims2.server.inspect.retention.service;

import com.bmos.lims2.server.inspect.retention.dto.*;
import com.bmos.mybatis.page.CommonPage;

import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 留样台账Service接口
 * @Author: yigaohui
 * @Date: 2026/02/10
 */
public interface RetentionLedgerService {

    /**
     * 分页查询留样接收台账列表
     * @param queryDTO 查询条件
     * @return 台账列表分页数据
     */
    CommonPage<RetentionReceiveLedgerListDTO> getReceiveLedgerPageList(RetentionReceiveLedgerPageQueryDTO queryDTO);

    /**
     * 分页查询留样销毁台账列表
     * @param queryDTO 查询条件
     * @return 台账列表分页数据
     */
    CommonPage<RetentionDestructionLedgerListDTO> getDestructionLedgerPageList(RetentionDestructionLedgerPageQueryDTO queryDTO);

    /**
     * 分页查询留样领用台账列表
     * @param queryDTO 查询条件
     * @return 台账列表分页数据
     */
    CommonPage<SampleCollectionLedgerListDTO> getCollectionLedgerPageList(SampleCollectionLedgerPageQueryDTO queryDTO);

    /**
     * 分页查询留样观察台账列表
     * @param queryDTO 查询条件
     * @return 台账列表分页数据
     */
    CommonPage<RetentionObservationLedgerListDTO> getObservationLedgerPageList(RetentionObservationLedgerPageQueryDTO queryDTO);

    /**
     * 导出留样接收台账
     * @param queryDTO 查询条件
     * @param response HTTP响应
     */
    void exportReceiveLedger(RetentionReceiveLedgerPageQueryDTO queryDTO, HttpServletResponse response);

    /**
     * 导出留样销毁台账
     * @param queryDTO 查询条件
     * @param response HTTP响应
     */
    void exportDestructionLedger(RetentionDestructionLedgerPageQueryDTO queryDTO, HttpServletResponse response);

    /**
     * 导出留样领用台账
     * @param queryDTO 查询条件
     * @param response HTTP响应
     */
    void exportCollectionLedger(SampleCollectionLedgerPageQueryDTO queryDTO, HttpServletResponse response);

    /**
     * 导出留样观察台账
     * @param queryDTO 查询条件
     * @param response HTTP响应
     */
    void exportObservationLedger(RetentionObservationLedgerPageQueryDTO queryDTO, HttpServletResponse response);
}
