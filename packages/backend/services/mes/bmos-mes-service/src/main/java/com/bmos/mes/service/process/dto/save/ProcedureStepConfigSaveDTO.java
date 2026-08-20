package com.bmos.mes.service.process.dto.save;

import com.bmos.mes.service.process.dto.ComponentConfigDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel("工序步骤记录项配置保存DTO")
public class ProcedureStepConfigSaveDTO {

    /**
     * 工序步骤模型id
     */
    @ApiModelProperty("工序步骤模型id")
    private Long procedureStepModelId;

    /**
     * 工序步骤id
     */
    @ApiModelProperty("工序步骤id")
    @NotNull
    private Long procedureStepId;

    /**
     * 流程节点Id
     */
    @ApiModelProperty("流程节点Id")
    @NotNull
    private String nodeId;

    /**
     * 工艺id
     */
    @ApiModelProperty("工序id")
    @NotNull
    private Long processId;

    /**
     * 工艺版本号
     */
    @ApiModelProperty("工艺版本号")
    @NotNull
    private String version;

    /**
     * 记录项id
     */
    @ApiModelProperty("记录项id")
    @NotNull
    private Long recordItemId;

    @ApiModelProperty("记录项版本id")
    private Long recordVersionId;

    @ApiModelProperty("组件信息")
    private List<ComponentConfigDTO> components;


    @ApiModelProperty("是否复用")
    @NotNull
    private Boolean reusable;
}
