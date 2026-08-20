package com.bmos.lims2.web.recordprint.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description: 合并打印请求VO
 * @Author: yigaohui
 * @Date: 2025/11/25 10:40
 */
@Getter
@Setter
@ApiModel("记录打印-合并打印请求VO")
public class RecordPrintMergeReqVO {

    @ApiModelProperty(value = "检验单ID", required = true)
    @NotNull(message = "检验单ID不能为空")
    private Long inspectionId;

    @ApiModelProperty(value = "打印项（顺序即合并顺序）", required = true)
    @NotEmpty(message = "打印项不能为空")
    @Valid
    private List<PrintItemVO> items;
}


