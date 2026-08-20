package com.bmos.mes.service.plan.info.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ApiModel("生产审核进度分页查询DTO")
@Data
public class ProductionAuditProgressQueryDTO extends BasePage {

    @ApiModelProperty("生产批号")
    private String batchNo;

    @ApiModelProperty("产品或分类id")
    @NotNull
    private Long id;

    @ApiModelProperty("生产工艺名称")
    private String processName;

    @ApiModelProperty("分类标识")
    @NotNull
    private Boolean categoryFlag;

    @ApiModelProperty(value = "部门列表", hidden = true)
    private List<Long> deptIds;

    @ApiModelProperty(value = "产品id列表", hidden = true)
    private List<Long> productIds;


}
