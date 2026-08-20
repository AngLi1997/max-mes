package com.bmos.mes.service.process.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @ClassName ProcessConfigVO
 * @Description TODO
 * @Author Ren Jin Guang
 * @Date 2024/11/26 11:35
 */
@Getter
@Setter
@ToString
@ApiModel("工艺各项配置返回vo")
public class ProcessConfigVO {

    @ApiModelProperty("配置id")
    private Long configId;

    @ApiModelProperty("数据id")
    private Long dataId;

    @ApiModelProperty("房间绑定id")
    private String roomIdPath;
}
