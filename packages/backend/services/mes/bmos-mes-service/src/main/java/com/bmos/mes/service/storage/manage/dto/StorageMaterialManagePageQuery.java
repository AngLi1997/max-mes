package com.bmos.mes.service.storage.manage.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

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
public class StorageMaterialManagePageQuery extends BasePage {

    /**
     * 物料批次id
     */
    @ApiModelProperty(value = "物料批次id", example = "1", required = true)
    @NotNull
    private Long storageMaterialBatchId;

    /**
     * 物料件号
     */
    @ApiModelProperty("物料件号")
    @Length(max = 100)
    private String storageMaterialNo;
}
