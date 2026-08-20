package com.bmos.lims2.server.inspect.retention.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bmos.lims2.server.inspect.retention.dto.RetentionDestructionLedgerListDTO;
import com.bmos.lims2.server.inspect.retention.dto.RetentionDestructionLedgerPageQueryDTO;
import com.bmos.lims2.server.inspect.retention.entity.RetentionDestructionLedger;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description: 留样销毁台账Mapper
 * @Author: yigaohui
 * @Date: 2026/02/10
 */
@Mapper
public interface RetentionDestructionLedgerMapper extends BaseMapper<RetentionDestructionLedger> {

    /**
     * 分页查询留样销毁台账列表
     * @param queryDTO 查询条件
     * @return 台账列表
     */
    List<RetentionDestructionLedgerListDTO> selectDestructionLedgerPageList(RetentionDestructionLedgerPageQueryDTO queryDTO);
}
