package com.bmos.lims2.server.inspect.scheme.dto.request;

import com.bmos.mybatis.page.BasePage;
import lombok.Data;

/**
 * 检验方案版本查询请求DTO
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Data
public class InspectionSchemeVersionQueryDTO extends BasePage {

    /**
     * 方案ID
     */
    private Long schemeId;

    /**
     * 版本号
     */
    private String versionNo;

    /**
     * 版本状态：EDITING-编辑中, APPROVING-审批中, ACTIVE-生效, INACTIVE-失效
     */
    private String status;
} 