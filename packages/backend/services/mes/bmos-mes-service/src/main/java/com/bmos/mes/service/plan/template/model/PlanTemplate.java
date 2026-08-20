package com.bmos.mes.service.plan.template.model;


import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@TableName("bm_plan_template")
@Getter
@Setter
public class PlanTemplate extends BaseDO {

    /**
     * 模板名称
     */
    private String name;

    /**
     * 确认状态 新增设置已确认
     * 下属工艺有版本与生效中版本不匹配变为未确认
     */
    private Boolean confirmed;

    /**
     * 启停状态
     */
    private Boolean state;

    /**
     * 操作人id
     */
    private String operatorUserId;

    /**
     * 操作时间
     */
    private LocalDateTime operationTime;
}
