package com.bmos.lims2.server.stability.scheme.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * 稳定性方案检验项目班组关联实体类
 */
@Getter
@Setter
@TableName("lm_stability_scheme_item_teams")
public class StabilitySchemeItemTeams extends BaseDO {

    private Long schemeId;

    private Long versionId;

    private Long inspectItemId;

    private Long itemConfigId;

    private Long teamId;
}
