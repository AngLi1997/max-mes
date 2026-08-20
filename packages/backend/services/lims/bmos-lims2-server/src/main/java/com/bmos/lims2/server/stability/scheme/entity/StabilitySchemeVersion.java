package com.bmos.lims2.server.stability.scheme.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.lims2.common.enums.StabilitySchemeVersionStatusEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * 稳定性方案版本实体类
 *
 * @author makejava
 * @since 2025-03-17 10:00:00
 */
@Getter
@Setter
@TableName("lm_stability_scheme_version")
public class StabilitySchemeVersion extends BaseDO {

    /**
     * 关联的稳定性方案ID
     */
    private Long schemeId;

    /**
     * 版本号
     */
    private String versionNo;

    /**
     * 版本状态：EDITING-编辑中, APPROVING-审批中, ACTIVE-生效, INACTIVE-失效
     */
    private StabilitySchemeVersionStatusEnum status;

    /**
     * 关联的审批流程ID
     */
    private String processInstanceId;

    /**
     * 父版本ID
     */
    private Long parentVersionId;

    /**
     * 版本描述
     */
    private String description;

    /**
     * 生效日期
     */
    private LocalDate effectiveDate;

    /**
     * 检品ID（物料ID）
     */
    private Long materialId;

    /**
     * 检品名称
     */
    private String materialName;

    /**
     * 检品编码
     */
    private String materialCode;
}
