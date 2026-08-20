package com.bmos.mes.service.process.dto.save;

import cn.hutool.core.collection.CollUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.process.dto.ProcedureStepDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@ToString
@ApiModel("工序流程保存DTO")
public class ProcedureSaveDTO {

    @ApiModelProperty("工序id")
    @NotNull
    private Long procedureId;

    @ApiModelProperty("流程模型")
    @NotBlank
    private String processModel;

    @ApiModelProperty("工序步骤集合")
    private List<ProcedureStepDTO> procedureSteps;

    @ApiModelProperty("任务节点集合")
    private List<ProcedureStepDTO> procedureTasks;

    @JsonIgnore
    public void validatedProcedureSteps() {
        if (CollUtil.isEmpty(procedureSteps)) {
            throw new BmosException(MesResponseCode.PROCESS_STEP_NOT_FINISH);
        }
        // 重复性校验
        duplicateValidate();
        for (ProcedureStepDTO procedureStepDTO : procedureSteps) {
            procedureStepDTO.validated(false);
        }
        if (CollUtil.isNotEmpty(procedureTasks)) {
            for (ProcedureStepDTO procedureStepDTO : procedureTasks) {
                procedureStepDTO.validated(true);
            }
        }
    }

    /**
     * 重复性校验
     * 同一工序下 名称不能重复,历史步骤id不能重复
     */
    private void duplicateValidate() {
        ArrayList<ProcedureStepDTO> allModels = new ArrayList<>(procedureSteps);
        Optional.ofNullable(procedureTasks).ifPresent(allModels::addAll);
        HashSet<String> names = new HashSet<>();
        HashSet<Long> stepIds = new HashSet<>();
        HashSet<String> hisNames = new HashSet<>();
        for (ProcedureStepDTO model : allModels) {
            // 名称不能重复
            if (!names.add(model.getName())) {
                throw new BmosException(MesResponseCode.PROCEDURE_STEP_DUPLICATE_NAME);
            }
            // 历史步骤id不能重复
            if (model.getProcedureStepId() != null && !stepIds.add(model.getProcedureStepId())) {
                throw new BmosException(MesResponseCode.HISTORY_PROCEDURE_STEP_DUPLICATE);
            }
            // 历史名称也不能重复
            if (!hisNames.add(model.getHistoricalName())) {
                throw new BmosException(MesResponseCode.HISTORY_PROCEDURE_STEP_DUPLICATE);
            }
        }
    }
}
