package com.bmos.lims2.server.inspect.scheme.service;

import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeSaveDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeBasicSaveDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeItemUpdateDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeSamplingUpdateDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeQueryDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeCopyFromVersionDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeVersionSaveItemsDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeSaveFusionDTO;
import com.bmos.lims2.server.inspect.scheme.dto.response.InspectionSchemeDTO;
import com.bmos.lims2.server.inspect.scheme.dto.response.InspectionSchemeBasicSaveRespDTO;
import com.bmos.lims2.server.inspect.scheme.dto.response.InspectionSchemeDropdownDTO;
import com.bmos.mybatis.page.CommonPage;

import java.util.List;

/**
 * 检验方案Service接口
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
public interface InspectionSchemeService {

    /**
     * 保存检验方案
     *
     * @param saveDTO 保存参数
     * @return 检验方案ID
     */
    Long saveInspectionScheme(InspectionSchemeSaveDTO saveDTO);

    /**
     * 保存检验方案基础信息（分步骤保存第一步）
     *
     * @param saveDTO 基础信息保存参数
     * @return 方案和版本信息
     */
    InspectionSchemeBasicSaveRespDTO saveInspectionSchemeBasic(InspectionSchemeBasicSaveDTO saveDTO);

    /**
     * 融合保存：按顺序执行 save-basic -> update-items -> update-samplings，整体控制事务
     * @param fusionDTO 入参
     * @return 方案与版本信息（与 save-basic 保持一致）
     */
    InspectionSchemeBasicSaveRespDTO saveInspectionSchemeFusion(InspectionSchemeSaveFusionDTO fusionDTO);

    /**
     * 根据实验包初始化检验项目配置
     *
     * @param schemeId 方案ID
     * @param versionId 版本ID
     * @param packageId 实验包ID
     */
    void initInspectionItemsByPackage(Long schemeId, Long versionId, Long packageId);

    /**
     * 更新检验项目配置（增量更新）
     *
     * @param updateDTO 检验项目配置更新参数
     */
    void updateInspectionSchemeItems(List<InspectionSchemeItemUpdateDTO> updateDTO);

    /**
     * 更新取样配置（增量更新）
     *
     * @param updateDTO 取样配置更新参数
     */
    void updateInspectionSchemeSamplings(List<InspectionSchemeSamplingUpdateDTO> updateDTO);

    /**
     * 分页查询检验方案
     *
     * @param queryDTO 查询参数
     * @return 分页结果
     */
    CommonPage<InspectionSchemeDTO> pageInspectionScheme(InspectionSchemeQueryDTO queryDTO);

    /**
     * 获取检验方案详情
     *
     * @param id 检验方案ID
     * @return 检验方案详情
     */
    InspectionSchemeDTO getInspectionScheme(Long id);

    /**
     * 删除检验方案
     *
     * @param id 检验方案ID
     */
    void deleteInspectionScheme(Long id);

    /**
     * 通过检品ID查询检验方案下拉数据
     *
     * @param materialId 检品ID
     * @return 检验方案下拉数据列表
     */
    List<InspectionSchemeDropdownDTO> getInspectionSchemeDropdownByMaterialId(Long materialId);

    /**
     * 从现有方案版本复制，创建一个新方案（并生成首个版本），复制所有配置
     * @param dto 输入参数（源版本ID、新方案名称、新版本号等）
     * @return 保存后返回方案ID与版本ID
     */
    InspectionSchemeBasicSaveRespDTO copySchemeFromVersion(InspectionSchemeCopyFromVersionDTO dto);

    /**
     * 保存方案版本的检验项目-分析项配置
     * 前端直接传入分析项列表（含所属检验项目ID），后台按检验项目去重分组后保存
     *
     * @param dto 入参
     */
    void saveVersionItems(InspectionSchemeVersionSaveItemsDTO dto);

    /**
     * 校验判定条件引用的数据点配置是否仍然有效
     *
     * @param versionId 方案版本ID
     */
    void validateJudgmentConfigConsistency(Long versionId);
} 