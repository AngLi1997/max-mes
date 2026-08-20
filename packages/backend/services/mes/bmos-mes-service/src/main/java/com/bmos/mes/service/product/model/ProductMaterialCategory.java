package com.bmos.mes.service.product.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@TableName("bm_material_category")
public class ProductMaterialCategory extends BaseDO {

    /**
     * 需要和平台关联 指定id新增
     */
    @TableId(type = IdType.INPUT)
    private Long id;

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
     * 分类类型 0：原辅包 1：中间品 2：产品信息
     */
    private Integer categoryType;

    /**
     * 平台分类id
     */
    private Long platformCategoryId;
}
