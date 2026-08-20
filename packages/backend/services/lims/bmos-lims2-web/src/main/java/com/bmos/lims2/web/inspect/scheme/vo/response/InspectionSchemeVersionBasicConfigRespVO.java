package com.bmos.lims2.web.inspect.scheme.vo.response;

import com.bmos.lims2.common.enums.InspectionSchemeVersionStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 检验方案版本基础信息响应VO
 */
@Data
@ApiModel("检验方案版本基础信息响应")
public class InspectionSchemeVersionBasicConfigRespVO {

    @ApiModelProperty("版本ID")
    private Long id;

    @ApiModelProperty("关联的检验方案ID")
    private Long schemeId;

    @ApiModelProperty("方案名称")
    private String schemeName;

    @ApiModelProperty("方案描述")
    private String description;

    @ApiModelProperty("版本号")
    private String versionNo;

    @ApiModelProperty("版本状态")
    private InspectionSchemeVersionStatusEnum status;

    @ApiModelProperty("父版本ID")
    private Long parentVersionId;

    @ApiModelProperty("父版本号")
    private String parentVersionNo;

    @ApiModelProperty("生效日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate effectiveDate;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("物料信息")
    private MaterialInfoRespVO material;

    @ApiModelProperty("实验包信息")
    private PackageInfoRespVO packageInfo;

    @Data
    @ApiModel("物料信息")
    public static class MaterialInfoRespVO {
        @ApiModelProperty("物料ID")
        private Long materialId;

        @ApiModelProperty("物料名称")
        private String materialName;

        @ApiModelProperty("物料编码")
        private String materialCode;

        @ApiModelProperty("物料单位ID")
        private Long unitId;

        @ApiModelProperty("物料单位名称")
        private String unitName;
    }

    @Data
    @ApiModel("实验包信息")
    public static class PackageInfoRespVO {
        @ApiModelProperty("实验包ID")
        private Long packageId;

        @ApiModelProperty("实验包名称")
        private String packageName;

        @ApiModelProperty("实验包编码")
        private String packageCode;
    }
}
