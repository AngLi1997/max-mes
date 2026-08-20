package com.bmos.lims2.server.inspect.order.dto;

import com.bmos.lims2.common.enums.InspectionOrderSourceEnum;
import com.bmos.lims2.common.enums.InspectionOrderStatusEnum;
import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 检验单分页查询DTO
 *
 * @author yigaohui
 * @since 2025/01/27 15:30
 */
@Getter
@Setter
@Accessors(chain = true)
@ApiModel("检验单分页查询条件")
public class InspectionOrderPageQueryDTO extends BasePage {

    @ApiModelProperty("检验单号")
    private String orderNo;

    @ApiModelProperty("检品名称")
    private String materialName;

    @ApiModelProperty("检品编码")
    private String materialCode;

    @ApiModelProperty("批次号")
    private String batchNo;

    @ApiModelProperty("单据状态（请验阶段），支持多选")
    private List<InspectionOrderStatusEnum> orderStatus;

    @ApiModelProperty("创建时间开始")
    private LocalDateTime inspectionRequestTimeStart;

    @ApiModelProperty("创建时间结束")
    private LocalDateTime inspectionRequestTimeEnd;

    @ApiModelProperty("检品ID集合")
    private List<Long> materialIds;

    @ApiModelProperty("请验人名称（模糊匹配）")
    private String requesterName;

    private List<String> requesterIds;

    @ApiModelProperty("检验单来源（REGULAR=常规请验；STABILITY=稳定性考察）")
    private InspectionOrderSourceEnum schemeSource;

}