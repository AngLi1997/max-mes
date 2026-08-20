package com.bmos.lims2.server.inspect.scheme.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * 检验方案取样量配置实体类
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Getter
@Setter
@TableName("lm_inspection_scheme_sampling")
public class InspectionSchemeSampling extends BaseDO {

    /**
     * 关联的方案ID
     */
    private Long schemeId;

    /**
     * 关联的版本ID
     */
    private Long versionId;

    /**
     * 检验项目ID，为NULL时表示整体取样
     */
    private Long inspectItemId;

    /**
     * 取样量
     */
    private String samplingAmount;

    /**
     * 取样单位
     */
    private String samplingUnit;

    /**
     * 取样份数
     */
    private Integer samplingCount;
} 