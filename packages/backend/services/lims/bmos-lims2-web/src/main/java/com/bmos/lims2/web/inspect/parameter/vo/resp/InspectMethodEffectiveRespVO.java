package com.bmos.lims2.web.inspect.parameter.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 方法-已生效版本 响应VO
 * @Author: yigaohui
 * @Date: 2025/11/03 10:00
 */
@Getter
@Setter
@ApiModel("方法-已生效版本 响应VO")
public class InspectMethodEffectiveRespVO {

    @ApiModelProperty("方法ID")
    private Long recordId;

    @ApiModelProperty("记录项ID（bm_batch_record_item.id）")
    private Long recordItemId;

    @ApiModelProperty("方法编码")
    private String recordCode;

    @ApiModelProperty("方法名称")
    private String recordName;

    @ApiModelProperty("方法生效版本ID")
    private Long recordVersionId;

    @ApiModelProperty("方法生效版本号")
    private String version;
}


