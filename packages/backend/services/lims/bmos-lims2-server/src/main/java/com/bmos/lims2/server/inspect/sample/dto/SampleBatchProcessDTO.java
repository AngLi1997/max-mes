package com.bmos.lims2.server.inspect.sample.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Description: 批量处理DTO
 * @Author: yigaohui
 * @Date: 2025/02/01 10:41
 */
@Getter
@Setter
@ApiModel("批量处理参数（样品平铺）")
public class SampleBatchProcessDTO {

    @ApiModelProperty("处理条目（按样品逐条提交信息）")
    @NotEmpty
    private List<SampleProcessItemDTO> items;
}


