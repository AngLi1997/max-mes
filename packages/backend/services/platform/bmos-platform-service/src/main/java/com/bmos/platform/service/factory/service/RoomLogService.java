package com.bmos.platform.service.factory.service;

import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.facade.factory.dto.BatchRoomCleanPageDTO;
import com.bmos.platform.facade.factory.vo.BatchRoomCleanInfoVO;
import com.bmos.platform.facade.factory.vo.RoomCleanInfoFeignVO;
import com.bmos.platform.service.factory.controller.vo.RoomLogPageVO;
import com.bmos.platform.service.factory.service.dto.RoomLogPageDTO;
import com.bmos.platform.service.factory.service.dto.RoomStatusLogExportDTO;

/**
 * 房间清洁日志
 */
public interface RoomLogService {

    /**
     * 房间状态清洁日志分页查询
     * @param dto
     * @return
     */
    CommonPage<RoomLogPageVO> cleanLogPage(RoomLogPageDTO dto);

    /**
     * 导出房间状态清洁日志
     * @param dto
     */
    void exportLog(RoomStatusLogExportDTO dto);

    /**
     * 根据生产批号查询房间清场信息分页
     * @param dto
     * @return
     */
    CommonPage<BatchRoomCleanInfoVO> getRoomCleanInfoPage(BatchRoomCleanPageDTO dto);
}
