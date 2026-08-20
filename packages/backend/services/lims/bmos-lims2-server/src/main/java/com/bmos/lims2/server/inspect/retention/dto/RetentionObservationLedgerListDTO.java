package com.bmos.lims2.server.inspect.retention.dto;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.lims2.server.platform.util.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description: 留样观察台账列表DTO
 * @Author: yigaohui
 * @Date: 2026/02/10
 */
@Data
public class RetentionObservationLedgerListDTO {

    /**
     * 样品编号
     */
    private String sampleNo;

    /**
     * 批号
     */
    private String batchNo;

    /**
     * 检品名称（物料名称）
     */
    private String materialName;

    /**
     * 检品编码（物料编码）
     */
    private String materialCode;

    /**
     * 规格（物料规格）
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
     * 观察结果（true-符合，false-不符合）
     */
    private Boolean observationResult;

    /**
     * 备注
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
     * 观察人名称
     */
    public String getObserverName() {
        BaseUserDO user = UserUtils.getUser(observerId);
        return ObjectUtil.isNotEmpty(user) ? user.getUserName() + StrUtil.DASHED + user.getLoginName() : null;
    }
}
