package com.bmos.mes.service.dataset.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/8/30 10:57
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bm_dataset_point_template_relation")
public class DatasetPointTemplateRelation extends BaseDO {

    private String templateUrl;

    private String placeholder;

    private Integer keySize;

    private String datasetPointKeys;

    private String datasetKeys;
}
