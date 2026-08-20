package com.bmos.mes.service.weigh.free.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author liang
 * @version 1.0.0
 * @date 2025/2/27 10:38
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("自由称量历史分页查询")
public class FreeWeighHistoryPageQuery extends BasePage {

    @ApiModelProperty(value = "物料批号", example = "1")
    private String storageMaterialBatchNo;

    @ApiModelProperty(value = "物料件号", example = "1")
    private String storageMaterialNo;

    @ApiModelProperty(value = "称量时间开始", example = "2024-02-02")
    private String weighDateStart;

    @ApiModelProperty(value = "称量时间结束", example = "2024-02-02")
    private String weighDateEnd;
}
