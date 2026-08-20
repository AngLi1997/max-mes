package com.bmos.lims2.server.inspect.sample.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Description: 批量回收DTO
 * @Author: yigaohui
 * @Date: 2025/02/01 10:40
 */
@Getter
@Setter
@ApiModel("批量回收参数（样品平铺）")
public class SampleBatchRecycleDTO {

    @ApiModelProperty("回收条目（按样品逐条提交信息）")
    @NotEmpty
    private List<SampleRecycleItemDTO> items;
}


