package com.bmos.platform.service.factory.service.dto;

import com.bmos.platform.facade.factory.enums.RoomStatusEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 房间保存DTO
 */
@Getter
@Setter
@ApiModel("房间保存入参")
public class RoomSaveDTO {

    /**
     * 房间模型id
     */
    @ApiModelProperty(value = "房间模型id", required = true)
    @NotNull
    private Long moduleId;

    /**
     * 编码
     */
    // 在当前类中构建多个属性，属性为：编码、名称、有效时间、描述，并生成swagger对应注解
    @ApiModelProperty(value = "编码", required = true)
    @NotBlank
    @Length(max = 100)
    private String code;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称", required = true)
    @NotBlank
    @Length(max = 100)
    private String name;

    @ApiModelProperty(value = "清洁时限（单位为h）", required = true)
    @NotBlank
    @Length(max = 6)
    private String timeLimit;

    @ApiModelProperty("描述")
    @Length(max = 200)
    private String description;

    @ApiModelProperty(value = "部门id集合 数据权限", required = true)
    @NotNull
    private List<Long> deptIds;

    @ApiModelProperty("楼层id")
    private Long floorId;

    @ApiModelProperty("楼栋id")
    private Long tenementId;

    @ApiModelProperty("洁净等级")
    private String cleanLevel;
}
