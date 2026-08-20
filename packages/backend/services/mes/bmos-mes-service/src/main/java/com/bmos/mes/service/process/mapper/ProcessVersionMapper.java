package com.bmos.mes.service.process.mapper;

import com.bmos.mes.common.enums.StateEnum;
import com.bmos.mes.service.process.dto.ProcessTodoPageDTO;
import com.bmos.mes.service.process.dto.ProcessVersionQueryDTO;
import com.bmos.mes.service.process.dto.query.ProcessDetailQueryDTO;
import com.bmos.mes.service.process.dto.query.ProcessQueryDTO;
import com.bmos.mes.service.process.dto.query.ProcessVersionPageQueryDTO;
import com.bmos.mes.service.process.model.ProcessVersion;
import com.bmos.mes.service.process.vo.ProcessTodoPageVO;
import com.bmos.mes.service.process.vo.ProcessVO;
import com.bmos.mes.service.process.vo.ProcessVersionPageVO;
import com.bmos.mes.service.record.business.model.ProcessDetailInfo;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProcessVersionMapper extends BaseMapperX<ProcessVersion> {
    List<ProcessVersionPageVO> selectPageList(ProcessVersionPageQueryDTO dto);

    default ProcessVersion selectByProcessIdAndVersion(ProcessDetailQueryDTO dto) {
        return selectOne(new LambdaQueryWrapperX<ProcessVersion>()
                .eq(ProcessVersion::getProcessId, dto.getProcessId())
                .eq(ProcessVersion::getVersion, dto.getVersion()));
    }

    default Boolean versionExists(Long id, Long processId, String version) {
        return exists(new LambdaQueryWrapperX<ProcessVersion>()
                .eq(ProcessVersion::getVersion, version)
                .eq(ProcessVersion::getProcessId, processId)
                .neIfPresent(ProcessVersion::getId, id)
                .last("limit 1"));
    }

    String selectProcessNameByProcessId(@Param("processId") Long processId);

    default Boolean existByIdAndVersion(Long id, String version) {
        return exists(new LambdaQueryWrapperX<ProcessVersion>()
                .eq(ProcessVersion::getVersion, version)
                .eq(ProcessVersion::getId, id)
                .last("limit 1"));
    }

    default ProcessVersion selectByProcessIdAndVersion(Long processId, String version) {
        return selectOne(new LambdaQueryWrapperX<ProcessVersion>()
                .eq(ProcessVersion::getProcessId, processId)
                .eq(ProcessVersion::getVersion, version));
    }

    List<ProcessVO> selectProcessList(ProcessQueryDTO dto);

    List<ProcessTodoPageVO> selectAuditTodoProcessVersionIds(@Param("dto") ProcessTodoPageDTO dto,
                                                             @Param("actionState") String actionState);

    List<ProcessTodoPageVO> selectByProcessInstanceIds(@Param("processInstanceIds") List<String> processInstanceIds,
                                                       @Param("actionState") String actionState);

    default ProcessVersion selectByProcessInstanceId(String processInstanceId) {
        return selectOne(new LambdaQueryWrapperX<ProcessVersion>().eq(ProcessVersion::getProcessInstanceId, processInstanceId));
    }

    default Boolean existsEnabled(Long processId) {
        return exists(new LambdaQueryWrapperX<ProcessVersion>().eq(ProcessVersion::getProcessId, processId)
                .eq(ProcessVersion::getState, StateEnum.ON.getValue()));
    }

    ProcessDetailInfo selectProcessDetailInfo(@Param("processId") Long processId, @Param("processVersion") String processVersion);

    default List<ProcessVersion> selectListByProcessId(Long id) {
        return selectList(new LambdaQueryWrapperX<ProcessVersion>()
                .eq(ProcessVersion::getProcessId, id));
    }

    default List<ProcessVersion> getListByActionStateAndDate(String state,String date){
        return selectList(new LambdaQueryWrapperX<ProcessVersion>()
                .eq(ProcessVersion::getActionState,state)
                .le(ProcessVersion::getEffectDate,date));
    }

    default List<ProcessVersion> selectListByProcessIdListAndState(List<Long> processIds, String state){
        return selectList(new LambdaQueryWrapperX<ProcessVersion>()
                .eq(ProcessVersion::getActionState,state)
                .in(ProcessVersion::getProcessId,processIds));
    }

    List<ProcessVersion> selectByQueryDTOLIst(@Param("queryDTOList") List<ProcessVersionQueryDTO> processVersionQueryDTOS);
}
