package com.bmos.mes.service.weigh.centre.config.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 称量中心工位关联表
 * @author liang
 * @version 1.0.0
 * @date 2024/6/7 10:09
 */
@Data
@TableName("bm_weigh_centre_station")
public class WeighCentreStation {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 称量中心id
     */
    private Long weighCentreId;

    /**
     * 工位id
     */
    private Long stationId;
}
