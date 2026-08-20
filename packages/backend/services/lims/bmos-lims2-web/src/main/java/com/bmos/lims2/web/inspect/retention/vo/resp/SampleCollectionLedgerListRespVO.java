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
 * @Description: 留样领用台账列表响应VO
 * @Author: yigaohui
 * @Date: 2026/02/10
 */
@Data
@ApiModel("留样领用台账列表响应")
public class SampleCollectionLedgerListRespVO {

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

    @ApiModelProperty("领用数量")
    private String collectQuantity;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("领用原因")
    private String collectReason;

    @ApiModelProperty("领用人ID")
    private String collectorId;

    @ApiModelProperty("领用时间")
    private LocalDateTime collectTime;

    /**
     * 领用人名称
     */
    public String getCollectorName() {
        BaseUserDO user = UserUtils.getUser(collectorId);
        return ObjectUtil.isNotEmpty(user) ? user.getUserName() + StrUtil.DASHED + user.getLoginName() : null;
    }
}
