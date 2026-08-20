package com.bmos.lims2.server.eln.record.entity.formula;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("字符串拼接公式配置")
@Data
public class StringJoinConfig implements FormulaConfig{

    @ApiModelProperty("连接符")
    private String join;

    @Override
    public boolean configIsComplete() {
        return StrUtil.isNotEmpty(join);
    }


    @Override
    public String calculateResult(List<String> inputs) {
        if (CollUtil.isEmpty(inputs)) {
            return null;
        }
        return StrUtil.join(join, inputs);
    }
}
