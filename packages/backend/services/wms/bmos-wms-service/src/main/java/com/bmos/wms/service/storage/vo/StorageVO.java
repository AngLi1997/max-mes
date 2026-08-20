package com.bmos.wms.service.storage.vo;

import com.bmos.common.validate.EnumValidate;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import com.bmos.wms.common.enums.inventory.StorageLevelEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 存储区域信息
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/5 11:33
 */
@Data
@ApiModel("存储区域信息")
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
    @ApiModelProperty(value = "区域名称", example = "培养室存储区域")
    private String name;

    /**
     * 层级
     */
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
