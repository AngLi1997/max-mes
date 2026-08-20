package com.bmos.lims2.server.inspect.retention.dto;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.lims2.server.platform.util.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description: 留样销毁台账列表DTO
 * @Author: yigaohui
 * @Date: 2026/02/10
 */
@Data
public class RetentionDestructionLedgerListDTO {

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
     * 销毁数量
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
     * 销毁原因
     */
    private String destructionReason;

    /**
     * 销毁方式
     */
    private String destructionMethod;

    /**
     * 销毁地点
     */
    private String destructionLocation;

    /**
     * 销毁时间
     */
    private LocalDateTime destructionTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 销毁人ID
     */
    private String destructorId;

    /**
     * 监督人ID
     */
    private String supervisorId;

    /**
     * 销毁人名称
     */
    public String getDestructorName() {
        BaseUserDO user = UserUtils.getUser(destructorId);
        return ObjectUtil.isNotEmpty(user) ? user.getUserName() + StrUtil.DASHED + user.getLoginName() : null;
    }

    /**
     * 监督人名称
     */
    public String getSupervisorName() {
        BaseUserDO user = UserUtils.getUser(supervisorId);
        return ObjectUtil.isNotEmpty(user) ? user.getUserName() + StrUtil.DASHED + user.getLoginName() : null;
    }
}
