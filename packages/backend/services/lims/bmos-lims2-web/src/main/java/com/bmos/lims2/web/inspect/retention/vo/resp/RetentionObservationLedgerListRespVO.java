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
 * @Description: 留样观察台账列表响应VO
 * @Author: yigaohui
 * @Date: 2026/02/10
 */
@Data
@ApiModel("留样观察台账列表响应")
public class RetentionObservationLedgerListRespVO {

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

    @ApiModelProperty("观察结果（true-符合，false-不符合）")
    private Boolean observationResult;

    @ApiModelProperty("备注")
    private String observationRemark;

    @ApiModelProperty("观察人ID")
    private String observerId;

    @ApiModelProperty("观察时间")
    private LocalDateTime observationTime;

    /**
     * 观察人名称
     */
    public String getObserverName() {
        BaseUserDO user = UserUtils.getUser(observerId);
        return ObjectUtil.isNotEmpty(user) ? user.getUserName() + StrUtil.DASHED + user.getLoginName() : null;
    }
}
