package com.bmos.lims2.web.stability.scheme.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 稳定性方案列表响应VO（物料信息平铺）
 */
@Data
@ApiModel("稳定性方案列表响应")
public class StabilitySchemeListRespVO {

    @ApiModelProperty("方案ID")
    private Long id;

    @ApiModelProperty("方案名称")
    private String name;

    @ApiModelProperty("方案编码")
    private String code;

    @ApiModelProperty("检品ID")
    private Long materialId;

    @ApiModelProperty("检品名称")
    private String materialName;

    @ApiModelProperty("检品编码")
    private String materialCode;

    @ApiModelProperty("当前生效版本号")
    private String activeVersionNo;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("数据权限部门ID集合")
    private List<Long> deptIds;
}
