package com.bmos.mes.service.requisition.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 预定物料组件与物料件关联表
 */
@Getter
@Setter
@TableName("bm_reserve_component_material")
public class ReserveComponentMaterial {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 物料预定组件实例id
     */
    private Long instanceId;

    /**
     * 物料件id
     */
    private Long storageMaterialId;

    /**
     * 是否取消预定
     * 被取消预订 不参与预定总量总计但是参与其他信息处理
     */
    private Boolean cancelReserve;

    /**
     * 操作量
     * 预定时是预定量
     * 取消预定时是物料剩余量 用于组件回填
     */
    private BigDecimal quantity;



}
