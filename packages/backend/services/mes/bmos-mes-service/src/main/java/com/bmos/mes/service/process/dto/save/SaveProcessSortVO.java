package com.bmos.mes.service.process.dto.save;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName SaveProcessSortVO
 * @Description 添加工艺排序dto
 * @Author Ren Jin Guang
 * @Date 2024/8/26 16:14
 */
@Setter
@Getter
@ToString
@ApiModel("添加工艺排序dto")
public class SaveProcessSortVO {

    @ApiModelProperty("工序id")
    @NotNull
    private Long id;

    @ApiModelProperty("工序名称")
    @NotBlank
    private String name;

    @ApiModelProperty("工序排序号")
    @NotNull
    private Integer sort;

    @ApiModelProperty("工序步骤排序信息")
    private List<SaveProcessSortVO> procedureStepSortList;



}
