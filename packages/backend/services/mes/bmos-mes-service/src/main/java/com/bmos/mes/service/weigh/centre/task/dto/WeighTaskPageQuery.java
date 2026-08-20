package com.bmos.mes.service.weigh.centre.task.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 称量任务分页查询参数
 * @author liang
 * @version 1.0.0
 * @date 2024/7/8 17:40
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("称量任务分页查询参数")
public class WeighTaskPageQuery extends BasePage {

    @ApiModelProperty(value = "称量任务编号", example = "1")
    @Length(max = 100)
    private String taskNo;

    @ApiModelProperty(value = "物料名称", example = "氯化钠")
    @Length(max = 100)
    private String materialName;

    @ApiModelProperty(value = "物料编码", example = "WH03")
    @Length(max = 100)
    private String materialMergeCode;

    @ApiModelProperty(value = "称量中心名称/编码", example = "狂犬疫苗配液称量中心")
    @Length(max = 100)
    private String weighCentre;

    @ApiModelProperty(value = "执行时间开始", example = "2024-07-01")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate executeDateStart;

    @ApiModelProperty(value = "需求时间结束", example = "2024-07-01")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate executeDateEnd;
}
