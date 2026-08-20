package com.bmos.mes.service.platform.code.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("BatchNextCodeVO:返回下一个编码批量")
public class BatchNextCodeVO {
    @ApiModelProperty("编码")
    private String code;
    @ApiModelProperty("编号")
    private List<NextCodeVO> nos;
    @ApiModelProperty("时间")
    private LocalDate applyTime;

    @Data
    @ApiModel("下一个编码")
    public static class NextCodeVO {
        @ApiModelProperty("编号")
        private String no;

        @ApiModelProperty("id")
        private Long id;

    }
}
