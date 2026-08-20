package com.bmos.mes.service.plan.document.service;

import com.bmos.mes.service.plan.document.controller.vo.PlanEasyVO;
import com.bmos.mes.service.plan.document.controller.vo.TemplateInfoPageVO;
import com.bmos.mes.service.plan.document.controller.vo.TemplateVersionEasyVO;
import com.bmos.mes.service.plan.document.controller.vo.TemplateVersionPageVO;
import com.bmos.mes.service.plan.document.model.BatchTemplateInfo;
import com.bmos.mes.service.plan.document.model.BatchTemplateInfoProcess;
import com.bmos.mes.service.plan.document.model.BatchTemplateVersion;
import com.bmos.mes.service.plan.document.service.dto.*;
import com.bmos.mybatis.page.CommonPage;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;

/**
 * 批记录模板
 */
public interface BatchTemplateService {

    /**
     * 批记录模板文件上传
     *
     * @param file
     * @return
     */
    String fileUpload(MultipartFile file);

    /**
     * 添加批记录模板
     * @param dto
     * @return
     */
    void saveTemplate(TemplateSaveDTO dto);

    /**
     * 新增批记录模板版本
     * @param dto
     */
    void saveTemplateVersion(TemplateVersionSaveDTO dto);

    /**
     * 对批记录模板版本重新上传批记录模板
     * @param dto
     */
    void uploadTemplateVersion(TemplateVersionUpdateDTO dto);

    /**
     * 下载批记录模板
     *
     * @param dto
     * @param response
     */
    void downloadTemplateVersion(TemplateVersionOperateDTO dto, HttpServletResponse response);

    /**
     * 作废批记录
     * @param dto
     */
    void scrapTemplateVersion(TemplateVersionOperateDTO dto);

    /**
     * 确认批记录模板版本
     * @param dto
     */
    void confirmTemplateVersion(TemplateVersionOperateDTO dto);

    /**
     * 模板信息绑定工艺
     * @param dto
     */
    void templateInfoBindProcess(TemplateInfoBindDTO dto);

    /**
     * 模板信息绑定数据权限
     * @param dto
     */
    void templateInfoBindDataAuth(TemplateInfoBindAuthDTO dto);

    /**
     * 分页查询批记录模板信息
     * @param dto
     * @return
     */
    CommonPage<TemplateInfoPageVO> templateInfoPage(TemplateInfoPageDTO dto);

    /**
     * 分页查询批记录模板版本
     * @param dto
     * @return
     */
    CommonPage<TemplateVersionPageVO> templateVersionPage(TemplateVersionPageDTO dto);

    /**
     * 批记录模板版本生效
     * @param dto
     */
    void normalTemplateVersion(TemplateVersionOperateDTO dto);

    /**
     * 根据模板信息id查询当前模板信息绑定的所有工艺下的所有已经开始生产计划id
     * @param templateInfoId
     * @return
     */
    List<PlanEasyVO> templatePlan(Long templateInfoId);

    /**
     * 查询绑定了当前工艺id的所有模板
     *
     * @param processId
     * @return
     */
    List<BatchTemplateInfoProcess> selectTemplateProcessByProcessId(Long processId);

    /**
     * 根据模板id查询模板信息
     * @param templateInfoIdList
     * @return
     */
    List<BatchTemplateInfo> selectAuthByIdList(List<Long> templateInfoIdList, List<Long> deptIds);

    /**
     * 根据模板id查询模板信息
     * @param templateInfoId
     * @return
     */
    BatchTemplateInfo selectById(Long templateInfoId);

    /**
     * 根据模板版本id查询模板版本
     * @param templateVersionId
     * @return
     */
    BatchTemplateVersion selectByVersionId(Long templateVersionId);

    /**
     * 查询具有模板的工艺
     * @return
     */
    List<Long> selectProcessIdListByTemplateId(Long templateId);

    /**
     * 根据模板版本id集合查询模板版本
     * @param templateVersionIdList
     * @return
     */
    List<BatchTemplateVersion> selectByVersionIdList(Collection<Long> templateVersionIdList);

    /**
     * 根据模板id集合查询模板
     * @param templateInfoIdSet
     * @return
     */
    List<BatchTemplateInfo> selectByIdList(Collection<Long> templateInfoIdSet);

    /**
     * 根据工艺id查询模板版本
     * @param processId
     * @return
     */
    List<BatchTemplateVersion> selectByNormalProcessId(Long processId);

    /**
     * 根据模板id查询模板版本id
     * @param templateInfoId
     * @return
     */
    List<Long> selectTemplateVersionByInfoId(Long templateInfoId);

    /**
     * 查询模板下的默认模板版本
     *
     * @param templateInfoId
     * @return
     */
    List<TemplateVersionEasyVO> templateNormalVersionInfo(Long templateInfoId);

    /**
     * 根据路径下载批记录模板
     * @param path
     * @param response
     */
    void downloadPath(String path, HttpServletResponse response);

    /**
     * 查询绑定了模板的工艺id
     * @return
     */
    List<Long> selectAllProcessIds();
}
