package com.bmos.mes.service.weigh.centre.config.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 称量中心
 * @author liang
 * @version 1.0.0
 * @date 2024/6/7 10:09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bm_weigh_centre")
public class WeighCentre extends BaseDO {

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 称量中心名称
     */
    private String name;

    /**
     * 称量中心编码
     */
    private String code;

    /**
     * 备注
     */
    private String remark;

    /**
     * 启停
     */
    private Boolean enabled;
}
