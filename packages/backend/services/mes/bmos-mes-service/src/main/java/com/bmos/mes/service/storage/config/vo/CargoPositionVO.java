package com.bmos.mes.service.storage.config.vo;

import com.bmos.mes.common.enums.BooleanEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 货位信息
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/5 11:04
 */
@Data
@ApiModel("货位信息VO")
public class CargoPositionVO {

    /**
     * id
     */
    @ApiModelProperty(value = "id", example = "1")
    private Long id;

    /**
     * 暂存货位
     */
    @ApiModelProperty(value = "暂存货位", example = "培养室-盐酸组氨酸货位")
    private String position;

    /**
     * 所属区域(路径)
     */
    @ApiModelProperty(value = "所属区域(路径)", example = "狂犬病毒疫苗车间/生产区域/培养室暂存间")
    private String path;

    /**
     * 所属区域id路径
     */
    @ApiModelProperty(value = "所属区域id路径", example = "1/2/3")
    private String idPath;

    /**
     * 货位编码
     */
    @ApiModelProperty(value = "货位编码", example = "KQ-PY-101")
    private String code;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", example = "备注")
    private String remark;

    /**
     * 启停
     */
    @ApiModelProperty(value = "启停", example = "true", notes = "true 启用 false 停用")
    private BooleanEnum enable;
}
