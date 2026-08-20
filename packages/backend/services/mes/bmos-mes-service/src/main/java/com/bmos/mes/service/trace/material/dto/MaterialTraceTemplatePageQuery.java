package com.bmos.mes.service.trace.material.dto;

import com.bmos.mybatis.page.BasePage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

/**
 * 物料模板查询
 * @author liang
 * @version 1.0.0
 * @date 2024/11/19 15:11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("物料模板分页查询query")
public class MaterialTraceTemplatePageQuery extends BasePage {

    @ApiModelProperty(value = "产品id", example = "1")
    private Long productId;

    @ApiModelProperty(value = "物料类别id", example = "1")
    private Long categoryId;

    @ApiModelProperty(value = "模板名称", example = "模板名称")
    @Length(max = 100)
    private String templateName;

    @ApiModelProperty(value = "工艺名称", example = "工艺名称")
    @Length(max = 100)
    private String processName;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private List<Long> materialIds = new ArrayList<>();

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private List<Long> processIds = new ArrayList<>();
}
