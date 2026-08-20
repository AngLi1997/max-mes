package com.bmos.mes.service.tag.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/3/15 10:39
 */
@Data
@ApiModel("配液产出物料标签vo")
public class PreparationProduceStorageMaterialTag extends BaseStorageMaterialTag {

    /**
     * 称重人 物料件产出的产出人员
     */
    @ApiModelProperty(value = "产出人", example = "张三")
    private String producerName;

    /**
     * 复核人 物料件产出的复核人员
     */
    @ApiModelProperty(value = "复核人", example = "李四")
    private String reCheckerName;

    /**
     * 称重时间 物料件的产出时间
     */
    @ApiModelProperty(value = "称重时间", example = "2024-02-02 14:36:42")
    private String produceTime;

}
