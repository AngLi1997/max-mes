package com.bmos.mes.service.process.vo;

import com.bmos.mes.service.process.vo.Task.ExpressionDetailVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel("工序VO")
public class ProcedureVO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("工序id")
    private Long procedureId;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty("历史工序名称")
    private String historicalName;


    @ApiModelProperty(value = "节点id")
    private String nodeId;


    @ApiModelProperty("阶段编码")
    private String stageCode;


    @ApiModelProperty("流程模型Id")
    private String processModelId;

    /**
     * 时长
     */
    @ApiModelProperty("时长")
    private Long duration;

    /**
     * 单位
     */
    @ApiModelProperty("单位")
    private String timeUnit;

    @ApiModelProperty("工序排序号")
    private Integer sort;

    @ApiModelProperty("负责人")
    private Long principal;


    @ApiModelProperty("班组id集合")
    private List<Long> groupIds;

    @ApiModelProperty("配方物料id集合")
    private List<Long> formulaMaterialIdList;

    @ApiModelProperty(value = "房间Path列表", example = "666-777")
    private List<String> roomIdList;

    @ApiModelProperty("完成条件配置")
    private ExpressionDetailVO completeCondition;
}
