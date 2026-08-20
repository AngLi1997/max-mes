package com.bmos.lims2.server.inspect.document.service;

import com.bmos.lims2.server.inspect.document.dto.*;
import com.bmos.mybatis.page.CommonPage;

import java.util.List;

public interface DocumentConfigService {

    /**
     * 请验单配置分页
     * @param dto
     * @return
     */
    CommonPage<DocumentConfigDTO> inspectionConfigPage(DocumentConfigPageReqDTO dto);

    /**
     * 新增请验单配置
     * @param dto
     */
    void saveInspectionConfig(DocumentConfigSaveDTO dto);

    /**
     * 编辑请验单配置
     * @param dto
     */
    void updateInspectionConfig(DocumentConfigUpdateDTO dto);

    /**
     * 删除请验单配置
     * @param id
     */
    void deleteInspectionConfig(Long id);

    /**
     * 启用请验单配置
     * @param id
     */
    void enableInspectionConfig(Long id);

    /**
     * 停用请验单配置
     * @param id
     */
    void disableInspectionConfig(Long id);

    /**
     * 查询请验单详情
     * @param id
     * @return
     */
    DocumentConfigWithFieldDTO queryDetail(Long id);

    /**
     * 请验单配置绑定检品
     * @param dto
     */
    void bindProduct(DocumentConfigBindProductDTO dto);

    /**
     * 获取请验单绑定的检品id列表
     * @param id
     * @return
     */
    List<Long> queryBindProductIdList(Long id);

    /**
     * 根据检品id获取请验单列表
     * @param productId 检品id
     * @return 请验单列表
     */
    List<DocumentConfigWithFieldDTO> getInspectionConfigListByProductId(Long productId);

    /**
     * 根据检品id获取已启用的请验单模板列表（用于下拉）
     * @param productId 检品id
     * @return 启用的请验单模板列表
     */
    List<DocumentConfigWithFieldDTO> getEnabledInspectionConfigListByProductId(Long productId);

    /**
     * 更新请验单配置字段排序
     * @param dto 排序参数
     */
    void updateFieldSort(DocumentConfigFieldSortUpdateDTO dto);
}
