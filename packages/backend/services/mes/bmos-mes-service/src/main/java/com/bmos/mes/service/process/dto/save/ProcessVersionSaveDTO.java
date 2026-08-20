package com.bmos.mes.service.process.dto.save;

import com.bmos.mes.service.process.dto.RelationBatchRecordItemDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel("新增版本DTO")
public class ProcessVersionSaveDTO {


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


    @ApiModelProperty(value = "版本号",required = true)
    private String version;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;


    @ApiModelProperty(value = "关联批记录",required = true)
    @NotEmpty
    private List<RelationBatchRecordItemDTO> batchRecordItems;


}
