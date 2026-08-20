package com.bmos.lims2.server.inspect.parameter.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 分析方法实体
 * @Author: yigaohui
 * @Date: 2025/10/27 00:00
 */
@Getter
@Setter
@TableName("lm_inspect_parameter_record")
public class InspectMethod extends BaseDO {

    /**
     * 批记录ID
     */
    private Long recordId;

    /**
     * 分析项ID
     */
    private Long parameterId;
}


