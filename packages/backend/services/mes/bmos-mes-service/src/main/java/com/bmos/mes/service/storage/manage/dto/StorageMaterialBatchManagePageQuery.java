package com.bmos.mes.service.storage.manage.dto;

import com.bmos.common.validate.EnumValidate;
import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * 物料管理批次分页查询参数
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/5 11:51
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("物料管理批次分页查询参数")
public class StorageMaterialBatchManagePageQuery extends BasePage {

    /**
     * 物料类型
     */
    @ApiModelProperty("物料类型")
    @EnumValidate(value = CategoryInfoTypeEnum.class)
    private Integer categoryType;

    /**
     * 物料id
     */
    @ApiModelProperty(value = "物料id", example = "1")
    private Long materialId;

    /**
     * 物料批号
     */
    @ApiModelProperty(value = "物料批号", example = "WH030102231001")
    @Length(max = 100)
    private String materialBatchNo;
}
