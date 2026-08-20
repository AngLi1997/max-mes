package com.bmos.mes.service.process.service;

import com.bmos.mes.service.platform.role.dto.PlatformRoleListQueryDTO;
import com.bmos.mes.service.platform.role.role.PlatformRoleVO;
import com.bmos.mes.service.platform.user.vo.PlatformUserVO;
import com.bmos.mes.service.process.dto.ProcedureDTO;
import com.bmos.mes.service.process.dto.ProcedureModelRoomQueryDTO;
import com.bmos.mes.service.process.dto.ProcedureQueryDTO;
import com.bmos.mes.service.process.dto.ProcedureValidateDTO;
import com.bmos.mes.service.process.dto.query.ProcedureHistoricQueryDTO;
import com.bmos.mes.service.process.dto.query.ProcedurePrincipalQueryDTO;
import com.bmos.mes.service.process.dto.save.ProcedureSaveDTO;
import com.bmos.mes.service.process.dto.save.SaveProcessSortVO;
import com.bmos.mes.service.process.model.ProcedureModel;
import com.bmos.mes.service.process.model.ProcedureModelGroup;
import com.bmos.mes.service.process.model.ProcessVersion;
import com.bmos.mes.service.process.vo.*;
import com.bmos.mes.service.process.vo.Task.EquipmentModuleTreeNodeVO;
import com.bmos.mes.service.process.vo.Task.NodeVO;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
public interface ProcedureModelService {

    List<ProcedureModel> saveBatch(@NotEmpty List<ProcedureModel> procedureModels);

    void refreshBatch(ProcessVersion processVersion, @NotEmpty List<ProcedureDTO> procedures);

    List<ProcedureModel> getByProcessIdAndVersion(@NotNull Long processId, @NotNull String version);

    String saveDetail(@Validated ProcedureSaveDTO dto);

    void modifyProcedureDetail(@Validated ProcedureSaveDTO dto);

    List<HistoricVO> getHistoricProcedureList(ProcedureHistoricQueryDTO dto);

    List<ProcedureModel> getByIds(List<Long> ids);

    List<ProcedureVO> getList(@Validated ProcedureQueryDTO dto);

    ProcedureModelDetailVO getDetail(Long id);

    List<PlatformUserVO> getPrincipalList(ProcedurePrincipalQueryDTO dto);

    List<String> getProcessModelList(Long processVersionId);

    void validateProcessModel(Long processVersionId);

    List<ProcedureModelRoomOrStationVO> getProcedureModelRoomList(ProcedureModelRoomQueryDTO dto);

    ProcedureModel getById(Long procedureModelId);

    /**
     * 查询工序绑定的房间信息 产线树
     *
     * @param dto
     * @return
     */
    List<ProcedureModelRoomVO> getProcedureModelRoomInfoTree(ProcedureModelRoomQueryDTO dto);

    List<NodeVO> getNodeList(Long id, Boolean type,Long stepModelId);

    List<EquipmentModuleTreeNodeVO> getEquipmentTree();

    List<ProcedureModelRoomVO> getProcedureModelRoomInfo(ProcedureModelRoomQueryDTO dto);

    List<ProcedureModelDetailVO> selectByIds(List<Long> procedureModelIdS);

    List<ProcessSortVO> listProcessSort(Long processVersionId);

    void saveProcessSort(List<SaveProcessSortVO> sortList);

    List<ProcedureModel> getListByProcessVersionId(Long processVersionId);

    List<ProcessConfigVO> getTeamByProcessVersionId(Long processVersionId);

    List<ProcessConfigVO> getRoomListByProcessVersionId(Long processVersionId);

    List<PlatformRoleVO> getProcedureRoleRoles(PlatformRoleListQueryDTO dto);

    List<PlatformRoleVO> getRoleListByProcessVersionId(Long processVersionId);

    List<NodeVO> getProcedureNodeList(Long procedureModelId);

    List<EquipmentModuleTreeNodeVO> getStepEquipmentTree(Long stepModelId);

    List<ProcedureModelRoomVO> getStepModelRoomInfo(ProcedureModelRoomQueryDTO dto);

    List<NodeVO> getProcedureModelList(Long versionId, Long stepModelId);

    List<NodeVO> getListByDeleteIds(List<Long> procedureId);

    List<ProcessConfigVO> getRoomListByProcedureModelId(Long procedureModelId);
}
