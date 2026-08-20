package com.bmos.lims2.server.inspect.order.dto;

import com.bmos.lims2.server.platform.util.UserUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @Description: 样品接收检验单DTO
 * @Author: yigaohui
 * @Date: 2025/01/29 16:30
 */
@Getter
@Setter
@Accessors(chain = true)
@ApiModel("样品接收检验单数据对象")
public class OrderPageDTO {

    @ApiModelProperty("检验单ID")
    private Long id;

    @ApiModelProperty("检验单号")
    private String orderNo;

    @ApiModelProperty("检品ID")
    private Long materialId;

    @ApiModelProperty("检品名称")
    private String materialName;

    @ApiModelProperty("检品编码")
    private String materialCode;

    @ApiModelProperty("检品规格")
    private String materialSpec;

    @ApiModelProperty("批次号")
    private String batchNo;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("创建人名称")
    private String createByName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("请验人")
    private String requestUserId;

    @ApiModelProperty("请验人名称")
    private String requestUserName;

    @ApiModelProperty("请验时间")
    private LocalDateTime requestTime;

    public String getRequestUserName() {
        if (requestUserId == null) {
            return null;
        }
        if ("system".equalsIgnoreCase(requestUserId)) {
            return "system";
        }
        return UserUtils.getUserDisplayName(requestUserId);
    }

    public String getCreateByName() {
        return UserUtils.getUserDisplayName(createBy);
    }
}
