package com.bmos.lims2.server.inspect.sample.ledger.mapper;

import com.bmos.lims2.server.inspect.sample.ledger.entity.SampleLedger;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.lims2.server.inspect.sample.ledger.dto.SampleLedgerListDTO;
import com.bmos.lims2.server.inspect.sample.ledger.dto.SampleLedgerPageQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 样品台账Mapper
 * @Author: yigaohui
 * @Date: 2025/09/05 10:00
 */
@Mapper
public interface SampleLedgerMapper extends BaseMapperX<SampleLedger> {

    void batchInsert(@Param("list") List<SampleLedger> list);

    List<SampleLedger> listByOrderId(@Param("orderId") Long orderId);

    List<SampleLedger> listBySampleIds(@Param("sampleIds") List<Long> sampleIds);

    List<SampleLedgerListDTO> selectLedgerPage(@Param("query") SampleLedgerPageQueryDTO queryDTO);

    /**
     * 不分页：按检验单ID查询台账联表列表
     */
    List<SampleLedgerListDTO> selectLedgerListByOrderId(@Param("orderId") Long orderId);

    /**
     * 查询指定样品最近一条台账（按操作时间/ID倒序）
     */
    SampleLedger selectLastBySampleId(@Param("sampleId") Long sampleId);

    /**
     * 按ID更新消耗量
     */
    int updateConsumedQuantityById(@Param("id") Long id, @Param("consumedQuantity") String consumedQuantity);
}


