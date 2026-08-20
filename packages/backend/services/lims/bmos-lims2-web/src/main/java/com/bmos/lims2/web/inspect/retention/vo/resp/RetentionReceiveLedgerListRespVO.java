package com.bmos.lims2.web.inspect.retention.vo.resp;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.lims2.server.platform.util.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description: 留样接收台账列表响应VO
 * @Author: yigaohui
 * @Date: 2026/02/10
 */
@Data
@ApiModel("留样接收台账列表响应")
public class RetentionReceiveLedgerListRespVO {

    @ApiModelProperty("样品编号")
    private String sampleNo;

    @ApiModelProperty("批号")
    private String batchNo;

    @ApiModelProperty("检品名称")
    private String materialName;

    @ApiModelProperty("检品编码")
    private String materialCode;

    @ApiModelProperty("规格")
    private String materialSpec;

    @ApiModelProperty("样品数量")
    private String quantity;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("取样人ID")
    private String samplerId;

    @ApiModelProperty("取样时间")
    private LocalDateTime samplingTime;

    @ApiModelProperty("接收人ID")
    private String receiverId;

    @ApiModelProperty("接收时间")
    private LocalDateTime receiveTime;

    /**
     * 取样人名称
     */
    public String getSamplerName() {
        BaseUserDO user = UserUtils.getUser(samplerId);
        return ObjectUtil.isNotEmpty(user) ? user.getUserName() + StrUtil.DASHED + user.getLoginName() : null;
    }

    /**
     * 接收人名称
     */
    public String getReceiverName() {
        BaseUserDO user = UserUtils.getUser(receiverId);
        return ObjectUtil.isNotEmpty(user) ? user.getUserName() + StrUtil.DASHED + user.getLoginName() : null;
    }
}
