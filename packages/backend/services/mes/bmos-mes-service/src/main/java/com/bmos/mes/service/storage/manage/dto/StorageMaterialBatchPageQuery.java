package com.bmos.mes.service.storage.manage.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * 暂存物料批次分页查询参数
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/5 11:51
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("暂存物料批次分页查询参数")
public class StorageMaterialBatchPageQuery extends BasePage {

    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称", example = "培养室-盐酸组氨酸货位")
    @Length(max = 100)
    private String materialName;

    /**
     * 编码
     */
    @ApiModelProperty(value = "编码", example = "KQ-PY-101")
    @Length(max = 100)
    private String mergeCode;

    /**
     * 物料批号
     */
    @ApiModelProperty(value = "物料批号", example = "WH030102231001")
    @Length(max = 100)
    private String materialBatchNo;

    /**
     * 暂存货位/暂存间id
     */
    @ApiModelProperty(value = "暂存货位/暂存间id(不传查询所有)", example = "1")
    private Long materialPositionId;
}
