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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@ApiModel("工序DTO")
public class ProcedureCopyDTO {

    @ApiModelProperty("id")
    private Long id;


    @ApiModelProperty("流程模型id")
    private String processModelId;

    @ApiModelProperty("工序id")
    private Long procedureId;

    @ApiModelProperty("阶段编码")
    private String stageCode;

    @ApiModelProperty("工序排序号")
    private Integer sort;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称",required = true)
    @NotBlank
    private String name;

    /**
     * 时长
     */
    @ApiModelProperty("时长")
    private Long duration;

    /**
     * 流程节点id
     */
    @ApiModelProperty(value = "流程节点id",required = true)
    @NotBlank
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

    @ApiModelProperty("物料id集合")
    private List<Long> formulaMaterialIdList;

    @ApiModelProperty("房间id集合")
    private List<String> roomIdList;

    @ApiModelProperty("工序完成条件配置")
    private ExpressionSaveDTO completeCondition;

    @JsonIgnore
    public void validatedPrincipalAndGroupIds() {
        if (CollUtil.isEmpty(groupIds) || Objects.isNull(principal) || StrUtil.isEmpty(name) || StrUtil.isEmpty(nodeId)) {
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
