package com.bmos.mes.service.weigh.centre.task.vo;

import com.bmos.mes.service.weigh.centre.requirement.vo.WeighRequirementVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 带有称量需求分页的称量任务详情vo
 * @author liang
 * @version 1.0.0
 * @date 2024/7/9 15:56
 */
@Data
@ApiModel("带有称量需求分页的称量任务详情vo")
public class WeighTaskAndRequirementPageVO {

    @ApiModelProperty(value = "称量任务id", example = "1")
    private Long id;

    @ApiModelProperty(value = "称量任务编号", example = "20240606001")
    private String taskNo;

    @ApiModelProperty(value = "执行时间", example = "2024-07-09")
    private LocalDate executeDate;

    @ApiModelProperty(value = "称量中心id", example = "1")
    private Long weighCentreId;

    @ApiModelProperty(value = "物料名称", example = "氯化钠")
    private String materialName;

    @ApiModelProperty(value = "物料编码", example = "WH03")
    private String materialMergeCode;

    @ApiModelProperty(value = "物料规格", example = "1个")
    private String materialSpecification;

    @ApiModelProperty(value = "称量中心名称", example = "狂犬疫苗配液称量中心")
    private String weighCentreName;

    @ApiModelProperty(value = "称量中心编码", example = "KQ-PYCL")
    private String weighCentreCode;

    @ApiModelProperty(value = "称量需求分页")
    private List<WeighRequirementVO> requirementList;
}
