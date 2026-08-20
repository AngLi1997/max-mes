package com.bmos.lims2.server.stability.scheme.dto.request;

import com.bmos.lims2.common.enums.StabilitySchemeVersionStatusEnum;
import com.bmos.mybatis.page.BasePage;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 稳定性方案版本查询请求DTO
 *
 * @author makejava
 * @since 2025-03-17 10:00:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StabilitySchemeVersionQueryDTO extends BasePage {

    /**
     * 方案ID
     */
    private Long schemeId;

    /**
     * 版本号
     */
    private String versionNo;

    /**
     * 状态
     */
    private StabilitySchemeVersionStatusEnum status;
}
