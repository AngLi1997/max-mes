package com.bmos.platform.service.material.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 物料分类
 */
@Getter
@Setter
@ToString
@TableName("bp_material_category")
public class MaterialCategory extends BaseDO {

    /**
     * 名称
     */
    private String name;

    /**
     * 编码
     */
    private String code;

    /**
     * 合并编码
     */
    private String mergeCode;


    /**
     * 父节点id
     */
    private Long parentId;

    /**
     * 已下发业务
     */
    private String dispenseRecord;
}
