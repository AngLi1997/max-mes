package com.bmos.mes.service.workflow.change.dto;

import cn.hutool.core.collection.CollUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.mes.common.exception.MesResponseCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName ChangeTeamDTO
 * @Description 换班dto
 * @Author Ren Jin Guang
 * @Date 2024/8/16 11:23
 */
@Setter
@Getter
@ToString
@ApiModel("换班dto")
public class ChangeTeamDTO {

    @ApiModelProperty("执行实例id")
    private String executionId;

    @ApiModelProperty("工序模型id")
    @NotNull
    private Long procedureModelId;

    @ApiModelProperty("工序步骤id")
    @NotNull
    private Long procedureStepModelId;

    @ApiModelProperty("工步节点")
    @NotNull
    private String nodeId;

    @ApiModelProperty("计划id")
    @NotNull
    private Long planId;

    @ApiModelProperty("节点作用")
    @NotNull
    private String nodeFunction;

    @ApiModelProperty("流程实例id")
    private String processInstanceId;

    @ApiModelProperty("换班班次信息")
    @NotEmpty
    private List<TeamListDTO> changeTeamList;

    @ApiModelProperty("工序换班次数")
    @NotNull
    private Integer procedureChangeNumber;

    @ApiModelProperty("工艺换班次数")
    @NotNull
    private Integer processChangeNumber;


    @ApiModelProperty("是否强制完成")
    private Boolean isCoerceComplete;

    @JsonIgnore
    public void validatedChangeTeamList() {
        if (CollUtil.isEmpty(changeTeamList)) {
            throw new BmosException(MesResponseCode.PROCESS_DATE_ERROR);
        }
        for (TeamListDTO teamList : changeTeamList) {
            teamList.validatedTeamIdAndProductInstructionTeamId();
        }
    }
}
