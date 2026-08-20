package com.bmos.mes.service.storage.manage.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * 暂存物料件分页查询参数
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/5 11:51
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("暂存物料件分页查询参数")
public class StorageMaterialPageQuery extends BasePage {

    /**
     * 暂存物料批次id
     */
    @ApiModelProperty(value = "暂存物料批次id 不传查询所有", example = "1")
    private Long storageMaterialBatchId;

    /**
     * 货位id
     */
    @ApiModelProperty(value = "货位id 不传查询所有", example = "1")
    private Long materialPositionId;

    /**
     * 物料件号
     */
    @ApiModelProperty(value = "物料件号", example = "000000002")
    @Length(max = 100)
    private String materialNo;
}
