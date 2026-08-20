package com.bmos.lims2.server.inspect.parameter.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.common.util.id.IdUtils;
import com.bmos.lims2.server.inspect.parameter.dto.InspectParameterOptionDTO;
import com.bmos.lims2.server.inspect.parameter.entity.InspectParameterOption;
import com.bmos.lims2.server.inspect.parameter.mapper.InspectParameterOptionMapper;
import com.bmos.lims2.server.inspect.parameter.service.InspectParameterDataPointOptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 分析项数据点选项Service实现类
 */
@Service
@Slf4j
public class InspectParameterDataPointOptionServiceImpl implements InspectParameterDataPointOptionService {

    @Autowired
    private InspectParameterOptionMapper optionMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOptions(Long dataPointId, List<InspectParameterOptionDTO> options) {
        // 先删除原有选项
        optionMapper.deleteByDataPointId(dataPointId);

        // 保存新选项
        if (options != null && !options.isEmpty()) {
            List<InspectParameterOption> optionList = BeanUtil.copyToList(options, InspectParameterOption.class);
            optionList.forEach(option -> {
                option.setDataPointId(dataPointId);
                option.setId(IdUtils.getSnowflake());
            });
            optionMapper.insertBatch(optionList);
        }
    }

    @Override
    public void deleteByDataPointId(Long dataPointId) {
        optionMapper.deleteByDataPointId(dataPointId);
    }

    @Override
    public List<InspectParameterOptionDTO> getOptionsByDataPointId(Long dataPointId) {
        return BeanUtil.copyToList(optionMapper.selectByDataPointId(dataPointId), InspectParameterOptionDTO.class);
    }
} 