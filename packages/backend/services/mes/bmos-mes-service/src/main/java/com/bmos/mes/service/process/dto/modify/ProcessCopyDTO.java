package com.bmos.mes.service.process.dto.modify;

import cn.hutool.core.collection.CollUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.process.dto.ProcedureCopyDTO;
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
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel("复制工艺DTO")
public class ProcessCopyDTO {

    @ApiModelProperty(value = "工艺版本id",required = true)
    @NotNull
    private Long id;

    @ApiModelProperty(value = "工艺名称",required = true)
    @NotNull
    private String name;

    @ApiModelProperty(value = "工艺id",required = true)
    @NotNull
    private Long processId;

    @ApiModelProperty(value = "产品id",required = true)
    @NotNull
    private Long productId;
    /**
     * 产品分类id
     */
    @NotNull
    @ApiModelProperty(value = "产品分类id",required = true)
    private Long productCategoryId;

    @ApiModelProperty(value = "版本号",required = true)
    @NotNull
    private String version;

    @ApiModelProperty(value = "源版本号",required = true)
    private String originVersion;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty(value = "配方版本id",required = true)
    @NotNull
    private Long productFormulaVersionId;

    @ApiModelProperty("流程模型")
    private String processModel;

    @NotEmpty
    @ApiModelProperty(value = "产线id",required = true)
    private List<Long> productionLineIds;

    @ApiModelProperty(value = "关联批记录",required = true)
    @NotEmpty
    @Valid
    private List<RelationBatchRecordItemDTO> batchRecordItems;


    @ApiModelProperty(value = "工序信息",required = true)
    private List<ProcedureCopyDTO> procedures;

    @ApiModelProperty(value = "配方物料id", required = false)
    private List<Long> formulaMaterialIdList;

    @JsonIgnore
    public void validatedProcedures() {
        if (CollUtil.isEmpty(procedures)) {
            throw new BmosException(MesResponseCode.PROCESS_NOT_FINISH);
        }
        for (ProcedureCopyDTO procedure : procedures) {
            procedure.validatedPrincipalAndGroupIds();
        }
    }



    @ApiModelProperty(value = "数据权限部门id")
    @NotEmpty
    private List<Long> deptIds;

}
