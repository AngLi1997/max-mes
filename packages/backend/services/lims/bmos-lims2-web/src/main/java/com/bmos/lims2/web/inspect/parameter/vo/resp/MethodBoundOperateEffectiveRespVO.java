package com.bmos.lims2.web.inspect.parameter.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 方法绑定的启用版本操作规程信息 响应VO
 * @Author: yigaohui
 * @Date: 2025/11/03 00:00
 */
@Getter
@Setter
@ApiModel("方法绑定的启用版本操作规程信息 响应VO")
public class MethodBoundOperateEffectiveRespVO {

    @ApiModelProperty("操作规程ID")
    private Long operateId;

    @ApiModelProperty("操作规程名称")
    private String operateName;

    @ApiModelProperty("版本ID")
    private Long versionId;

    @ApiModelProperty("版本号")
    private String versionNo;

    @ApiModelProperty("下载路径")
    private String downloadUrl;
}


