package com.bmos.mes.service.weigh.centre.execute.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * 称量执行任务分页查询参数
 * @author liang
 * @version 1.0.0
 * @date 2024/7/10 10:14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("称量执行任务分页查询参数")
public class WeighExecuteTaskPageQuery extends BasePage {

    @ApiModelProperty(value = "称量任务编号", example = "1")
    @Length(max = 100)
    private String taskNo;

    @ApiModelProperty(value = "物料名称/编码", example = "氯化钠")
    @Length(max = 100)
    private String material;

    @ApiModelProperty(value = "称量中心名称/编码", example = "狂犬疫苗配液称量中心")
    @Length(max = 100)
    private String weighCentre;
}
