package com.bmos.platform.service.tag.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 标签实例分类
 * (脚本控制数据)
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/29 19:38
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bp_tag_type")
public class TagType extends BaseDO {

    /**
     * 标签类型名称
     */
    private String tagTypeName;

    /**
     * 标签类型描述
     */
    private String tagTypeDesc;

    /**
     * 排序
     */
    private Integer sort;
}
