package com.bmos.mes.service.process.dto.save;

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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel("新增工艺DTO")
public class ProcessSaveDTO {

    /**
     * 工艺名称
     */
    @NotBlank
    @ApiModelProperty(value = "工艺名称",required = true)
    private String name;


    /**
     * 产品id
     */
    @NotNull
    @ApiModelProperty(value = "产品id",required = true)
    private Long productId;

    /**
     * 产品分类id
     */
    @NotNull
    @ApiModelProperty(value = "产品分类id",required = true)
    private Long productCategoryId;

    /**
     * 配方id
     */
    @NotNull
    @ApiModelProperty(value = "配方版本id",required = true)
    private Long productFormulaVersionId;

    /**
     * 流程模型
     */
    @ApiModelProperty(value = "流程模型")
    private String processModel;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;


    @ApiModelProperty(value = "关联批记录",required = true)
    @NotEmpty
    @Valid
    private List<RelationBatchRecordItemDTO> batchRecordItems;

    @ApiModelProperty(value = "工序集合",required = true)
    private List<ProcedureDTO> procedures;

    @ApiModelProperty(value = "数据权限部门id",required = true)
    @NotEmpty
    private List<Long> deptIds;

    @ApiModelProperty(value = "版本号",required = true)
    @NotNull
    private String version;

    @ApiModelProperty(value = "产线id列表", required = true)
    @NotEmpty
    private List<Long> productionLineIds;

    @ApiModelProperty(value = "生产阶段代码", required = true)
    private String productionStageCode;

    @JsonIgnore
    public void validatedProcedures() {
        if (CollUtil.isEmpty(procedures)) {
            throw new BmosException(MesResponseCode.PROCESS_NOT_FINISH);
        }
        for (ProcedureDTO procedure : procedures) {
            procedure.validatedPrincipalAndGroupIds();
        }
    }

}
