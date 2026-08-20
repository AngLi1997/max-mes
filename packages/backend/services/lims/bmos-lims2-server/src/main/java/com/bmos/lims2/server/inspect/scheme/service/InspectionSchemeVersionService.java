package com.bmos.lims2.server.inspect.scheme.service;

import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeVersionQueryDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeVersionAuditQueryDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeVersionCopyDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeBasicSaveDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeItemUpdateDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeSamplingUpdateDTO;
import com.bmos.lims2.server.inspect.scheme.dto.response.InspectionSchemeVersionDTO;
import com.bmos.lims2.server.inspect.scheme.dto.response.InspectionSchemeVersionFullConfigDTO;
import com.bmos.lims2.server.inspect.scheme.dto.response.InspectionSchemeVersionAuditDTO;
import com.bmos.mybatis.page.CommonPage;

import java.util.List;

/**
 * 检验方案版本Service接口
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
public interface InspectionSchemeVersionService {

    /**
     * 分页查询检验方案版本
     *
     * @param queryDTO 查询参数
     * @return 分页结果
     */
    CommonPage<InspectionSchemeVersionDTO> pageInspectionSchemeVersion(InspectionSchemeVersionQueryDTO queryDTO);

    /**
     * 获取检验方案版本详情
     *
     * @param id 版本ID
     * @return 版本详情
     */
    InspectionSchemeVersionDTO getInspectionSchemeVersion(Long id);

    /**
     * 获取检验方案版本完整配置（用于复制和编辑）
     *
     * @param id 版本ID
     * @return 版本完整配置信息
     */
    InspectionSchemeVersionFullConfigDTO getInspectionSchemeVersionFullConfig(Long id);

    /**
     * 完成检验方案版本（编辑中→已完成）：提交全量数据，后台校验必填项并置为已完成
     * 校验：至少一个检验项目，每个项目至少一个分析项，分析项必填字段完整，至少一条取样信息
     *
     * @param id              版本ID
     * @param basicDTO        方案基础信息（名称、版本号、物料等）
     * @param itemUpdates     检验项目配置（全量，可为空）
     * @param samplingUpdates 取样配置（全量，可为空）
     */
    void completeInspectionSchemeVersion(Long id, InspectionSchemeBasicSaveDTO basicDTO,
                                         List<InspectionSchemeItemUpdateDTO> itemUpdates,
                                         List<InspectionSchemeSamplingUpdateDTO> samplingUpdates);

    /**
     * 启用检验方案版本（发起审批）
     *
     * @param id 版本ID
     */
    void activateInspectionSchemeVersion(Long id);

    /**
     * 停用检验方案版本
     *
     * @param id 版本ID
     */
    void deactivateInspectionSchemeVersion(Long id);

    /**
     * 作废检验方案版本
     *
     * @param id 版本ID
     */
    void voidInspectionSchemeVersion(Long id);

    /**
     * 审批通过回调处理
     *
     * @param versionId 版本ID
     */
    void handleApprovalPassed(Long versionId);

    /**
     * 审批拒绝回调处理
     *
     * @param versionId 版本ID
     */
    void handleApprovalRejected(Long versionId);

    /**
     * 分页查询待审批的检验方案版本
     *
     * @param queryDTO 查询参数
     * @return 分页结果
     */
    CommonPage<InspectionSchemeVersionAuditDTO> pageAuditSchemeVersions(InspectionSchemeVersionAuditQueryDTO queryDTO);

    /**
     * 审批成功回调处理
     *
     * @param processInstanceId 流程实例ID
     * @param comment 审批意见
     * @param userId 审批人ID
     */
    void auditProcessSuccessCallBack(String processInstanceId, String comment, String userId);

    /**
     * 审批拒绝回调处理
     *
     * @param processInstanceId 流程实例ID
     * @param comment 审批意见
     * @param remark 备注
     * @param userId 审批人ID
     * @param nodeName 节点名称
     */
    void auditProcessRejectCallBack(String processInstanceId, String comment, String remark, String userId, String nodeName);

    /**
     * 审批执行成功回调处理
     *
     * @param businessKey 业务key
     * @param comment 审批意见
     * @param remark 备注
     * @param userId 审批人ID
     * @param nodeName 节点名称
     */
    void auditExecutionSuccessCallBack(String businessKey, String comment, String remark, String userId, String nodeName);

    /**
     * 审批执行拒绝回调处理
     *
     * @param businessKey 业务key
     * @param comment 审批意见
     * @param userId 审批人ID
     */
    void auditExecutionRejectCallBack(String businessKey, String comment, String userId);

    /**
     * 获取检验方案版本基础信息（版本信息、物料、实验包）
     *
     * @param id 版本ID
     * @return 版本基础信息
     */
    InspectionSchemeVersionFullConfigDTO getVersionBasicConfig(Long id);

    /**
     * 获取检验方案版本分析项信息（检验项目列表，含分析项、数据点、判定）
     *
     * @param id 版本ID
     * @return 检验项目配置列表
     */
    List<InspectionSchemeVersionFullConfigDTO.InspectionItemConfigDTO> getVersionItemsConfig(Long id);

    /**
     * 获取检验方案版本取样配置信息
     *
     * @param id 版本ID
     * @return 取样配置列表
     */
    List<InspectionSchemeVersionFullConfigDTO.SamplingConfigDTO> getVersionSamplingConfig(Long id);

    /**
     * 新增一个版本并从源版本复制配置
     * @param dto 输入参数（源版本ID、新版本号等）
     * @return 新版本ID
     */
    Long copyVersionFromSource(InspectionSchemeVersionCopyDTO dto);

    /**
     * 查询方案版本关联的操作规程列表
     *
     * @param schemeVersionId 方案版本ID
     * @return 操作规程列表
     */
    java.util.List<com.bmos.lims2.server.inspect.scheme.dto.response.SchemeVersionOperateRuleDTO> listOperateRulesByVersionId(Long schemeVersionId);
} 