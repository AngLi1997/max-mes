package com.bmos.platform.service.permission.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Tolerate;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel("数据权限保存DTO")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourcePermissionSaveDTO {

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
