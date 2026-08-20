package com.bmos.wms.service.businessLog.service;

import com.bmos.mybatis.page.CommonPage;
import com.bmos.wms.common.enums.inventory.PositionInventoryOperateLogType;
import com.bmos.wms.service.inventory.model.Inventory;
import com.bmos.wms.service.businessLog.dto.PositionLogPageQuery;
import com.bmos.wms.service.businessLog.vo.PositionLogVO;
import com.bmos.wms.service.sendout.model.SendOutOrder;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/7 18:20
 */
public interface IPositionLogService {

    CommonPage<PositionLogVO> queryPage(PositionLogPageQuery pageQuery);

    /**
     * 保存货位日志
     *
     * @param logType     货品日志类型
     * @param inventory   货品件
     * @param quantity    关键数量
     * @param operatorIds 操作人id
     * @param order       领料工单
     * @param remark      备注
     */
    void savePositionLog(PositionInventoryOperateLogType logType,
                         Inventory inventory, BigDecimal quantity,
                         String[] operatorIds,
                         SendOutOrder order,
                         String remark);

    /**
     * 保存货位日志
     *
     * @param logType     货品日志类型
     * @param inventories 货品件列表
     * @param quantities  关键数量列表
     * @param operatorIds 操作人id
     * @param order       领料工单
     * @param remark      备注
     */
    void savePositionLog(PositionInventoryOperateLogType logType,
                         List<Inventory> inventories,
                         List<BigDecimal> quantities,
                         String[] operatorIds,
                         SendOutOrder order,
                         String remark);
}
