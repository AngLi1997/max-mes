package com.bmos.mes.service.components.comps;

import com.bmos.mes.service.components.annotations.BmosComponentDetail;
import lombok.Data;

import static com.bmos.mes.common.enums.record.BusinessComponentTypeEnum.*;

/**
 * 称量数据组件
 * @author liang
 * @version 1.0.0
 * @date 2024/11/12 17:19
 */
@Data
public class WeighDataComponentsFromDataOPT {

    /**
     * 重量
     */
    @BmosComponentDetail(WEIGHING_DATA_DETAIL_WEIGHT)
    private String weight;

    /**
     * 单位
     */
    @BmosComponentDetail(WEIGHING_DATA_DETAIL_UNIT)
    private String unit;

    /**
     * 称量人
     */
    @BmosComponentDetail(WEIGHING_DATA_DETAIL_OPERATOR)
    private String weigher;

    /**
     * 称量时间
     */
    @BmosComponentDetail(WEIGHING_DATA_DETAIL_TIME)
    private String weighTime;
}
