package com.bmos.mes.service.process.dto.modify;

import cn.hutool.core.collection.CollUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.process.dto.ProcedureDTO;
import com.bmos.mes.service.process.dto.RelationBatchRecordItemDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@ApiModel("编辑工艺DTO")
public class ProcessModifyDTO {

    @ApiModelProperty(value = "工艺版本id",required = true)
    @NotNull
    private Long id;

    @ApiModelProperty(value = "工艺id",required = true)
    @NotNull
    private Long processId;

    @ApiModelProperty(value = "版本号",required = true)
    @NotNull
    private String version;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty(value = "配方版本id",required = true)
    @NotNull
    private Long productFormulaVersionId;

    @ApiModelProperty(value = "产线id列表", required = true)
    @NotEmpty
    private List<Long> productionLineIds;

    @ApiModelProperty(value = "生产阶段代码", required = true)
    private String productionStageCode;

    @ApiModelProperty("流程模型")
    private String processModel;

    /**
     * 产品id
     */
    @ApiModelProperty(value = "产品id",required = true)
    private Long productId;

    /**
     * 产品分类id
     */
    @ApiModelProperty(value = "产品分类id",required = true)
    private Long productCategoryId;

    @ApiModelProperty(value = "关联批记录",required = true)
    @NotEmpty
    @Valid
    private List<RelationBatchRecordItemDTO> batchRecordItems;


    @ApiModelProperty(value = "工序信息",required = true)
    @NotEmpty
    @Valid
    private List<ProcedureDTO> procedures;

    @JsonIgnore
    public void validatedProcedures() {
        if (CollUtil.isEmpty(procedures)) {
            throw new BmosException(MesResponseCode.PROCESS_NOT_FINISH);
        }
        // 重复性校验
        validateProcedureIds();
        for (ProcedureDTO procedure : procedures) {
            procedure.validatedPrincipalAndGroupIds();
        }
    }

    private void validateProcedureIds() {
        HashSet<Long> procedureIds = new HashSet<>();
        HashSet<String> hisNames = new HashSet<>();
        HashSet<String> names = new HashSet<>();
        procedures.forEach(procedure ->{
                    if (!names.add(procedure.getName())) {
                        throw new BmosException(MesResponseCode.PROCEDURE_DUPLICATE_NAME);
                    }
                    if (!hisNames.add(procedure.getHistoricalName())) {
                        throw new BmosException(MesResponseCode.HISTORY_PROCEDURE_DUPLICATE);
                    }
                    if (procedure.getProcedureId() != null && !procedureIds.add(procedure.getProcedureId())) {
                        throw new BmosException(MesResponseCode.HISTORY_PROCEDURE_DUPLICATE);
                    }
                });
    }

}
