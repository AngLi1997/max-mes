package com.bmos.mes.service.process.service;

import com.bmos.mes.service.plan.production.vo.BuildPlanBatchNextNoVO;
import com.bmos.mes.service.process.dto.ProcessTodoPageDTO;
import com.bmos.mes.service.process.dto.ProcessTreeQueryDTO;
import com.bmos.mes.service.process.dto.ProcessVersionAuditDTO;
import com.bmos.mes.service.process.dto.SaveDashboardConfigDTO;
import com.bmos.mes.service.process.dto.modify.ProcessCopyDTO;
import com.bmos.mes.service.process.dto.modify.ProcessModifyDTO;
import com.bmos.mes.service.process.dto.modify.ProcessSaveVersionDTO;
import com.bmos.mes.service.process.dto.modify.ProcessVersionChangeStateDTO;
import com.bmos.mes.service.process.dto.query.*;
import com.bmos.mes.service.process.dto.save.ProcessRecordOrderSaveDTO;
import com.bmos.mes.service.process.dto.save.ProcessRelationSaveDTO;
import com.bmos.mes.service.process.dto.save.ProcessSaveDTO;
import com.bmos.mes.service.process.model.Process;
import com.bmos.mes.service.process.vo.*;
import com.bmos.mybatis.page.CommonPage;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Set;

@Validated
public interface ProcessService {

    ProcessVersionVO save(@Validated ProcessSaveDTO dto);

    CommonPage<ProcessPageVO> getPage(@Validated ProcessPageQueryDTO dto);

    List<ProcessListItemVO> getList(ProcessListQueryDTO dto);
    List<ProcessListItemVO> getRelationProcessList(ProcessRelationQueryDTO dto);

    CommonPage<ProcessVersionPageVO> getVersionPage(@Validated ProcessVersionPageQueryDTO dto);

    void changeProcessVersionState(@Validated ProcessVersionChangeStateDTO dto);

    ProcessDetailVO getDetail(@Validated ProcessDetailQueryDTO dto);

    ProcessVersionVO modifyProcess(@Validated ProcessModifyDTO dto);

    ProcessVersionVO saveProcessVersion(@Validated ProcessSaveVersionDTO dto);

    ProcessVersionVO copyProcessVersion(@Validated ProcessCopyDTO dto);

    List<ProcessVO> getVersionList(ProcessQueryDTO dto);

    List<ProcessRecordOrderVO> getRecordOrder(ProcessRecordOrderQueryDTO dto);

    void saveRecordOrder(ProcessRecordOrderSaveDTO dto);

    List<ProductProcessTreeNodeVO> getProcessProductTree();

    void auditVersion(ProcessVersionAuditDTO dto);

    CommonPage<ProcessTodoPageVO> getAuditTodoPage(ProcessTodoPageDTO dto);
    void auditProcessSuccessCallBack(String processInstanceId,String comment,String userId);
    void auditProcessRejectCallBack(String processInstanceId,String comment,String remark,String userId,String nodeName);
    void auditExecutionSuccessCallBack(String businessKey,String comment,String remark,String userId,String nodeName);
    void auditExecutionRejectCallBack(String businessKey,String comment,String userId);
    List<ProcessListItemVO> getRecursionRelationProcessList(ProcessRelationQueryDTO dto);

    List<ProcessRelationVO> getProcessRelation(Long processId);

    void saveProcessRelation(ProcessRelationSaveDTO dto);

    List<String> getAuditBusinessKey(List<Long> deptIdList);

    List<Long> getIdListByDeptIds();

    /**
     * 获取产线名称
     * @return
     */
    List<ProductLineVO> getProductLine();

    List<ProductLineRoomVO> getLineRoom(Long lineId);

    /**
     * 获取产线树
     * @return
     */
    List<ProductLineModuleTreeNodeVO> getProductLineTree();
    void updateProcessVersionActionState();

    List<ProcessListItemVO> getInstructionProcessList(ProcessListQueryDTO dto);

    /**
     * @param dto
     *        是否过滤出生效版本的工艺
     *        是否过滤工艺权限
     * @return
     */
    List<ProcessListItemTreeVO> getListTree(ProcessTreeQueryDTO dto);

    /**
     * 根据工艺id集合查询绑定的产品的所有分类id
     *
     * @param processIdList
     * @return
     */
    Set<Long> getByIdList(List<Long> processIdList);

    /**
     * 根据工序差选产品信息
     * @param processIdList 工艺id集合
     * @return
     */
    List<BuildPlanBatchNextNoVO> selectProductListByProcessIdS(Set<Long> processIdList);


    /**
     * 查询工艺大屏看板数据配置
     * @param processId 工艺id
     * @return
     */
    ProcessDashboardVO getDashBoardConfig(Long processId);

    /**
     * 保存工艺大屏看板数据配置
     * @param dto
     */
    void saveDashboardConfig(SaveDashboardConfigDTO dto);

    Process getOneByVersionId(Long businessId);

    /**
     * 根据工艺id集合查询工艺信息
     * @param processIdList
     * @return
     */
    List<Process> selectByIdList(List<Long> processIdList);
}
