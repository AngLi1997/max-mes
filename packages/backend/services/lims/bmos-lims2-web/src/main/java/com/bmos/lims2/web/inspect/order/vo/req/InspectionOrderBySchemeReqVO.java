package com.bmos.lims2.web.inspect.order.vo.req;

import com.bmos.lims2.common.enums.InspectionOrderStatusEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description: 按方案ID查询检验单下拉-请求VO
 * @Author: yigaohui
 * @Date: 2025/09/08 10:20
 */
@Data
@ApiModel("按方案ID查询检验单选项请求")
public class InspectionOrderBySchemeReqVO {

    @ApiModelProperty(value = "方案ID", required = true)
    @NotNull
    private Long schemeId;

    @ApiModelEnumProperty(value = "请验阶段状态枚举", enumClass = InspectionOrderStatusEnum.class)
    private InspectionOrderStatusEnum orderStatus;
}


