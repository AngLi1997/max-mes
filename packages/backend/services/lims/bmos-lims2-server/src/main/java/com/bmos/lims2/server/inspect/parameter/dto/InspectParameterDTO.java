package com.bmos.lims2.server.inspect.parameter.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.lims2.common.enums.AnalyzeResultTypeEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 分析项(BmExperimentAnalyze)实体类
 *
 * @author makejava
 * @since 2024-03-02 12:43:37
 */
@Getter
@Setter
@ApiModel("分析项DTO")
public class InspectParameterDTO extends BaseDO {

    /**
     * 分析项编码
     */
    @ApiModelProperty(value = "分析项编码", required = true)
    private String code;
    /**
     * 分析项名称
     */
    @ApiModelProperty(value = "分析项名称", required = true)
    private String name;
    /**
     * 当前分析项默认标准规定
     */
    @ApiModelProperty(value = "当前分析项默认标准规定")
    private String standard;

    /**
     * 数据点列表
     */
    @ApiModelProperty(value = "数据点列表", required = true)
    @NotEmpty(message = "数据点列表不能为空")
    @Valid
    private List<InspectParameterDataPointDTO> dataPoints;
}

