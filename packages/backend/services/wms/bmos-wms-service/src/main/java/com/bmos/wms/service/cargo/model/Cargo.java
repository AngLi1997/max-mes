package com.bmos.wms.service.cargo.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/3/26 09:55
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "bw_cargo")
public class Cargo extends BaseDO {

    /**
     * 货品分类id
     */
    private Long cargoCategoryId;

    /**
     * 货品名称
     */
    private String cargoName;

    /**
     * 货品编码
     */
    private String cargoCode;

    /**
     * 货品合并编码
     */
    private String mergeCode;

    /**
     * 规格
     */
    private String specification;

    /**
     * 单位id
     */
    private Long unitId;

    /**
     * 是否是成员物料
     */
    private Boolean isMember;

    /**
     * 所属物料id
     */
    private Long subMaterialId;

    /**
     * 单件量
     */
    private BigDecimal singleQuantity;

    /**
     * 供应商
     */
    private String supplier;

    /**
     * 生产商
     */
    private String producer;

    /**
     * 备注
     */
    private String remark;

    /**
     * 平台物料id
     */
    private Long platformMaterialId;

    /**
     * 是否启用
     */
    private Boolean enable;
}
