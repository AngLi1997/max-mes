package com.bmos.mes.service.weigh.centre.config.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 称量中心分类
 * @author liang
 * @version 1.0.0
 * @date 2024/6/7 10:09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bm_weigh_centre_category")
public class WeighCentreCategory extends BaseDO {

    /**
     * 父级分类id
     */
    private Long parentId;

    /**
     * 称量中心分类名称
     */
    private String name;

    /**
     * 称量中心分类id路径
     */
    private String idPath;
}
