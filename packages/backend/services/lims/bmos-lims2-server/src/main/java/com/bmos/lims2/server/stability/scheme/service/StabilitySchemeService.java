package com.bmos.lims2.server.stability.scheme.service;

import com.bmos.lims2.server.stability.scheme.dto.request.StabilitySchemeCopyFromVersionDTO;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilitySchemeQueryDTO;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilitySchemeSaveFusionDTO;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilitySchemeSaveDTO;
import com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeBasicSaveRespDTO;
import com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeDTO;
import com.bmos.mybatis.page.CommonPage;

import java.util.List;

/**
 * 稳定性方案Service接口
 *
 * @author makejava
 * @since 2025-03-17 10:00:00
 */
public interface StabilitySchemeService {

    /**
     * 分页查询稳定性方案列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    CommonPage<StabilitySchemeDTO> pageStabilityScheme(StabilitySchemeQueryDTO queryDTO);

    /**
     * 根据ID查询稳定性方案
     *
     * @param id 方案ID
     * @return 方案信息
     */
    StabilitySchemeDTO getStabilityScheme(Long id);

    /**
     * 保存稳定性方案基础信息（生成方案）
     *
     * @param saveDTO 保存请求
     * @return 保存结果
     */
    StabilitySchemeBasicSaveRespDTO saveStabilityScheme(StabilitySchemeSaveDTO saveDTO);

    /**
     * 删除稳定性方案（逻辑删除）
     *
     * @param id 方案ID
     */
    void deleteStabilityScheme(Long id);

    /**
     * 根据检品ID查询方案
     *
     * @param materialId 检品ID
     * @return 方案信息
     */
    StabilitySchemeDTO getByMaterialId(Long materialId);

    /**
     * 查询所有稳定性方案列表
     *
     * @return 方案列表
     */
    List<StabilitySchemeDTO> listAll();

    /**
     * 全量暂存稳定性方案（基础信息+分析项，不校验必填）
     *
     * @param fusionDTO 暂存请求
     * @return 保存结果
     */
    StabilitySchemeBasicSaveRespDTO saveStabilitySchemeFusion(StabilitySchemeSaveFusionDTO fusionDTO);

    /**
     * 保存数据权限（部门）
     *
     * @param schemeId 方案ID
     * @param deptIds  部门ID列表
     */
    void savePermissions(Long schemeId, List<Long> deptIds);

    /**
     * 查询数据权限（部门ID列表）
     *
     * @param schemeId 方案ID
     * @return 部门ID列表
     */
    List<Long> getPermissions(Long schemeId);

    /**
     * 校验方案版本内所有判定条件引用的数据点配置是否仍然有效，
     * 包括：数据点未被删除、类型未变更、ELN执行方式下绑定完整。
     * 存在异常时抛出 BmosException。
     *
     * @param versionId 方案版本ID
     */
    void validateJudgmentConfigConsistency(Long versionId);

    /**
     * 新增稳定性方案（从现有方案版本复制），返回方案与版本信息
     *
     * @param dto 复制请求
     * @return 新方案与版本信息
     */
    StabilitySchemeBasicSaveRespDTO copySchemeFromVersion(StabilitySchemeCopyFromVersionDTO dto);
}
