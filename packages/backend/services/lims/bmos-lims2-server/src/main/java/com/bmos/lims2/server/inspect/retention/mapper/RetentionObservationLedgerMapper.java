package com.bmos.lims2.server.inspect.retention.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bmos.lims2.server.inspect.retention.dto.RetentionObservationLedgerListDTO;
import com.bmos.lims2.server.inspect.retention.dto.RetentionObservationLedgerPageQueryDTO;
import com.bmos.lims2.server.inspect.retention.entity.RetentionObservationLedger;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description: 留样观察台账Mapper
 * @Author: yigaohui
 * @Date: 2026/02/06
 */
@Mapper
public interface RetentionObservationLedgerMapper extends BaseMapper<RetentionObservationLedger> {

    /**
     * 分页查询留样观察台账列表
     * @param queryDTO 查询条件
     * @return 台账列表
     */
    List<RetentionObservationLedgerListDTO> selectObservationLedgerPageList(RetentionObservationLedgerPageQueryDTO queryDTO);
}
