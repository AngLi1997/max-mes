package com.bmos.wms.service.platform.permission.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Tolerate;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel("数据权限保存DTO")
@Builder
public class ResourcePermissionSaveDTO {

    @Tolerate
    public ResourcePermissionSaveDTO() {
    }

    /**
     * 资源id
     */
    @ApiModelProperty("资源id")
    @NotNull
    private Long resourceId;


    /**
     * 部门id
     */
    @ApiModelProperty("部门id集合")
    @NotNull
    private List<Long> deptIds;
}
