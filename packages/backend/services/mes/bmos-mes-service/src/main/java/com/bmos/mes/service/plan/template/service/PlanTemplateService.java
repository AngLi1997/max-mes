package com.bmos.mes.service.plan.template.service;

import com.bmos.mes.service.plan.template.dto.PlanTemplateChangeStateDTO;
import com.bmos.mes.service.plan.template.dto.PlanTemplateEditDTO;
import com.bmos.mes.service.plan.template.dto.PlanTemplatePageQueryDTO;
import com.bmos.mes.service.plan.template.dto.PlanTemplateSaveDTO;
import com.bmos.mes.service.plan.template.model.PlanTemplate;
import com.bmos.mes.service.plan.template.vo.PlanTemplateDetailVO;
import com.bmos.mes.service.plan.template.vo.PlanTemplateListVO;
import com.bmos.mes.service.plan.template.vo.PlanTemplatePageVO;
import com.bmos.mes.service.process.model.Process;
import com.bmos.mybatis.page.CommonPage;

import java.util.List;

public interface PlanTemplateService {


    /**
     * 新增生产计划模板
     * @param dto
     */
    void savePlanTemplate(PlanTemplateSaveDTO dto);


    /**
     * 查询生产计划分页
     * @param dto
     * @return
     */
    CommonPage<PlanTemplatePageVO> queryPlanTemplatePage(PlanTemplatePageQueryDTO dto);

    /**
     * 修改生产计划模板启停状态
     * @param dto
     */
    void changePlanTemplateState(PlanTemplateChangeStateDTO dto);

    /**
     * 删除生产计划模板
     * @param id
     */
    void deletePlanTemplate(Long id);

    /**
     * 获取生产计划模板详情
     * @param id
     * @return
     */
    PlanTemplateDetailVO getPlanTemplateDetail(Long id);

    /**
     * 生产计划模板编辑
     * @param dto
     */
    void editPlanTemplate(PlanTemplateEditDTO dto);

    /**
     * 查询启用的生产计划模板列表
     * @return
     */
    List<PlanTemplateListVO> getEnablePlanTemplateList();

    /**
     * 根据id查询生产计划模板
     * @param planTemplateId
     * @return
     */
    PlanTemplate getById(Long planTemplateId);

    /**
     * 根据工艺版本更新模板确认
     * @param processes
     */
    void updateTemplateConfirmStatus(List<Process> processes);

    /**
     * 校验模板工艺版本是否匹配
     * @param planTemplateId
     * @return
     */
    Boolean validateProcessVersionMatch(Long planTemplateId);
}
