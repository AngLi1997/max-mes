package com.bmos.mq.listener.Event;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class StateEvent {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("状态")
    private String state;

    @ApiModelProperty("类型")
    private String type;
}
