package com.bmos.mes.service.weigh.centre.config.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 新增称量中心DTO
 * @author liang
 * @version 1.0.0
 * @date 2024/7/3 17:09
 */
@Data
@ApiModel("新增称量中心分页查询参数")
public class WeighCentrePageQuery extends BasePage {

    @ApiModelProperty(value = "称量中心分类id", example = "1")
    private Long categoryId;

    @ApiModelProperty(value = "称量中心名称", example = "称量中心名称")
    @Length(max = 100)
    private String name;

    @ApiModelProperty(value = "称量中心编码", example = "KQ-PY-101")
    @Length(max = 100)
    private String code;
}
