package com.bmos.mes.service.weigh.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 称量数据组件称量记录
 * @author liang
 * @version 1.0.0
 * @date 2024/11/12 17:59
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bm_weigh_data")
public class WeighDataDO extends BaseDO {

    /**
     * 重量
     */
    private String weight;

    /**
     * 单位id
     */
    private Long unitId;

    /**
     * 称量人
     */
    private String weigherId;

    /**
     * 称量时间
     */
    private LocalDateTime weighTime;

    /**
     * 称量数据组件实例id
     */
    private Long componentInstanceId;
}
