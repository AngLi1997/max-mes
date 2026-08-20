package com.bmos.mes.service.process.service;

import com.bmos.mes.service.process.dto.ProcessTodoPageDTO;
import com.bmos.mes.service.process.dto.ProcessVersionQueryDTO;
import com.bmos.mes.service.process.dto.modify.ProcessModifyDTO;
import com.bmos.mes.service.process.dto.modify.ProcessSaveVersionDTO;
import com.bmos.mes.service.process.dto.modify.ProcessVersionChangeStateDTO;
import com.bmos.mes.service.process.dto.query.ProcessDetailQueryDTO;
import com.bmos.mes.service.process.dto.query.ProcessQueryDTO;
import com.bmos.mes.service.process.dto.query.ProcessVersionPageQueryDTO;
import com.bmos.mes.service.process.model.ProcessVersion;
import com.bmos.mes.service.process.vo.ProcessTodoPageVO;
import com.bmos.mes.service.process.vo.ProcessVO;
import com.bmos.mes.service.process.vo.ProcessVersionPageVO;
import com.bmos.mes.service.record.business.model.ProcessDetailInfo;
import com.bmos.mybatis.page.CommonPage;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

@Validated
public interface ProcessVersionService {
    void save(@Validated ProcessVersion processVersion);

    CommonPage<ProcessVersionPageVO> getPage(@Validated ProcessVersionPageQueryDTO dto);

    ProcessVersion updateState(@Validated ProcessVersionChangeStateDTO dto);

    ProcessVersion getByProcessIdAndVersion(@Validated ProcessDetailQueryDTO dto);
    ProcessVersion getByProcessIdAndVersion(@NotNull Long processId,@NotNull String version);

    void modifyVersion(@Validated ProcessModifyDTO dto, ProcessVersion processVersion);

    /**
     * 校验版本号是否重复
     *
     * @param id        版本id （存在 id 时判断排除此 id 的数据）
     * @param processId 工艺id (存在工艺时，判断该工艺下的所有数据)
     * @param version   版本号
     */
    void validateVersion(Long id, Long processId, String version);

    ProcessVersion validateEditState(Long id);

    ProcessVersion getById(Long id);

    Boolean existByIdAndVersion(Long id,String version);

    ProcessVersion saveNewVersion(ProcessSaveVersionDTO dto, ProcessVersion processVersion);

    List<ProcessVO> getProcessList(ProcessQueryDTO dto);

    ProcessVersion getByProcessModel(Long processId, String version);

    void updateById(ProcessVersion processVersion);

    List<ProcessTodoPageVO> getAuditTodoProcessVersionIds(ProcessTodoPageDTO dto, String value);

    List<ProcessTodoPageVO> getByProcessInstanceIds(List<String> processInstanceIds,String actionState);

    ProcessVersion getByProcessInstanceId(String processInstanceId);

    ProcessDetailInfo getProcessDetailInfo(Long processId, String processVersion);


    List<ProcessVersion> selectListByProcessId(Long id);

    void validateVersionAudit(Long id);

    ProcessVersion updateVersionActionState(String processInstanceId);

    List<ProcessVersion> updateProcessVersionActionState(String value);

    /**
     * 根据工艺id+工艺版本查询数据
     * @param processVersionQueryDTOS
     * @return
     */
    List<ProcessVersion> selectByQueryDTOLIst(List<ProcessVersionQueryDTO> processVersionQueryDTOS);

    /**
     * 根据工艺版本id查询数据
     * @param processVersionIdList
     * @return
     */
    List<ProcessVersion> getByIds(Collection<Long> processVersionIdList);
}
