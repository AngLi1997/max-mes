package com.bmos.mes.service.plan.document.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 批记录模板信息版本与工艺的绑定关系(BmBatchTemplateOperateLog)实体类
 *
 * @author makejava
 * @since 2024-08-19 14:11:59
 */
@Getter
@Setter
@TableName("bm_batch_template_operate_log")
public class BatchTemplateOperateLog extends BaseDO {
    /**
     * bm_batch_template_version 批记录模板版本表的主键id
     */
    private Long batchTemplateVersionId;
    /**
     * 操作名称
     */
    private Integer operateType;
    /**
     * 备注
     */
    private String remark;
    /**
     * 文件路径
     */
    private String path;
    /**
     * 操作人ueseId
     */
    private String operatorId;
    /**
     * 操作人登录名称
     */
    private String operatorLoginName;
    /**
     * 操作人用户名称
     */
    private String operatorName;
    /**
     * 创建时间
     */
    private LocalDateTime operateTime;

}

