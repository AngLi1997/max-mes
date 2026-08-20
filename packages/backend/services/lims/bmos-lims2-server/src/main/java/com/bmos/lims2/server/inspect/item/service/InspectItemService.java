package com.bmos.lims2.server.inspect.item.service;

import com.bmos.lims2.server.inspect.item.dto.InspectItemDTO;
import com.bmos.lims2.server.inspect.item.dto.InspectItemListDTO;
import com.bmos.lims2.server.inspect.item.dto.InspectItemWithParameterDTO;
import com.bmos.lims2.server.inspect.item.dto.InspectItemPageReqDTO;
import com.bmos.mybatis.page.CommonPage;

import java.util.List;

public interface InspectItemService {
    /**
     * 新增检验项
     * @param reqVO
     * @return: 返回带有重复分析项的id集合
     */
    List<Long> saveInspectProgram(InspectItemWithParameterDTO reqVO);

    /**
     * 删除检验项
     * @param id
     */
    void deleteInspectProgram(Long id);

    /**
     * 编辑检验项
     * @param reqVO
     * @return: 返回带有重复分析项的id集合
     */
    List<Long> updateInspectProgram(InspectItemWithParameterDTO reqVO);

    /**
     * 检验项分页查询
     * @param reqVO
     * @return
     */
    CommonPage<InspectItemWithParameterDTO> inspectProgramPage(InspectItemPageReqDTO reqVO);

    /**
     * 根据检验项id查询检验项的详情信息,包含检验项下的所有分析项信息
     * @param id
     * @return
     */
    InspectItemWithParameterDTO inspectProgramInfo(Long id);

    /**
     * 根据检验项目id集合查询检验项信息
     * @param idList
     * @return
     */
    List<InspectItemDTO> selectByIdList(List<Long> idList);

    /**
     * 查询检验项目列表 - 用于下拉选择
     * @return 检验项目列表
     */
    List<InspectItemListDTO> getList();

    /**
     * 查询全量检验项目-分析项树（用于方案版本编辑时选择）
     * @return 检验项目列表，每项含其下所有分析项
     */
    List<InspectItemWithParameterDTO> listAllWithParameters();
}
