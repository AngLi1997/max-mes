package com.bmos.platform.service.system.code.dto;

import cn.hutool.core.util.StrUtil;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.exception.BmosException;
import com.bmos.common.validate.EnumValidate;
import com.bmos.platform.common.enums.BooleanEnum;
import com.bmos.platform.common.enums.StatusEnum;
import com.bmos.platform.common.enums.system.code.RuleTypeEnum;
import com.bmos.platform.common.exception.PlatformResponseCode;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * 编码规则主表
 */
@Getter
@Setter
@ApiModel("CodeRuleVersionDetailSaveDTO:编码规则保存DTO")
public class CodeRuleVersionDetailSaveDTO {
    @NotBlank
    @EnumValidate(value = RuleTypeEnum.class)
    @ApiModelProperty(value = "类型 常量 CONSTANT 参数 PARAMETER 日期 DATE 流水号 SEQUENCE", required = true)
    private String type;

    @ApiModelProperty("值 类型为常量使用")
    private String value;

    @ApiModelProperty("参数id")
    private Long parameterId;

    @ApiModelProperty("日期类型前端展示数字 年月等")
    private String dateType;

    @ApiModelProperty("日期格式 yyyyMMdd yyyy-MM-dd yyyy/MM/dd 类似此格式")
    private String dateFormat;

    @ApiModelProperty("开始编号")
    private Integer startNo;

    @ApiModelProperty("最大长度")
    private Integer maxLength;

    @ApiModelProperty("步长")
    private Integer step;

    @ApiModelProperty("是否补零 TRUE FALSE")
    private String fillZero;

    @NotNull
    @ApiModelProperty(value = "排序", required = true)
    private Integer sort;

    @ApiModelProperty("是否展示：是true,否：false")
    @ApiModelEnumProperty(value = "是否展示", enumClass = StatusEnum.class)
    private Boolean isShow;

    public void isShowNotBoolean() {
        if (type.equals(RuleTypeEnum.PARAMETER.getValue())) {
            if (StrUtil.isBlankIfStr(isShow)) {
                throw new BmosException(PlatformResponseCode.PARAMETER_NOT_EMPTY);
            }
        }
    }

    @JsonIgnore
    public boolean isTrue() {
        RuleTypeEnum ruleTypeEnum = CommonEnum.getEnumByValue(RuleTypeEnum.class, getType());
        if (Objects.isNull(ruleTypeEnum)) {
            throw new BmosException(PlatformResponseCode.TYPE_NOT_ALLOW);
        }
        if (RuleTypeEnum.CONSTANT == ruleTypeEnum) {
            if (StrUtil.isEmpty(getValue())) {
                throw new BmosException(PlatformResponseCode.CONSTANT_NOT_EMPTY);
            }
            return Boolean.TRUE;
        }
        if (RuleTypeEnum.PARAMETER == ruleTypeEnum) {
            if (Objects.isNull(getParameterId())) {
                throw new BmosException(PlatformResponseCode.PARAMETER_NOT_EMPTY);
            }
            return Boolean.TRUE;
        }
        if (RuleTypeEnum.DATE == ruleTypeEnum) {
            if (StrUtil.isEmpty(getDateFormat()) || StrUtil.isEmpty(getDateType())) {
                throw new BmosException(PlatformResponseCode.DATE_NOT_EMPTY);
            }
            return Boolean.TRUE;
        }
        if (RuleTypeEnum.SEQUENCE == ruleTypeEnum) {
            if (Objects.isNull(getStartNo()) || Objects.isNull(getMaxLength())
                    || Objects.isNull(CommonEnum.getEnumByValue(BooleanEnum.class, getFillZero()))
                    || Objects.isNull(getStep())
            ) {
                throw new BmosException(PlatformResponseCode.SEQUENCE_NOT_EMPTY);
            }
            return Boolean.TRUE;
        }
        return Boolean.TRUE;
    }
}
