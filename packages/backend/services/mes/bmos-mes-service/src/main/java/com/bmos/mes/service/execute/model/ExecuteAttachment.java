package com.bmos.mes.service.execute.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@TableName("bm_execute_attachment")
public class ExecuteAttachment extends BaseDO {

    private String type;


    private String path;

    /**
     * 生产计划id
     */
    private Long productPlanId;

    /**
     * 批号
     */
    private String batchNo;

    /**
     * 工艺id
     */
    private Long processId;

    /**
     * 工艺版本
     */
    private String processVersion;

    /**
     * 记录项id
     */
    private Long recordItemId;

    /**
     * 记录项id
     */
    private Long recordVersionId;

    /**
     * 历史工序步骤id
     */
    private Long procedureStepId;

    /**
     * 工艺换班次数
     */
    private Integer processChangeNumber;

    /**
     * 工序换班次数
     */
    private Integer procedureChangeNumber;

    /**
     * 是否复用
     */
    @TableField("is_reuse")
    private Boolean reuse;

    /**
     * 复制版本（默认0）
     */
    private Long copyVersion;

    /**
     * 附件类型
     */
    private String attachmentType;

    /**
     * 备注信息
     */
    private String remark;
}
