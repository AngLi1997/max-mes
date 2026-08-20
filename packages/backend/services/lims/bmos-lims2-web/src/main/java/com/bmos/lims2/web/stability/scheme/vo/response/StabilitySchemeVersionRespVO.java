package com.bmos.lims2.web.stability.scheme.vo.response;

import com.bmos.lims2.common.enums.StabilitySchemeVersionStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 稳定性方案版本响应VO
 *
 * @author makejava
 * @since 2025-03-17 10:00:00
 */
@Data
@ApiModel("稳定性方案版本响应")
public class StabilitySchemeVersionRespVO {

    @ApiModelProperty("版本ID")
    private Long id;

    @ApiModelProperty("方案ID")
    private Long schemeId;

    @ApiModelProperty("方案名称")
    private String schemeName;

    @ApiModelProperty("方案编码")
    private String schemeCode;

    @ApiModelProperty("版本号")
    private String versionNo;

    @ApiModelProperty("版本状态")
    private StabilitySchemeVersionStatusEnum status;

    @ApiModelProperty("状态名称")
    private String statusName;

    @ApiModelProperty("版本描述")
    private String description;

    @ApiModelProperty("生效日期")
    private LocalDate effectiveDate;

    @ApiModelProperty("父版本ID")
    private Long parentVersionId;

    @ApiModelProperty("父版本号")
    private String parentVersionNo;

    @ApiModelProperty("检品信息")
    private StabilitySchemeVersionRespVO.MaterialInfoRespVO material;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("数据权限部门ID列表")
    private List<Long> deptIds;

    @ApiModelProperty("审批流程实例ID")
    private String processInstanceId;


    @Data
    @ApiModel("检品信息")
    public static class MaterialInfoRespVO {
        @ApiModelProperty("检品ID")
        private Long materialId;

        @ApiModelProperty("检品名称")
        private String materialName;

        @ApiModelProperty("检品编码")
        private String materialCode;
    }
}
