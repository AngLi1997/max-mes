package com.bmos.lims2.server.eln.record.entity.formula;

import cn.hutool.core.collection.CollUtil;
import com.bmos.lims2.common.enums.FormulaValueTakeTypeEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 组件公式配置 不含精度、公式类型、修约、日期类型这几个已有字段配置
 */
@ApiModel("组件公式配置")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ComponentFormulaConfig {

    /**
     * 求和公式:取值方式
     * {@link com.bmos.lims2.common.enums.FormulaValueTakeTypeEnum}
     */
    @ApiModelEnumProperty(value = "取值类型", enumClass = FormulaValueTakeTypeEnum.class)
    private String valueTakeType;

    @ApiModelProperty("数值判定公式配置")
    private List<NumericalJudgmentConfig> numericalJudgmentConfig;

    @ApiModelProperty("日期计算公式配置")
    private DateCalculateConfig dateCalculateConfig;

    @ApiModelProperty("关联引用公式格式配置")
    private AssociationPatternConfig associationPatternConfig;

    @ApiModelProperty("字符串拼接公式配置")
    private StringJoinConfig stringJoinConfig;

    /**
     * 判断数值判定公式的配置是否齐全
     * @return
     */
    public boolean numericalJudgmentConfigIsComplete() {
        if (CollUtil.isEmpty(numericalJudgmentConfig)) {
            return false;
        }
        return numericalJudgmentConfig.stream().allMatch(NumericalJudgmentConfig::configIsComplete);
    }

    /**
     * 判断日期计算公式配置是否齐全
     * @return
     */
    public boolean dateCalculateConfigIsComplete() {
        if (dateCalculateConfig == null) {
            return false;
        }
        return dateCalculateConfig.configIsComplete();
    }

    /**
     * 判断关联公式格式配置是否齐全
     * @return
     */
    public boolean associationPatternConfigIsComplete() {
        if (associationPatternConfig == null) {
            return false;
        }
        return associationPatternConfig.configIsComplete();
    }

    /**
     * 判断字符串拼接公式配置是否齐全
     * @return
     */
    public boolean stringJoinConfigComplete() {
        if (stringJoinConfig == null) {
            return false;
        }
        return stringJoinConfig.configIsComplete();
    }


}
