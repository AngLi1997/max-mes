package com.bmos.mes.service.storage.config.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * 货位分页查询参数
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/5 11:51
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("货位分页查询参数")
public class CargoPositionPageQuery extends BasePage {

    /**
     * 暂存间id
     */
    @ApiModelProperty(value = "暂存间id", example = "1", required = true)
    private Long storageId;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称", example = "培养室-盐酸组氨酸货位")
    @Length(max = 100)
    private String position;

    /**
     * 编码
     */
    @ApiModelProperty(value = "编码", example = "KQ-PY-101")
    @Length(max = 100)
    private String code;
}
