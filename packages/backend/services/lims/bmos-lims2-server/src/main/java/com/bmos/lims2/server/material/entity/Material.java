package com.bmos.lims2.server.material.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * 检品信息(BmInspectionProducts)实体类
 *
 * @author makejava
 * @since 2024-02-26 20:14:39
 */
@Getter
@Setter
@TableName("lm_inspect_material")
public class Material extends BaseDO {

    /**
     * 物料分类id
     */
    private Long categoryId;
    /**
     * 所属物料id
     */
    private Long principalMaterialId;
    /**
     * 物料名称
     */
    private String name;
    /**
     * 编码
     */
    private String code;
    /**
     * 规格
     */
    private String specification;
    /**
     * 单位
     */
    private Long unitId;

    private Long extendUnitId;

    /**
     * 是否是成员物料
     */
    @TableField("is_sub_material")
    private Boolean subMaterial;
    /**
     * 启停状态 0-关闭 1-启用
     */
    private Boolean status;
    /**
     * 合并编码:分类合并编码+自身编码
     */
    private String mergeCode;
    /**
     * 检品备注
     */
    private String remark;
    /**
     * 平台物料关联id
     */
    private Long platformMaterialId;
    /**
     * 当前检品所配的实验包数量
     */
    private Integer packageCount;
    /**
     * 拓展信息 json字符串
     */
    private String expandInfo;

}

