package com.bmos.lims2.server.inspect.retention.mapper;

import com.bmos.lims2.server.inspect.retention.dto.SampleCollectionLedgerListDTO;
import com.bmos.lims2.server.inspect.retention.dto.SampleCollectionLedgerPageQueryDTO;
import com.bmos.lims2.server.inspect.retention.entity.SampleCollectionLedger;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 留样样品领用台账Mapper
 *
 * @author yigaohui
 * @since 2026/02/09
 */
@Mapper
public interface SampleCollectionLedgerMapper extends BaseMapperX<SampleCollectionLedger> {

    /**
     * 分页查询留样领用台账列表
     * @param queryDTO 查询条件
     * @return 台账列表
     */
    List<SampleCollectionLedgerListDTO> selectCollectionLedgerPageList(SampleCollectionLedgerPageQueryDTO queryDTO);
}
