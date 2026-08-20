package com.bmos.platform.service.tag.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.platform.common.enums.BooleanEnum;
import com.bmos.platform.service.tag.dto.TagInstanceField;
import com.bmos.platform.service.tag.typeHandler.TagInstanceFieldTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 标签
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/29 19:37
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "bp_tag_instance", autoResultMap = true)
public class TagInstance extends BaseDO {

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 标签类型id
     */
    private Long tagTypeId;

    /**
     * 标签场景id
     */
    private Long tagSceneId;

    /**
     * 标签定义id
     */
    private Long tagDefineId;

    /**
     * 标签字段配置
     */
    @TableField(typeHandler = TagInstanceFieldTypeHandler.class)
    private List<TagInstanceField> configFields;

    /**
     * 起停状态
     */
    @TableField("is_enable")
    private BooleanEnum enable;
}
