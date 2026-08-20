package com.bmos.mes.service.workflow.change.dto;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.mes.common.exception.MesResponseCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName TeamListDTO
 * @Description 班组集合dto
 * @Author Ren Jin Guang
 * @Date 2024/8/16 13:40
 */
@Setter
@Getter
@ToString
@ApiModel(value = "添加换班班次信息dto")
public class TeamListDTO {

    @ApiModelProperty("生产计划指令单班组id")
    @NotNull
    private Long productInstructionTeamId;

    @ApiModelProperty("班次id")
    @NotBlank
    private List<Long> teamIds;

    @JsonIgnore
    public void validatedTeamIdAndProductInstructionTeamId() {
        if (ObjectUtil.isNull(productInstructionTeamId) || CollUtil.isEmpty(teamIds)) {
            throw new BmosException(MesResponseCode.PROCESS_DATE_ERROR);
        }
    }
}
