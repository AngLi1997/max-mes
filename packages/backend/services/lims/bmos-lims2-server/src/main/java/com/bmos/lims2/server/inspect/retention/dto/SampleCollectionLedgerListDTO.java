package com.bmos.lims2.server.inspect.retention.dto;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.lims2.server.platform.util.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description: 留样领用台账列表DTO
 * @Author: yigaohui
 * @Date: 2026/02/10
 */
@Data
public class SampleCollectionLedgerListDTO {

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
     * 领用数量
     */
    private String collectQuantity;

    /**
     * 单位ID
     */
    private Long unitId;

    /**
     * 单位名称
     */
    private String unitName;

    /**
     * 领用原因
     */
    private String collectReason;

    /**
     * 领用人ID
     */
    private String collectorId;

    /**
     * 领用时间
     */
    private LocalDateTime collectTime;

    /**
     * 领用人名称
     */
    public String getCollectorName() {
        BaseUserDO user = UserUtils.getUser(collectorId);
        return ObjectUtil.isNotEmpty(user) ? user.getUserName() + StrUtil.DASHED + user.getLoginName() : null;
    }
}
