package com.bmos.lims2.server.inspect.parameter.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.common.util.id.IdUtils;
import com.bmos.lims2.server.inspect.parameter.dto.InspectParameterDataPointTrendDTO;
import com.bmos.lims2.server.inspect.parameter.entity.InspectParameterTrend;
import com.bmos.lims2.server.inspect.parameter.mapper.InspectParameterTrendMapper;
import com.bmos.lims2.server.inspect.parameter.service.InspectParameterDataPointTrendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 分析项趋势线配置Service实现类
 */
@Service
@Slf4j
public class InspectParameterDataPointTrendServiceImpl implements InspectParameterDataPointTrendService {

    @Autowired
    private InspectParameterTrendMapper trendMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTrends(Long dataPointId, List<InspectParameterDataPointTrendDTO> trends) {
        // 先删除原有配置
        trendMapper.deleteByParameterId(dataPointId);

        // 保存新配置
        if (trends != null && !trends.isEmpty()) {
            List<InspectParameterTrend> trendList = BeanUtil.copyToList(trends, InspectParameterTrend.class);
            trendList.forEach(trend -> {
                trend.setDataPointId(dataPointId);
                trend.setId(IdUtils.getSnowflake());
            });
            trendMapper.insertBatch(trendList);
        }
    }


    @Override
    public void deleteByDataPointId(Long dataPointId) {
        trendMapper.deleteByParameterId(dataPointId);
    }

    @Override
    public List<InspectParameterDataPointTrendDTO> getTrendsByDataPointId(Long dataPointId) {
        return BeanUtil.copyToList(trendMapper.selectByDataPointId(dataPointId), InspectParameterDataPointTrendDTO.class);
    }
} 