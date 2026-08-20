package com.bmos.lims2.web.inspect.scheme.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @className: InspectionSchemeParameterJudgmentUpdateVO
 * @author: yigaohui
 * @date: 2025/8/18 10:07
 * @Version: 1.0
 * @description:
 */

@ApiModel("检验方案判定条件更新请求")
@Data
public class InspectionSchemeParameterJudgmentUpdateVO {

    @ApiModelProperty("分析项id，更新试必传")
    private Long parameterConfigId;

    @ApiModelProperty("判定配置ID（修改时需要）")
    private String finalExpression;

    @ApiModelProperty("表达式集合")
    List<InspectionSchemeJudgmentBatchUpdateReqVO> updateJudgmentList;
}
