package com.bmos.mes.service.storage.config.vo;

import com.bmos.common.validate.EnumValidate;
import com.bmos.mes.common.enums.storage.StorageLevelEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 暂存间信息
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/5 11:33
 */
@Data
@ApiModel("暂存间信息")
public class StorageVO {

    /**
     * id
     */
    @ApiModelProperty(value = "id", example = "1")
    private Long id;

    /**
     * 上级区域id
     */
    @ApiModelProperty(value = "父级id", example = "1")
    private Long parentId;

    /**
     * 区域名称
     */
    @ApiModelProperty(value = "区域名称", example = "培养室暂存间")
    private String name;

    /**
     * 层级
     */
    @ApiModelProperty(value = "层级", example = "1")
    @ApiModelEnumProperty(value = "层级信息类型", required = true, enumClass = StorageLevelEnum.class)
    @EnumValidate(value = StorageLevelEnum.class)
    private StorageLevelEnum level;

    /**
     * 货位编码
     */
    @ApiModelProperty(value = "货位编码", example = "KQ-PY-101")
    private String positionCode;

    /**
     * 子节点列表
     */
    @ApiModelProperty(value = "子节点列表")
    private List<StorageVO> children = new ArrayList<>();
}
