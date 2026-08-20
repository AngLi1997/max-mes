package com.bmos.lims2.server.stability.scheme.service;

import com.bmos.lims2.server.stability.scheme.dto.request.StabilitySchemeItemSaveDTO;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilitySchemePlanSaveDTO;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilitySchemeSaveDTO;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilitySchemeVersionQueryDTO;
import com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeVersionAuditDTO;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilitySchemeVersionAuditQueryDTO;
import com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeVersionDTO;
import com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeVersionFullConfigDTO;
import com.bmos.mybatis.page.CommonPage;

import java.util.List;

/**
 * 稳定性方案版本Service接口
 *
 * @author makejava
 * @since 2025-03-17 10:00:00
 */
public interface StabilitySchemeVersionService {

    /**
     * 分页查询稳定性方案版本列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    CommonPage<StabilitySchemeVersionDTO> pageStabilitySchemeVersion(StabilitySchemeVersionQueryDTO queryDTO);

    /**
     * 根据ID查询稳定性方案版本
     *
     * @param id 版本ID
     * @return 版本信息
     */
    StabilitySchemeVersionDTO getStabilitySchemeVersion(Long id);

    /**
     * 根据方案ID查询版本列表
     *
     * @param schemeId 方案ID
     * @return 版本列表
     */
    List<StabilitySchemeVersionDTO> listBySchemeId(Long schemeId);

    /**
     * 根据物料ID查询对应稳定性方案的生效版本列表
     *
     * @param materialId 物料ID
     * @return 生效版本列表（含方案基本信息）
     */
    List<StabilitySchemeVersionDTO> listActiveVersionsByMaterialId(Long materialId);

    /**
     * 启用版本（发起审批）
     *
     * @param versionId 版本ID
     */
    void activateVersion(Long versionId);

    /**
     * 停用版本
     *
     * @param versionId 版本ID
     */
    void deactivateVersion(Long versionId);

    /**
     * 作废版本
     *
     * @param versionId 版本ID
     */
    void voidVersion(Long versionId);

    /**
     * 复制版本
     *
     * @param versionId    原版本ID
     * @param newVersionNo 新版本号
     * @param description  版本描述
     * @return 新版本ID
     */
    Long copyVersion(Long versionId, String newVersionNo, String description);

    /**
     * 完成版本编辑（EDITING → COMPLETED）：增量保存数据并校验必填项，将版本置为已完成
     * 检验项目配置使用增量更新（有ID则更新，无ID则新增，不在列表中的项目保持不变）
     *
     * @param versionId   版本ID
     * @param basicDTO    方案基础信息（名称、版本号、检品等）
     * @param itemSaveDTO 检验项目配置（增量）
     * @param planSaveDTO 检验计划（全量）
     */
    void completeVersion(Long versionId, StabilitySchemeSaveDTO basicDTO,
                         StabilitySchemeItemSaveDTO itemSaveDTO, StabilitySchemePlanSaveDTO planSaveDTO);

    /**
     * 分页查询待审批的稳定性方案版本
     *
     * @param queryDTO 查询参数
     * @return 分页结果
     */
    CommonPage<StabilitySchemeVersionAuditDTO> pageAuditVersions(StabilitySchemeVersionAuditQueryDTO queryDTO);

    /**
     * 审批流程通过结束回调（流程全部节点通过）
     *
     * @param processInstanceId 流程实例ID
     * @param comment           审批意见
     * @param userId            审批人ID
     */
    void auditProcessSuccessCallBack(String processInstanceId, String comment, String userId);

    /**
     * 审批流程拒绝结束回调（流程终止拒绝）
     *
     * @param processInstanceId 流程实例ID
     * @param comment           审批意见
     * @param remark            备注
     * @param userId            审批人ID
     * @param nodeName          节点名称
     */
    void auditProcessRejectCallBack(String processInstanceId, String comment, String remark, String userId, String nodeName);

    /**
     * 审批节点执行通过回调（单节点通过）
     *
     * @param businessKey 业务key
     * @param comment     审批意见
     * @param remark      备注
     * @param userId      审批人ID
     * @param nodeName    节点名称
     */
    void auditExecutionSuccessCallBack(String businessKey, String comment, String remark, String userId, String nodeName);

    /**
     * 审批节点执行拒绝回调（退回）
     *
     * @param businessKey 业务key
     * @param comment     审批意见
     * @param userId      审批人ID
     */
    void auditExecutionRejectCallBack(String businessKey, String comment, String userId);

    /**
     * 获取稳定性方案版本完整配置（基础信息+检验项目+检验计划）
     *
     * @param versionId 版本ID
     * @return 完整配置DTO
     */
    StabilitySchemeVersionFullConfigDTO getVersionFullConfig(Long versionId);

    /**
     * 获取稳定性方案版本基础信息（版本信息、检品）
     *
     * @param versionId 版本ID
     * @return 基础配置DTO
     */
    StabilitySchemeVersionFullConfigDTO getVersionBasicConfig(Long versionId);
}
