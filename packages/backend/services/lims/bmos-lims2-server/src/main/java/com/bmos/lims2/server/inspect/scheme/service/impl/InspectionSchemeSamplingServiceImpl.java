package com.bmos.lims2.server.inspect.scheme.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeSamplingDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeSamplingSaveDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeSamplingUpdateDTO;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionSchemeSampling;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeSamplingMapper;
import com.bmos.lims2.server.inspect.scheme.service.InspectionSchemeSamplingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description: 检验方案取样配置Service实现类
 * @Author: yigaohui
 * @Date: 2025/01/21 10:30
 */
@Service
public class InspectionSchemeSamplingServiceImpl implements InspectionSchemeSamplingService {

    @Autowired
    private InspectionSchemeSamplingMapper inspectionSchemeSamplingMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveInspectionSchemeSamplings(Long schemeId, Long versionId, List<InspectionSchemeSamplingSaveDTO> saveDTOList) {
        // 删除原有的取样配置
        LambdaQueryWrapper<InspectionSchemeSampling> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InspectionSchemeSampling::getVersionId, versionId);
        inspectionSchemeSamplingMapper.delete(wrapper);

        if (saveDTOList == null || saveDTOList.isEmpty()) {
            return;
        }

        // 保存取样配置
        for (InspectionSchemeSamplingSaveDTO saveDTO : saveDTOList) {
            InspectionSchemeSampling schemeSampling = new InspectionSchemeSampling();
            // 手动复制属性
            schemeSampling.setSchemeId(schemeId);
            schemeSampling.setVersionId(versionId);
            schemeSampling.setInspectItemId(saveDTO.getInspectItemId());
            schemeSampling.setSamplingAmount(saveDTO.getSamplingAmount());
            schemeSampling.setSamplingUnit(saveDTO.getSamplingUnit());
            schemeSampling.setSamplingCount(saveDTO.getSamplingCount());
            // schemeSampling.setRemark(saveDTO.getRemark()); // 暂时注释，等DTO定义完善

            inspectionSchemeSamplingMapper.insert(schemeSampling);
        }
    }

    @Override
    public List<InspectionSchemeSamplingDTO> listInspectionSchemeSamplings(Long schemeId, Long versionId) {
        return inspectionSchemeSamplingMapper.selectBySchemeVersion(schemeId, versionId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteInspectionSchemeSamplings(Long schemeId, Long versionId) {
        LambdaQueryWrapper<InspectionSchemeSampling> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InspectionSchemeSampling::getSchemeId, schemeId)
                .eq(InspectionSchemeSampling::getVersionId, versionId);
        inspectionSchemeSamplingMapper.delete(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateInspectionSchemeSamplings(List<InspectionSchemeSamplingUpdateDTO> updateDTO) {
        if (updateDTO == null || updateDTO.isEmpty()) {
            return;
        }

        List<Long> schemeVersionIds = updateDTO.stream().map(InspectionSchemeSamplingUpdateDTO::getVersionId).collect(Collectors.toList());

        // 获取现有的取样配置
        LambdaQueryWrapper<InspectionSchemeSampling> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(InspectionSchemeSampling::getVersionId, schemeVersionIds);
        List<InspectionSchemeSampling> existingSamplings = inspectionSchemeSamplingMapper.selectList(queryWrapper);

        // 创建现有配置的ID映射
        Map<Long, InspectionSchemeSampling> existingSamplingMap = existingSamplings.stream()
                .collect(Collectors.toMap(InspectionSchemeSampling::getId, sampling -> sampling));


        List<Long> samplingConfigIds = updateDTO.stream().map(InspectionSchemeSamplingUpdateDTO::getSamplingConfigId).collect(Collectors.toList());

        // 删除前端没有传入的取样配置（表示被删除）
        for (Long existingSamplingId : existingSamplingMap.keySet()) {
            if (!samplingConfigIds.contains(existingSamplingId)) {
                inspectionSchemeSamplingMapper.deleteById(existingSamplingId);
            }
        }

        for (InspectionSchemeSamplingUpdateDTO samplingConfig : updateDTO) {
            if (samplingConfig.getSamplingConfigId() != null) {
                // 更新现有取样配置
                InspectionSchemeSampling existingSampling = existingSamplingMap.get(samplingConfig.getSamplingConfigId());
                if (existingSampling != null) {
                    updateExistingSampling(existingSampling, samplingConfig);
                }
            } else {
                // 新增取样配置
                addNewSampling(samplingConfig);
            }
        }
    }

    /**
     * 更新现有取样配置
     */
    private void updateExistingSampling(InspectionSchemeSampling existingSampling,
                                        InspectionSchemeSamplingUpdateDTO samplingConfig) {
        existingSampling.setInspectItemId(samplingConfig.getInspectItemId());
        existingSampling.setSamplingAmount(samplingConfig.getSamplingAmount());
        existingSampling.setSamplingUnit(samplingConfig.getSamplingUnit());
        existingSampling.setSamplingCount(samplingConfig.getSamplingCount());
        inspectionSchemeSamplingMapper.updateById(existingSampling);
    }

    /**
     * 新增取样配置
     */
    private void addNewSampling(InspectionSchemeSamplingUpdateDTO samplingConfig) {
        InspectionSchemeSampling newSampling = new InspectionSchemeSampling();
        newSampling.setSchemeId(samplingConfig.getSchemeId());
        newSampling.setVersionId(samplingConfig.getVersionId());
        newSampling.setInspectItemId(samplingConfig.getInspectItemId());
        newSampling.setSamplingAmount(samplingConfig.getSamplingAmount());
        newSampling.setSamplingUnit(samplingConfig.getSamplingUnit());
        newSampling.setSamplingCount(samplingConfig.getSamplingCount());
        inspectionSchemeSamplingMapper.insert(newSampling);
    }
}