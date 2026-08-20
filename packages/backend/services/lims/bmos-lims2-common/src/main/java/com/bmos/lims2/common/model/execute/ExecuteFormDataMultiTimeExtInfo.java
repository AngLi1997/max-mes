package com.bmos.lims2.common.model.execute;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExecuteFormDataMultiTimeExtInfo extends ExecuteFormDataBaseExtInfo {


    /**
     * 对于部分业务组件汇总数据
     * 会存在多条换行时间日期 需要保存下多条时间戳
     * 注意：为保持格式一致，存在null值
     */
    private List<Long> timestampList;

    /**
     * 是否是多行时间数据
     */
    private Boolean multiLine;

}
