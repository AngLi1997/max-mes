package com.bmos.lims2.server.inspect.query.service;

import com.bmos.lims2.server.inspect.query.dto.TrendQueryDTO;
import com.bmos.lims2.server.inspect.query.dto.TrendValueSeriesDTO;

/**
 * @Description: 趋势查询服务
 * @Author: yigaohui
 * @Date: 2025/09/05 11:40
 */
public interface TrendQueryService {

    /**
     * 查询某数据点的数值趋势（跨版本同名视为同一数据点）
     */
    TrendValueSeriesDTO queryNumericTrend(TrendQueryDTO queryDTO);
}


