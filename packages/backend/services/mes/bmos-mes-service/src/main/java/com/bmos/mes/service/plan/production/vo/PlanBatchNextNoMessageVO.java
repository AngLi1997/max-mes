package com.bmos.mes.service.plan.production.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Tolerate;

import java.util.List;

/**
 * @ClassName PlanBatchNextNoMessageVO
 * @Description 生成编号提醒vo
 * @Author Ren Jin Guang
 * @Date 2024/9/26 9:43
 */
@Setter
@Getter
@ToString
@Builder
@ApiModel("生成编号提醒vo")
public class PlanBatchNextNoMessageVO {
    @Tolerate
    public PlanBatchNextNoMessageVO() {}

    @ApiModelProperty("消息")
    private String meg;

    @ApiModelProperty("数据")
    private List<PlanBatchNextNoVO> list;
}
