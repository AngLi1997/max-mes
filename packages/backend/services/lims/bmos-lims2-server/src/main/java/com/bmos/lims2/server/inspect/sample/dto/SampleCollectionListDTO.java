package com.bmos.lims2.server.inspect.sample.dto;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.lims2.server.platform.util.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @Description: 样品领取列表DTO
 * @Author: yigaohui
 * @Date: 2025/01/29 17:00
 */
@Getter
@Setter
@Accessors(chain = true)
@ApiModel("样品领取列表数据对象")
public class SampleCollectionListDTO {

    @ApiModelProperty("样品ID")
    private Long id;

    @ApiModelProperty("样品编号")
    private String sampleNo;

    @ApiModelProperty("样品名称")
    private String sampleName;

    @ApiModelProperty("检验单ID")
    private Long inspectionOrderId;

    @ApiModelProperty("检验单号")
    private String orderNo;

    @ApiModelProperty("检验项目ID")
    private Long inspectItemId;

    @ApiModelProperty("检验项目编码")
    private String inspectItemCode;

    @ApiModelProperty("检验项目名称")
    private String inspectItemName;

    @ApiModelProperty("检品名称")
    private String materialName;

    @ApiModelProperty("检品编码")
    private String materialCode;

    @ApiModelProperty("检品规格")
    private String materialSpec;

    @ApiModelProperty("批次号")
    private String batchNo;

    @ApiModelProperty("样品数量")
    private String quantity;

    @ApiModelProperty("单位ID")
    private Long unitId;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("请验时间")
    private LocalDateTime requestTime;

    @ApiModelProperty("请验人ID")
    private String requestUserId;

    @ApiModelProperty("取样人ID")
    private String samplerId;

    @ApiModelProperty("取样时间")
    private LocalDateTime samplingTime;

    @ApiModelProperty("接收人ID")
    private String receiverId;

    @ApiModelProperty("接收时间")
    private LocalDateTime receiveTime;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("标签是否已打印")
    private Boolean tagPrinted;

    @ApiModelProperty("请验人名称")
    public String getRequestUserName() {
        BaseUserDO user = UserUtils.getUser(requestUserId);
        return ObjectUtil.isNotEmpty(user) ? user.getUserName() + StrUtil.DASHED + user.getLoginName() : null;
    }

    @ApiModelProperty("取样人名称")
    public String getSamplerName() {
        BaseUserDO user = UserUtils.getUser(samplerId);
        return ObjectUtil.isNotEmpty(user) ? user.getUserName() + StrUtil.DASHED + user.getLoginName() : null;
    }

    @ApiModelProperty("接收人名称")
    public String getReceiverName() {
        BaseUserDO user = UserUtils.getUser(receiverId);
        return ObjectUtil.isNotEmpty(user) ? user.getUserName() + StrUtil.DASHED + user.getLoginName() : null;
    }
}
