package com.bmos.mes.service.lotsummary.vo;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 批次摘要生产数据分页数据
 * @author liang
 * @version 1.0.0
 * @date 2024/9/5 11:20
 */
@Data
@ApiModel("批次摘要生产数据分页数据")
public class LotSummaryProductDataPageVO {

    @ApiModelProperty(value = "生产计划id", example = "1")
    private Long planId;

    @ApiModelProperty(value = "批号", example = "123")
    private String batchNo;

    @ApiModelProperty(value = "动态数据json")
    @JsonIgnore
    private String dataJson;

    @ApiModelProperty(value = "动态列")
    @Valid
    private List<FieldInfo> data;

    @Data
    @ApiModel("批次摘要动态列字段vo")
    public static final class FieldInfo {

        /**
         * 字段名
         */
        @ApiModelProperty(value = "字段id", example = "123")
        private Long fieldId;

        /**
         * 工步id
         */
        @ApiModelProperty(value = "工步id", example = "123")
        private Long procedureStepId;

        /**
         * 值
         */
        @ApiModelProperty(value = "值", example = "123")
        private String value;
    }

    public @Valid List<FieldInfo> getData() {
        if (StrUtil.isBlank(dataJson)){
            return new ArrayList<>();
        }
        return JSON.parseArray(dataJson, FieldInfo.class);
    }
}
