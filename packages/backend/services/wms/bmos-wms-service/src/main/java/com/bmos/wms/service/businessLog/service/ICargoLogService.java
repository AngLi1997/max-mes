package com.bmos.wms.service.businessLog.service;

import com.bmos.mybatis.page.CommonPage;
import com.bmos.wms.common.enums.inventory.CargoInventoryOperateLogType;
import com.bmos.wms.service.inventory.model.Inventory;
import com.bmos.wms.service.businessLog.dto.CargoLogPageQuery;
import com.bmos.wms.service.businessLog.vo.CargoLogVO;
import com.bmos.wms.service.sendout.model.SendOutOrder;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/7 18:20
 */
public interface ICargoLogService {

    CommonPage<CargoLogVO> queryPage(CargoLogPageQuery pageQuery);

    /**
     * 保存货品日志
     *
     * @param logType     货品日志类型
     * @param inventory   货品件
     * @param quantity    关键数量
     * @param operatorIds 操作人id
     * @param order       领料工单信息
     * @param remark      备注
     */
    void saveCargoLog(CargoInventoryOperateLogType logType,
                      Inventory inventory, BigDecimal quantity,
                      String[] operatorIds,
                      SendOutOrder order,
                      String remark);

    /**
     * 保存货品日志
     *
     * @param logType     货品日志类型
     * @param inventories 货品件列表
     * @param quantities  关键数量列表
     * @param operatorIds 操作人id
     * @param order       领料工单信息
     * @param remark      备注
     */
    void saveCargoLog(CargoInventoryOperateLogType logType,
                      List<Inventory> inventories,
                      List<BigDecimal> quantities,
                      String[] operatorIds,
                      SendOutOrder order,
                      String remark);
}
