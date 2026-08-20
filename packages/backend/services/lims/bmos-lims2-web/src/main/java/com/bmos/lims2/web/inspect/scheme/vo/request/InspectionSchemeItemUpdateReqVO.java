package com.bmos.lims2.web.inspect.scheme.vo.request;

import com.bmos.lims2.common.enums.ItemDurationUnitEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;

/**
 * @Description: 检验项目配置编辑保存请求VO
 * @Author: yigaohui
 * @Date: 2025/01/21 17:00
 */
@Data
@ApiModel("检验项目配置编辑保存请求")
public class InspectionSchemeItemUpdateReqVO {

    @ApiModelProperty("检验项目配置ID")
    private Long itemConfigId;

    /**
     * 关联的方案ID
     */
    @ApiModelProperty("关联的方案ID")
    private Long schemeId;

    /**
     * 关联的版本ID
     */
    @ApiModelProperty("关联的版本ID")
    private Long versionId;

    /**
     * 实验包id
     */
    @ApiModelProperty("实验包ID")
    private Long packageId;

    /**
     * 检验项目ID
     */
    @ApiModelProperty("检验项目ID")
    private Long inspectItemId;

    /**
     * 是否必检
     */
    @ApiModelProperty("是否必检")
    private Boolean isRequired;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;


    @ApiModelProperty("时长")
    private Integer duration;

    @ApiModelProperty("时长单位")
    private ItemDurationUnitEnum timeUnit;

    @ApiModelProperty("分析项配置列表")
    @Valid
    private List<InspectParameterConfigReqVO> inspectionParameters;

    @ApiModelProperty("班组ID列表")
    private List<Long> teams;

    /**
     * 分析项配置请求VO
     */
    @Data
    @ApiModel("分析项配置-更新")
    public static class InspectParameterConfigReqVO {
        @ApiModelProperty("分析项配置ID（修改时需要）")
        private Long parameterConfigId;

        /**
         * 关联的方案ID
         */
        @ApiModelProperty("关联的方案ID")
        private Long schemeId;

        /**
         * 关联的版本ID
         */
        @ApiModelProperty("关联的版本ID")
        private Long versionId;

        @ApiModelProperty("关联的实验包ID")
        private Long packageId;


        @ApiModelProperty("关联的检验项目ID")
        private Long inspectItemId;

        /**
         * 关联的检验项目配置ID
         */
        @ApiModelProperty("关联的检验项目配置ID")
        private Long itemConfigId;

        /**
         * 分析项ID
         */
        @ApiModelProperty("分析项ID")
        private Long parameterId;

        @ApiModelProperty("分析方法ID")
        private Long recordId;

        @ApiModelProperty("分析方法编码")
        private String recordCode;

        @ApiModelProperty("分析方法版本ID")
        private Long recordVersionId;

        @ApiModelProperty("分析项名称")
        private String parameterName;

        @ApiModelProperty("分析项编码")
        private String parameterCode;
        /**
         * 标准规定
         */
        @ApiModelProperty("标准规定")
        private String standardRule;

        /**
         * 是否报告项
         */
        @ApiModelProperty("是否报告项")
        private Boolean isReportable;

        /**
         * 是否可执行
         */
        @ApiModelProperty("是否可执行")
        private Boolean isExecutable;
    }
}
