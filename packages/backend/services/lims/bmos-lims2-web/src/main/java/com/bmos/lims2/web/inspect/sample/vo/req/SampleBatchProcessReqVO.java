package com.bmos.lims2.web.inspect.sample.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@ApiModel("批量处理请求（样品平铺）")
public class SampleBatchProcessReqVO {

    @ApiModelProperty("处理条目（按样品逐条提交信息）")
    @NotEmpty
    private List<SampleProcessItemReqVO> items;
}


