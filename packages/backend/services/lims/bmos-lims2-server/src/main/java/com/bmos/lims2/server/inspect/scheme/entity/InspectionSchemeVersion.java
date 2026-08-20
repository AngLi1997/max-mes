package com.bmos.lims2.server.inspect.scheme.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.lims2.common.enums.InspectionSchemeVersionStatusEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 检验方案版本实体类
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Getter
@Setter
@TableName("lm_inspection_scheme_version")
public class InspectionSchemeVersion extends BaseDO {

    /**
     * 关联的检验方案ID
     */
    private Long schemeId;

    /**
     * 版本号
     */
    private String versionNo;

    /**
     * 版本状态：EDITING-编辑中, APPROVING-审批中, ACTIVE-生效, INACTIVE-失效
     */
    private InspectionSchemeVersionStatusEnum status;

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
     * 物料ID
     */
    private Long materialId;

    /**
     * 物料编码
     */
    private String materialCode;

    /**
     * 实验包ID
     */
    private Long packageId;

    /**
     * 实验包编码
     */
    private String packageCode;
} 