package com.bmos.mes.service.lotrelease.template.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mes.service.lotrelease.template.enums.LotReleaseTemplateVersionStatus;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 批签发模板版本
 * @author liang
 * @version 1.0.0
 * @date 2024/8/26 18:39
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bm_lot_release_template_version")
public class LotReleaseTemplateVersion extends BaseDO {

    /**
     * 模版id
     */
    private Long templateId;

    /**
     * 模版名称
     */
    private String name;

    /**
     * 版本
     */
    private String version;

    /**
     * 模板url
     */
    private String templateUrl;

    /**
     * 备注
     */
    private String remark;

    /**
     * 版本状态
     */
    private LotReleaseTemplateVersionStatus status;

    /**
     * 是否默认
     */
    private Boolean isDefault;
}
