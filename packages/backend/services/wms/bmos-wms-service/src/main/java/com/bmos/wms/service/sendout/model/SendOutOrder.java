package com.bmos.wms.service.sendout.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.wms.common.enums.sendout.SendOrderStatus;
import com.bmos.wms.common.enums.sendout.SendOrderType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 发料工单
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/12 17:12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bw_send_out_order")
public class SendOutOrder extends BaseDO {

    /**
     * 领料计划id
     */
    private Long requisitionPlanId;

    /**
     * 产品id
     */
    private Long productId;

    /**
     * 产品编码
     */
    private String productCode;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品规格
     */
    private String productSpecification;

    /**
     * 工艺id
     */
    private Long processId;

    /**
     * 工艺名称
     */
    private String processName;

    /**
     * 生产批号
     */
    private String batchNo;

    /**
     * 领料单号
     */
    private String pullOrderNo;

    /**
     * 计划人
     */
    private String submitterId;

    /**
     * 计划时间
     */
    private LocalDateTime submitTime;

    /**
     * 发料时间
     */
    private LocalDateTime sendTime;

    /**
     * 取消时间
     */
    private LocalDateTime cancelTime;

    /**
     * 发料工单类型
     */
    private SendOrderType sendOrderType;

    /**
     * 发料工单状态
     */
    private SendOrderStatus sendOrderStatus;
}
