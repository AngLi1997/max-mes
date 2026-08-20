package com.bmos.mes.service.plan.document.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * 批记录模板信息版本与工艺的绑定关系(BmBatchTemplateInfoProcess)实体类
 *
 * @author makejava
 * @since 2024-08-19 11:06:42
 */
@Getter
@Setter
@TableName("bm_batch_template_info_process")
public class BatchTemplateInfoProcess {

    /**
     * bm_batch_template_info 批记录模板版本表的主键id
     */
    private Long batchTemplateInfoId;
    /**
     * 工艺id
     */
    private Long processId;

}

