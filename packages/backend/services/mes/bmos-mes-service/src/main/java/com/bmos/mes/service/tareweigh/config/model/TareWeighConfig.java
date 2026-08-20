package com.bmos.mes.service.tareweigh.config.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 皮重配置
 * @author liang
 * @version 1.0.0
 * @date 2024/9/23 10:22
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "bm_tare_weigh_config")
@Data
public class TareWeighConfig extends BaseDO {

    /**
     * 皮重
     */
    private String tareWeigh;

    /**
     * 皮重单位
     */
    private String unit;

    /**
     * 皮重单位id
     */
    private Long unitId;

    /**
     * 描述
     */
    private String describeInfo;

    /**
     * 修订人
     */
    private String editorId;

    /**
     * 修订时间
     */
    private LocalDateTime editTime;
}
