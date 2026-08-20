package com.bmos.mes.service.workflow.dto;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@ApiModel("App生产历史DTO")
@Data
public class AppPlanHistoryDTO extends BasePage {

    @ApiModelProperty("生产批号")
    private String batchNo;

    @ApiModelProperty(value = "产品Id字符串", example = "666,777")
    private String productId;

    @ApiModelProperty(value = "产线id", example = "111,222")
    private String lineId;

    @ApiModelProperty(value = "权限过滤后计划id", hidden = true)
    private Set<Long> teamPlanIdList;

    public List<Long> getProductIdList() {
        List<String> split = StrUtil.split(productId, StrUtil.COMMA);
        if (StrUtil.isBlank(productId) || CollUtil.isEmpty(split)) {
            return new ArrayList<>();
        }
        return split.stream().map(Long::valueOf).collect(Collectors.toList());
    }

    public List<Long> getLineIdList() {
        List<String> split = StrUtil.split(lineId, StrUtil.COMMA);
        if (StrUtil.isBlank(lineId) || CollUtil.isEmpty(split)) {
            return new ArrayList<>();
        }
        return split.stream().map(Long::valueOf).collect(Collectors.toList());
    }
}
