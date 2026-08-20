package com.bmos.lims2.web.stability.scheme.vo.response;

import com.bmos.lims2.common.enums.StabilitySchemeVersionStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 稳定性方案版本完整配置响应VO
 */
@Data
@ApiModel("稳定性方案版本完整配置响应")
public class StabilitySchemeVersionFullConfigRespVO {

    @ApiModelProperty("版本ID")
    private Long id;

    @ApiModelProperty("方案ID")
    private Long schemeId;

    @ApiModelProperty("方案编码")
    private String schemeCode;

    @ApiModelProperty("方案名称")
    private String schemeName;

    @ApiModelProperty("版本描述")
    private String description;

    @ApiModelProperty("生效日期")
    private LocalDate effectiveDate;

    @ApiModelProperty("版本号")
    private String versionNo;

    @ApiModelProperty("版本状态")
    private StabilitySchemeVersionStatusEnum status;

    @ApiModelProperty("父版本ID")
    private Long parentVersionId;

    @ApiModelProperty("父版本号")
    private String parentVersionNo;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("检品信息")
    private MaterialInfoRespVO material;

    @ApiModelProperty("检验项目配置列表")
    private List<StabilitySchemeItemRespVO> inspectionItems;

    @ApiModelProperty("检验计划列表")
    private List<StabilitySchemePlanRespVO> plans;

    @Data
    public static class MaterialInfoRespVO {
        @ApiModelProperty("检品ID")
        private Long materialId;
        @ApiModelProperty("检品名称")
        private String materialName;
        @ApiModelProperty("检品编码")
        private String materialCode;
    }
}
