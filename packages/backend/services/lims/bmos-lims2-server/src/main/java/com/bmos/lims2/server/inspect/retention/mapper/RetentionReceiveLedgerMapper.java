package com.bmos.lims2.server.inspect.retention.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bmos.lims2.server.inspect.retention.dto.RetentionReceiveLedgerListDTO;
import com.bmos.lims2.server.inspect.retention.dto.RetentionReceiveLedgerPageQueryDTO;
import com.bmos.lims2.server.inspect.retention.entity.RetentionReceiveLedger;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description: 留样接收台账Mapper
 * @Author: yigaohui
 * @Date: 2026/02/10
 */
@Mapper
public interface RetentionReceiveLedgerMapper extends BaseMapper<RetentionReceiveLedger> {

    /**
     * 分页查询留样接收台账列表
     * @param queryDTO 查询条件
     * @return 台账列表
     */
    List<RetentionReceiveLedgerListDTO> selectReceiveLedgerPageList(RetentionReceiveLedgerPageQueryDTO queryDTO);
}
