package com.bmos.platform.service.system.code.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.platform.common.enums.BooleanEnum;
import com.bmos.platform.common.enums.system.code.RuleTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class CodeRuleUseDetailVO  {
    @ApiModelProperty("类型 常量 CONSTANT 参数 PARAMETER 日期 DATE 流水号 SEQUENCE")
    private RuleTypeEnum type;

    @ApiModelProperty("值 类型为常量使用")
    private String value;

    @ApiModelProperty("参数id")
    private Long parameterId;

    @ApiModelProperty("参数编号")
    private String parameterNo;

    @ApiModelProperty("日期类型前端展示数字 年月等")
    private String dateType;

    @ApiModelProperty("日期格式 yyyyMMdd yyyy-MM-dd yyyy/MM/dd 类似此格式")
    private String dateFormat;

    @ApiModelProperty("开始编号")
    private Long startNo;

    @ApiModelProperty("最大长度")
    private Long maxLength;

    @ApiModelProperty("步长")
    private Long step;

    @ApiModelProperty("是否补零 TRUE FALSE")
    private BooleanEnum fillZero;

    @ApiModelProperty("排序")
    private Long sort;

    @ApiModelProperty("是否展示")
    private Boolean isShow;
}
