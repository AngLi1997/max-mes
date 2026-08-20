package com.bmos.lims2.common.model.execute;

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
public class ExecuteFormDataBaseExtInfo {

    /**
     * 若表单类型是一个关于日期的 则需要存下值对应的时间搓 若业务组件获取的值是一个非准确时间 则取最大时间
     * eg：2024-06-27则存的2024-06-27 23:59:59
     * 若是前端的扩展字段中有timeStamp的值 则使用前端的进行复值
     */
    private String timeStamp;

}
