package com.bmos.mes.service.process.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @ClassName ProcessSortVO
 * @Description 工艺排序vo
 * @Author Ren Jin Guang
 * @Date 2024/8/26 16:14
 */
@Setter
@Getter
@ToString
@ApiModel("工艺排序返回vo")
public class ProcessSortVO {

    @ApiModelProperty("工序id")
    private Long id;

    @ApiModelProperty("工序名称")
    private String name;

    @ApiModelProperty("工序排序号")
    private Integer sort;

    @ApiModelProperty("工序模型id")
    private Long procedureModelId;

    @ApiModelProperty("工序步骤排序信息")
    private List<ProcessSortVO> procedureStepSortList;



}
