package com.bmos.mes.service.tareweigh.config.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/9/23 10:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("皮重配置分页查询条件")
public class TareWeighConfigQuery extends BasePage {

    /**
     * 毛重
     */
    @ApiModelProperty(value = "毛重", example = "10.0")
    @Length(max = 100)
    private String tareWeigh;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述", example = "皮重配置")
    @Length(max = 200)
    private String describeInfo;
}
