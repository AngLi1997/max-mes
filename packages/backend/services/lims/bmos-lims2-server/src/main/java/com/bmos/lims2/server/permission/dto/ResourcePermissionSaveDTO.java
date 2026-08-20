package com.bmos.lims2.server.permission.dto;

import com.bmos.lims2.common.enums.PermissionModuleEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Tolerate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel("数据权限保存DTO")
@Builder
public class ResourcePermissionSaveDTO {

    @Tolerate
    public ResourcePermissionSaveDTO(){}

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
    @NotEmpty
    private List<Long> deptIds;

    @ApiModelEnumProperty(value = "资源模块", required = true, enumClass = PermissionModuleEnum.class)
    @NotEmpty
    private String module;
}
