package com.bmos.lims2.server.material.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 检品分类(BmInspectionCategory)实体类
 *
 * @author makejava
 * @since 2024-02-26 20:12:46
 */
@Getter
@Setter
@ToString
public class MaterialCategoryDTO extends BaseDO {

    /**
     * 需要和平台关联 指定id新增
     */
    @TableId(type = IdType.INPUT)
    private Long id;

    /**
     * 父级id，默认0
     */
    private Long parentId;
    /**
     * 编码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 合并编码:父级合并编码+自身编码
     */
    private String mergeCode;
    /**
     * 所属的业务分类
     */
    private Integer categoryType;
    /**
     * 平台物料分类id
     */
    private Long platformCategoryId;

}

