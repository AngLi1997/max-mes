package com.bmos.lims2.web.inspect.item.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import com.bmos.lims2.web.inspect.parameter.vo.resp.InspectParameterDataPointRespVO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 检验项目详情响应VO - 包含完整的检验项目和分析项信息
 */
@Getter
@Setter
@ApiModel("检验项目详情响应VO")
public class InspectItemDetailRespVO {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "检验项目编码")
    private String code;

    @ApiModelProperty(value = "检验项目名称")
    private String name;

    @ApiModelProperty(value = "检验项目描述")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "当前检验项下的分析项列表")
    private List<InspectItemAnalysisItemVO> parameterList;

    /**
     * 检验项目下的分析项信息VO
     */
    @Getter
    @Setter
    @ApiModel("检验项目下的分析项信息VO")
    public static class InspectItemAnalysisItemVO {

        @ApiModelProperty(value = "分析项ID")
        private Long inspectParameterId;

        @ApiModelProperty(value = "分析项编码")
        private String code;

        @ApiModelProperty(value = "分析项名称")
        private String name;

        @ApiModelProperty(value = "标准规定")
        private String standard;

        @ApiModelProperty(value = "数据点列表")
        private List<InspectParameterDataPointRespVO> dataPoints;
    }
}