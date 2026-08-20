package com.bmos.lims2.server.inspect.scheme.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmos.common.exception.BmosException;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeDataPointDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeDataPointBindingUpdateDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeDataPointBatchUpdateDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeDataPointSaveDTO;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionSchemeDataPoint;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeDataPointMapper;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeJudgmentMapper;
import com.bmos.lims2.server.inspect.scheme.service.InspectionSchemeDataPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 检验方案数据点配置Service实现类
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Service
public class InspectionSchemeDataPointServiceImpl extends ServiceImpl<InspectionSchemeDataPointMapper, InspectionSchemeDataPoint> implements InspectionSchemeDataPointService {

    @Autowired
    private InspectionSchemeDataPointMapper inspectionSchemeDataPointMapper;

    @Autowired
    private InspectionSchemeJudgmentMapper inspectionSchemeJudgmentMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveInspectionSchemeDataPoints(Long schemeId, Long versionId, Long packageId,Long parameterConfigId, List<InspectionSchemeDataPointSaveDTO> saveDTOList) {
        // 验证数据点名称在同一分析项配置中的唯一性
        Set<String> nameSet = new HashSet<>();
        for (InspectionSchemeDataPointSaveDTO saveDTO : saveDTOList) {
            if (nameSet.contains(saveDTO.getName())) {
                throw new BmosException(LimsResponseCode.DATA_POINT_NAME_DUPLICATE);
            }
            nameSet.add(saveDTO.getName());
        }

        // 删除原有配置
        LambdaQueryWrapper<InspectionSchemeDataPoint> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InspectionSchemeDataPoint::getParameterConfigId, parameterConfigId);
        inspectionSchemeDataPointMapper.delete(wrapper);

        // 保存新配置
        saveDTOList.forEach(saveDTO -> {
            InspectionSchemeDataPoint dataPoint = BeanUtil.copyProperties(saveDTO, InspectionSchemeDataPoint.class);
            dataPoint.setParameterConfigId(parameterConfigId);
            dataPoint.setSchemeId(schemeId);
            dataPoint.setVersionId(versionId);
            dataPoint.setPackageId(packageId);
            dataPoint.setInspectItemId(saveDTO.getInspectItemId());
            dataPoint.setParameterId(saveDTO.getInspectParameterId());
            inspectionSchemeDataPointMapper.insert(dataPoint);
        });
    }

    @Override
    public List<InspectionSchemeDataPointDTO> listInspectionSchemeDataPoints(Long parameterConfigId) {
        // 查询数据点配置列表（包含判定信息）

        return inspectionSchemeDataPointMapper.listByParameterConfigId(parameterConfigId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteInspectionSchemeDataPoints(Long parameterConfigId) {
        // 删除数据点配置
        LambdaQueryWrapper<InspectionSchemeDataPoint> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InspectionSchemeDataPoint::getParameterConfigId, parameterConfigId);
        inspectionSchemeDataPointMapper.delete(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateInspectionSchemeDataPoints(List<InspectionSchemeDataPointBatchUpdateDTO> batchUpdateDTO) {
        // 获取当前分析项配置的所有数据点
        List<Long> parameterConfigIds = batchUpdateDTO.stream().map(InspectionSchemeDataPointBatchUpdateDTO::getParameterConfigId).collect(Collectors.toList());
        List<InspectionSchemeDataPoint> existingDataPoints = list(
            new LambdaQueryWrapper<InspectionSchemeDataPoint>()
                .in(InspectionSchemeDataPoint::getParameterConfigId, parameterConfigIds)
        );

        // 记录前端提交的数据点ID（用于判断哪些数据点被删除了）
        Set<Long> submittedDataPointIds = new HashSet<>();

        // 验证数据点名称在同一分析项配置中的唯一性
        Set<String> nameSet = new HashSet<>();
        for (InspectionSchemeDataPointBatchUpdateDTO dataPointConfig : batchUpdateDTO) {
            if (dataPointConfig.getName() != null && nameSet.contains(dataPointConfig.getName())) {
                throw new BmosException(LimsResponseCode.DATA_POINT_NAME_DUPLICATE, dataPointConfig.getName());
            }
            if (dataPointConfig.getName() != null) {
                nameSet.add(dataPointConfig.getName());
            }
        }

        // 处理前端提交的每个数据点
        for (InspectionSchemeDataPointBatchUpdateDTO dataPointConfig : batchUpdateDTO) {
            if (dataPointConfig.getDataPointConfigId() == null) {
                // 新增数据点（没有dataPointConfigId）
                addDataPoint(dataPointConfig);
            } else {
                // 更新现有数据点
                updateDataPoint(dataPointConfig);
                submittedDataPointIds.add(dataPointConfig.getDataPointConfigId());
            }
        }

        // 删除前端没有提交的现有数据点（前端删除的数据点不会传到后端）
        for (InspectionSchemeDataPoint existingDataPoint : existingDataPoints) {
            if (!submittedDataPointIds.contains(existingDataPoint.getId())) {
                // 删除数据点及其相关的判断条件
                deleteDataPointAndRelatedData(existingDataPoint.getId());
            }
        }
    }

    /**
     * 新增数据点
     */
    private void addDataPoint(InspectionSchemeDataPointBatchUpdateDTO dto) {
        InspectionSchemeDataPoint dataPoint = BeanUtil.copyProperties(dto, InspectionSchemeDataPoint.class);
        save(dataPoint);
    }

    /**
     * 更新数据点
     */
    private void updateDataPoint(InspectionSchemeDataPointBatchUpdateDTO dataPointConfig) {
        if (dataPointConfig.getDataPointConfigId() == null) {
            throw new BmosException(LimsResponseCode.INVALID_PARAM, "更新操作必须提供数据点配置ID");
        }

        InspectionSchemeDataPoint dataPoint = getById(dataPointConfig.getDataPointConfigId());
        if (dataPoint == null) {
            throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS, "数据点配置不存在");
        }

        // 更新数据点信息
        if (dataPointConfig.getDataPointId() != null) {
            dataPoint.setDataPointId(dataPointConfig.getDataPointId());
        }
        BeanUtil.copyProperties(dataPointConfig, dataPoint);
        dataPoint.setId(dataPointConfig.getDataPointConfigId());
        updateById(dataPoint);
    }

    /**
     * 删除数据点及其相关数据
     */
    private void deleteDataPointAndRelatedData(Long dataPointConfigId) {
        // 删除相关的判断条件
//        inspectionSchemeJudgmentMapper.deleteByDataPointConfigId(dataPointConfigId);
        // 删除数据点配置
        removeById(dataPointConfigId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveDataPointBindings(List<InspectionSchemeDataPointBindingUpdateDTO> bindings) {
        if (bindings == null || bindings.isEmpty()) {
            return;
        }
        for (InspectionSchemeDataPointBindingUpdateDTO b : bindings) {
            if (b.getDataPointConfigId() == null) {
                throw new BmosException(LimsResponseCode.INVALID_PARAM, "dataPointConfigId不能为空");
            }
            InspectionSchemeDataPoint entity = getById(b.getDataPointConfigId());
            if (entity == null) {
                throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS, "数据点配置不存在: " + b.getDataPointConfigId());
            }
            entity.setRecordId(b.getRecordId());
            entity.setRecordVersionId(b.getRecordVersionId());
            entity.setComponentId(b.getComponentId());
            entity.setRecordItemId(b.getRecordItemId());
            entity.setFieldId(b.getFieldId());
            updateById(entity);
        }
    }
} 