package com.bmos.mes.common.model.execute;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 工艺执行时。表单数据的扩展字段 Bean
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExecuteFormDataTimeExtInfo extends ExecuteFormDataBaseExtInfo{


    /**
     * 来自于前端时间组件保存的秒值
     * 或者时间差公式计算的秒值
     */
    private String timeSeconds;

}
