package com.bmos.lims2.server.stability.scheme.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.lims2.server.stability.scheme.entity.StabilitySchemeItemTeams;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemeItemTeamMapper;
import com.bmos.lims2.server.stability.scheme.service.StabilitySchemeItemTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 稳定性方案检验项目班组Service实现类
 */
@Service
public class StabilitySchemeItemTeamServiceImpl implements StabilitySchemeItemTeamService {

    @Autowired
    private StabilitySchemeItemTeamMapper itemTeamMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTeams(Long schemeId, Long versionId, Long itemConfigId, Long inspectItemId, List<Long> teamIds) {
        // 先删除原有班组配置
        LambdaQueryWrapper<StabilitySchemeItemTeams> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StabilitySchemeItemTeams::getItemConfigId, itemConfigId);
        itemTeamMapper.delete(wrapper);

        if (teamIds == null || teamIds.isEmpty()) {
            return;
        }

        // 插入新班组配置
        for (Long teamId : teamIds) {
            StabilitySchemeItemTeams record = new StabilitySchemeItemTeams();
            record.setSchemeId(schemeId);
            record.setVersionId(versionId);
            record.setItemConfigId(itemConfigId);
            record.setInspectItemId(inspectItemId);
            record.setTeamId(teamId);
            itemTeamMapper.insert(record);
        }
    }

    @Override
    public List<Long> listByItemConfigId(Long itemConfigId) {
        LambdaQueryWrapper<StabilitySchemeItemTeams> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StabilitySchemeItemTeams::getItemConfigId, itemConfigId)
               .eq(StabilitySchemeItemTeams::getDeleted, false);
        return itemTeamMapper.selectList(wrapper).stream()
                .map(StabilitySchemeItemTeams::getTeamId)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByItemConfigId(Long itemConfigId) {
        LambdaQueryWrapper<StabilitySchemeItemTeams> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StabilitySchemeItemTeams::getItemConfigId, itemConfigId);
        itemTeamMapper.delete(wrapper);
    }
}
