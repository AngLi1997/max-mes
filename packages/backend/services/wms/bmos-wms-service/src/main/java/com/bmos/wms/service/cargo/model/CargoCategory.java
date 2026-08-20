package com.bmos.wms.service.cargo.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 货品分类信息
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/22 16:55
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "bw_cargo_category")
@NoArgsConstructor
public class CargoCategory extends BaseDO {

    /**
     * 父级id
     */
    private Long parentId = 0L;

    /**
     * 货位分类名称
     */
    private String cargoCategoryName;

    /**
     * 货位分类编码
     */
    private String cargoCategoryCode;

    /**
     * 货位合并编码
     */
    private String cargoCategoryMergeCode;

    /**
     * 平台分类id
     */
    private Long platformCategoryId;

    /**
     * construct
     *
     * @param parent            父节点
     * @param cargoCategoryName 货品分类名称
     * @param cargoCategoryCode 货品分类编码
     */
    public CargoCategory(CargoCategory parent, String cargoCategoryName, String cargoCategoryCode) {
        this.cargoCategoryName = cargoCategoryName;
        this.cargoCategoryCode = cargoCategoryCode;
        if (parent != null) {
            this.parentId = parent.getId();
            this.cargoCategoryMergeCode = parent.getCargoCategoryMergeCode() + cargoCategoryCode;
        } else {
            this.cargoCategoryMergeCode = cargoCategoryCode;
        }
    }
}
