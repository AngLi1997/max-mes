package com.bmos.lims2.web.inspect.retention.vo.resp;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.lims2.server.platform.util.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Description: 留样观察任务列表响应VO
 * @Author: yigaohui
 * @Date: 2026/02/06
 */
@Data
@ApiModel("留样观察任务列表响应")
public class RetentionObservationTaskListRespVO {

    @ApiModelProperty("任务ID")
    private Long id;

    @ApiModelProperty("样品ID")
    private Long sampleId;

    @ApiModelProperty("样品编号")
    private String sampleNo;

    @ApiModelProperty("批号")
    private String batchNo;

    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("物料编码")
    private String materialCode;

    @ApiModelProperty("物料规格")
    private String materialSpec;

    @ApiModelProperty("样品数量")
    private String quantity;

    @ApiModelProperty("单位ID")
    private Long unitId;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("留样时间（样品接收时间）")
    private LocalDateTime retentionTime;

    @ApiModelProperty("留样人ID")
    private String retentionUserId;

    @ApiModelProperty("留样人名称")
    private String retentionUserName;

    @ApiModelProperty("留样期限")
    private LocalDate retentionExpiryDate;

    @ApiModelProperty("储存位置")
    private String storageLocation;

    @ApiModelProperty("观察到期时间")
    private LocalDate observationDueDate;

    @ApiModelProperty("观察年度（第几年）")
    private Integer observationYear;

    @ApiModelProperty("是否已完成")
    private Boolean completed;

    @ApiModelProperty("观察结果（true-符合，false-不符合）")
    private Boolean observationResult;

    @ApiModelProperty("观察备注")
    private String observationRemark;

    @ApiModelProperty("观察人名称")
    private String observerName;

    @ApiModelProperty("观察时间")
    private LocalDateTime observationTime;

    @ApiModelProperty("是否临期（7天内到期）")
    private Boolean isUpcoming;

    @ApiModelProperty("距离到期天数")
    private Integer daysUntilDue;

    /**
     * 观察人ID
     */
    private String observerId;

    /**
     * 留样人名称
     */
    public String getRetentionUserName() {
        BaseUserDO user = UserUtils.getUser(retentionUserId);
        return ObjectUtil.isNotEmpty(user) ? user.getUserName() + StrUtil.DASHED + user.getLoginName() : null;
    }

    /**
     * 观察人名称
     */
    public String getObserverName() {
        BaseUserDO user = UserUtils.getUser(observerId);
        return ObjectUtil.isNotEmpty(user) ? user.getUserName() + StrUtil.DASHED + user.getLoginName() : null;
    }
}
