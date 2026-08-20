package com.bmos.mes.service.storage.log.service;

import com.bmos.mes.service.storage.log.dto.StorageMaterialPositionLogDTO;
import com.bmos.mes.service.storage.log.dto.StorageMaterialPositionLogPageQuery;
import com.bmos.mes.service.storage.log.vo.StorageMaterialPositionLogVO;
import com.bmos.mybatis.page.CommonPage;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/2/21 09:31
 */
public interface IStorageMaterialPositionLogService {

    /**
     * 保存日志
     *
     * @param dto
     */
    void saveLog(StorageMaterialPositionLogDTO dto);

    /**
     * 批量保存日志
     *
     * @param list
     */
    void saveLogs(List<StorageMaterialPositionLogDTO> list);

    /**
     * 分页查询日志
     *
     * @param pageQuery
     * @return
     */
    CommonPage<StorageMaterialPositionLogVO> queryPage(StorageMaterialPositionLogPageQuery pageQuery);

}
