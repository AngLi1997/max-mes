package com.bmos.lims2.server.inspect.parameter.service;

import com.bmos.lims2.server.inspect.parameter.dto.InspectParameterDTO;
import com.bmos.lims2.server.inspect.parameter.dto.InspectParameterListDTO;
import com.bmos.lims2.server.inspect.parameter.dto.InspectParameterPageReqDTO;
import com.bmos.mybatis.page.CommonPage;

import java.util.List;

/**
 * 分析项Service接口
 *
 * @author makejava
 * @since 2024-03-02 12:43:37
 */
public interface InspectParameterService {

    /**
     * 保存分析项
     */
    void save(InspectParameterDTO dto);

    /**
     * 修改分析项
     */
    void update(Long id, InspectParameterDTO dto);

    /**
     * 删除分析项
     */
    void delete(Long id);

    /**
     * 获取分析项详情
     */
    InspectParameterDTO getById(Long id);

    /**
     * 分页查询分析项列表
     */
    CommonPage<InspectParameterDTO> getPage(InspectParameterPageReqDTO dto);

    /**
     * 根据id列表查询分析项
     * @param idList id列表
     * @return 分析项列表
     */
    List<InspectParameterDTO> selectByIdList(List<Long> idList);

    /**
     * 查询分析项列表 - 用于下拉选择
     * @return 分析项列表
     */
    List<InspectParameterListDTO> getList();

    /**
     * 根据ID列表查询分析项完整信息（包含数据点）
     *
     * @param idList ID列表
     * @return 分析项完整信息列表
     */
    List<InspectParameterDTO> selectFullInfoByIdList(List<Long> idList);
}
