package com.bmos.lims2.web.inspect.parameter.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Description: 方法列表响应VO（含已绑定操作规程ID集合）
 * @Author: yigaohui
 * @Date: 2025/11/11 00:00
 */
@Getter
@Setter
@ApiModel("方法列表响应VO（含已绑定操作规程ID集合）")
public class InspectMethodListRespVO {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("方法记录ID")
    private Long recordId;

    @ApiModelProperty("分析项ID")
    private Long parameterId;
}


