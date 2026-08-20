package com.bmos.lims2.server.inspect.retention.dto;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.lims2.server.platform.util.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Description: 留样观察任务列表DTO
 * @Author: yigaohui
 * @Date: 2026/02/06
 */
@Data
public class RetentionObservationTaskListDTO {

    /**
     * 任务ID
     */
    private Long id;

    /**
     * 样品ID
     */
    private Long sampleId;

    /**
     * 样品编号
     */
    private String sampleNo;

    /**
     * 批号
     */
    private String batchNo;

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 物料编码
     */
    private String materialCode;

    /**
     * 物料规格
     */
    private String materialSpec;

    /**
     * 样品数量
     */
    private String quantity;

    /**
     * 单位ID
     */
    private Long unitId;

    /**
     * 单位名称
     */
    private String unitName;

    /**
     * 留样时间（样品接收时间）
     */
    private LocalDateTime retentionTime;

    /**
     * 留样人ID
     */
    private String retentionUserId;

    /**
     * 留样期限
     */
    private LocalDate retentionExpiryDate;

    /**
     * 储存位置
     */
    private String storageLocation;

    /**
     * 观察到期时间
     */
    private LocalDate observationDueDate;

    /**
     * 观察年度（第几年）
     */
    private Integer observationYear;

    /**
     * 是否已完成
     */
    private Boolean completed;

    /**
     * 观察结果（true-符合，false-不符合）
     */
    private Boolean observationResult;

    /**
     * 观察备注
     */
    private String observationRemark;

    /**
     * 观察人ID
     */
    private String observerId;

    /**
     * 观察时间
     */
    private LocalDateTime observationTime;

    /**
     * 是否临期（7天内到期）
     */
    private Boolean isUpcoming;

    /**
     * 距离到期天数
     */
    private Integer daysUntilDue;

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
