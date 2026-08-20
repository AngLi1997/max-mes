package com.bmos.lims2.server.inspect.parameter.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 方法-操作规程 绑定关系实体
 * @Author: yigaohui
 * @Date: 2025/11/03 00:00
 */
@Getter
@Setter
@TableName("lm_inspect_method_operate_bind")
public class InspectMethodOperateBind extends BaseDO {

    /**
     * 方法记录ID -> bm_batch_record.id
     */
    private Long recordId;

    /**
     * 操作规程ID -> bm_operate_rule.id
     */
    private Long operateId;
}


