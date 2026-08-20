package com.bmos.lims2.server.stability.scheme.service;

import java.util.List;

/**
 * 稳定性方案检验项目班组Service接口
 */
public interface StabilitySchemeItemTeamService {

    /**
     * 保存检验项目的班组（先删后插）
     */
    void saveTeams(Long schemeId, Long versionId, Long itemConfigId, Long inspectItemId, List<Long> teamIds);

    /**
     * 根据检验项目配置ID查询班组ID列表
     */
    List<Long> listByItemConfigId(Long itemConfigId);

    /**
     * 根据检验项目配置ID删除班组
     */
    void deleteByItemConfigId(Long itemConfigId);
}
