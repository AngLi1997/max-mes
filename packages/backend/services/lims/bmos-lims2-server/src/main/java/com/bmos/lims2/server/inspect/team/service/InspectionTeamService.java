package com.bmos.lims2.server.inspect.team.service;

import com.bmos.lims2.server.inspect.team.dto.InspectionTeamAssignUserDTO;
import com.bmos.lims2.server.inspect.team.dto.InspectionTeamDTO;
import com.bmos.lims2.server.inspect.team.dto.InspectionTeamPageReqDTO;
import com.bmos.lims2.server.inspect.team.dto.InspectionTeamSaveDTO;
import com.bmos.lims2.server.inspect.team.dto.InspectionTeamUpdateDTO;
import com.bmos.mybatis.page.CommonPage;

import java.util.List;

public interface InspectionTeamService {

    /**
     * 通过方案版本ID与检验项目ID查询班组人员列表
     * 服务内部自动判断是稳定性方案还是常规检验方案
     *
     * @param schemeVersionId 方案版本ID
     * @param inspectItemId   检验项目ID
     * @return 人员列表
     */
    List<com.bmos.lims2.server.inspect.team.dto.InspectionTeamUserDTO> listUsersBySchemeVersionAndInspectItem(
            Long schemeVersionId, Long inspectItemId);

    /**
     * 新增班组
     *
     * @param dto
     */
    void saveInspectionTeam(InspectionTeamSaveDTO dto);

    /**
     * 更新班组
     *
     * @param dto 更新参数
     */
    void updateInspectionTeam(InspectionTeamUpdateDTO dto);

    /**
     * 班组分页
     *
     * @param dto
     * @return
     */
    CommonPage<InspectionTeamDTO> getInspectionTeamPage(InspectionTeamPageReqDTO dto);

    /**
     * 启用班组
     *
     * @param id
     */
    void enableInspectionTeam(Long id);

    /**
     * 停用班组
     *
     * @param id
     */
    void disableInspectionTeam(Long id);

    /**
     * 班组人员分配
     *
     * @param dto
     */
    void inspectionTeamAssignUser(InspectionTeamAssignUserDTO dto);

    /**
     * 获取检验班组人员列表
     *
     * @param id
     * @return
     */
    List<String> getInspectionTeamUserIdList(Long id);


    /**
     * 查询当前用户能够看到的班组列表
     *
     * @return 查询结果
     */
    List<InspectionTeamDTO> getTeamListByPermission();
}
