package com.bmos.lims2.server.inspect.scheme.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * 检验方案分析项配置实体类
 * 新的业务层级：检验方案 → 版本 → 检验项目配置 → 分析项配置
 * 注意：使用Parameter命名以保持与InspectParameter一致
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Getter
@Setter
@TableName("lm_inspection_scheme_item_teams")
public class InspectionSchemeItemTeams extends BaseDO {

    /**
     * 关联的方案ID
     */
    private Long schemeId;

    /**
     * 关联的版本ID
     */
    private Long versionId;

    private Long packageId;

    private Long inspectItemId;

    /**
     * 关联的检验项目配置ID
     */
    private Long itemConfigId;

    private Long teamId;

}