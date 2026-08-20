package com.bmos.mes.service.process.dto;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.process.dto.task.ConditionSaveDTO;
import com.bmos.mes.service.process.dto.task.ExpressionSaveDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@ApiModel("工序DTO")
public class ProcedureDTO {

    @ApiModelProperty("id")
    private Long id;


    @ApiModelProperty("工序id")
    private Long procedureId;


    @ApiModelProperty("阶段编码")
    private String stageCode;

    /**
     * 名称 工序实际展示名称
     */
    @ApiModelProperty(value = "名称",required = true)
    private String name;

    /**
     * 历史工序名称
     * 如果未传工序id 以该名称创建新历史工序
     */
    @ApiModelProperty(value = "历史工序名称", required = true)
    private String historicalName;

    /**
     * 时长
     */
    @ApiModelProperty("时长")
    private Long duration;

    /**
     * 流程节点id
     */
    @ApiModelProperty(value = "流程节点id",required = true)
    private String nodeId;

    /**
     * 单位
     */
    @ApiModelProperty("单位")
    private String timeUnit;

    @ApiModelProperty("负责人")
    private Long principal;


    @ApiModelProperty("班组id集合")
    private List<Long> groupIds;

    @ApiModelProperty("配方物料id集合")
    private List<Long> formulaMaterialIdList;

    @ApiModelProperty("产线房间path")
    private List<String> roomIdList;

    @ApiModelProperty("完成条件")
    private ExpressionSaveDTO completeCondition;

    @JsonIgnore
    public void validatedPrincipalAndGroupIds() {
        if (CollUtil.isEmpty(groupIds) || Objects.isNull(principal) || StrUtil.isEmpty(name) || StrUtil.isEmpty(nodeId) || StrUtil.isEmpty(historicalName)) {
            throw new BmosException(MesResponseCode.PROCESS_NOT_FINISH);
        }
        if (ObjectUtil.isNotEmpty(completeCondition)){
            for (ConditionSaveDTO conditionSaveDTO : completeCondition.getConditionList()) {
                if (ObjectUtil.isEmpty(conditionSaveDTO)){
                    throw new BmosException(MesResponseCode.PROCESS_NOT_FINISH);
                }
            }
        }
    }
}
