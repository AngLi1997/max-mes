package com.bmos.lims2.server.inspect.retention.dto;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.lims2.server.platform.util.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description: 留样接收台账列表DTO
 * @Author: yigaohui
 * @Date: 2026/02/10
 */
@Data
public class RetentionReceiveLedgerListDTO {

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
     * 取样人ID
     */
    private String samplerId;

    /**
     * 取样时间
     */
    private LocalDateTime samplingTime;

    /**
     * 接收人ID
     */
    private String receiverId;

    /**
     * 接收时间
     */
    private LocalDateTime receiveTime;

    /**
     * 储存位置
     */
    private String storageLocation;

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
