package com.bmos.lims2.server.inspect.scheme.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeParameterDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeParameterSaveDTO;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionSchemeItemTeams;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionSchemeParameter;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeItemTeamMapper;
import com.bmos.lims2.server.inspect.scheme.service.InspectionSchemeItemTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 检验方案分析项配置Service实现类
 * 注意：使用Parameter命名以保持与InspectParameter一致
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Service
public class InspectionSchemeItemTeamServiceImpl implements InspectionSchemeItemTeamService {

    @Autowired
    private InspectionSchemeItemTeamMapper inspectionSchemeItemTeamMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveInspectionSchemeParameters(Long schemeId, Long versionId,Long packageId, Long itemConfigId,Long inspectItemId, List<Long> saveDTOList) {
        // 删除原有配置
        LambdaQueryWrapper<InspectionSchemeItemTeams> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InspectionSchemeItemTeams::getItemConfigId, itemConfigId);
        inspectionSchemeItemTeamMapper.delete(wrapper);

        // 保存新配置
        saveDTOList.forEach(teamId -> {
            InspectionSchemeItemTeams schemeItemTeams = new InspectionSchemeItemTeams();
            schemeItemTeams.setItemConfigId(itemConfigId);
            schemeItemTeams.setSchemeId(schemeId);
            schemeItemTeams.setVersionId(versionId);
            schemeItemTeams.setPackageId(packageId);
            schemeItemTeams.setInspectItemId(inspectItemId);
            schemeItemTeams.setTeamId(teamId);
            inspectionSchemeItemTeamMapper.insert(schemeItemTeams);
        });
    }

    @Override
    public List<Long> listByItemConfigId(Long id) {
        LambdaQueryWrapper<InspectionSchemeItemTeams> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        objectLambdaQueryWrapper.eq(InspectionSchemeItemTeams::getItemConfigId, id);
        objectLambdaQueryWrapper.eq(InspectionSchemeItemTeams::getDeleted, false);
        return inspectionSchemeItemTeamMapper.selectList(objectLambdaQueryWrapper).stream().map(InspectionSchemeItemTeams::getTeamId).collect(Collectors.toList());
    }
}