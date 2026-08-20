package com.bmos.lims2.server.inspect.scheme.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.lims2.common.enums.ItemDurationUnitEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 检验方案检验项目配置实体类
 * 新的业务层级：检验方案 → 版本 → 检验项目配置 → 分析项配置
 *
 * @author yigaohui
 * @since 2025/01/21 10:30
 */
@Getter
@Setter
@TableName("lm_inspection_scheme_item")
public class InspectionSchemeItem extends BaseDO {

    /**
     * 关联的方案ID
     */
    private Long schemeId;

    /**
     * 关联的版本ID
     */
    private Long versionId;

    /**
     * 实验包id
     */
    private Long packageId;

    /**
     * 检验项目ID
     */
    private Long inspectItemId;

    /**
     * 是否必检
     */
    private Boolean isRequired;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;


    private Integer duration;

    private ItemDurationUnitEnum timeUnit;

}