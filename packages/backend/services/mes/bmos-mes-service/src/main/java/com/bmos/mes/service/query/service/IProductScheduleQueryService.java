package com.bmos.mes.service.query.service;

import com.bmos.mes.service.query.dto.ProcedureInProductionDTO;

import java.util.List;

/**
 * 生产进度查询服务
 *
 * @className: IProductScheduleQueryService
 * @author: yigaohui
 * @date: 2024/12/4 18:16
 * @Version: 1.0
 * @description:
 */

public interface IProductScheduleQueryService {

    /**
     * 查询正在生产中的工序信息
     *
     * @return 查询结果
     */
    List<ProcedureInProductionDTO> inProduction();
}
