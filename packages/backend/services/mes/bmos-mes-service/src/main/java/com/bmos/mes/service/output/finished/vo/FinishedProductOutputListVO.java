package com.bmos.mes.service.output.finished.vo;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ApiModel("成品产出列表VO")
@Data
public class FinishedProductOutputListVO {

    @ApiModelProperty("成品编码")
    private String productMergeCode;

    @ApiModelProperty("成品名称")
    private String productName;

    @ApiModelProperty("成品批号")
    private String productBatchNo;

    @ApiModelProperty("单件量")
    private BigDecimal singleQuantity;

    @ApiModelProperty("单位")
    private String unitName;

    @ApiModelProperty("单位id")
    private Long unitId;

    @ApiModelProperty("件数")
    private Integer number;

    @ApiModelProperty("操作人id")
    private String operatorId;

    @ApiModelProperty("操作人")
    private String operatorName;

    @ApiModelProperty("操作时间")
    private LocalDateTime createTime;

    @ApiModelProperty("成品规格")
    private String specification;

    public String getOperatorName(){
        BaseUserDO user = UserUtils.getUser(operatorId);
        return ObjectUtil.isNotEmpty(user) ? (user.getUserName() + StrUtil.DASHED + user.getLoginName()) : "";
    }

}
