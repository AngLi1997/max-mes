package com.bmos.mes.service.lotrelease.template.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 批签发模板关联工艺
 * @author liang
 * @version 1.0.0
 * @date 2024/8/27 14:24
 */
@Data
@TableName("bm_lot_release_template_process")
public class LotReleaseTemplateProcessRelation {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 工艺id
     */
    private Long processId;

    /**
     * 批签发模版id
     */
    private Long lotReleaseTemplateId;
}
