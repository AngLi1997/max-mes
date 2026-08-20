package com.bmos.lims2.server.inspect.scheme.dto.request;

import com.bmos.mybatis.page.BasePage;
import lombok.Data;

import java.util.List;

/**
 * 检验方案查询请求DTO
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Data
public class InspectionSchemeQueryDTO extends BasePage {



    /**
     * 方案名称
     */
    private String name;

    /**
     * 物料ID集合
     */
    private List<Long> materialIds;

    /**
     * 是否仅返回存在生效版本的方案（默认false）
     */
    private Boolean onlyActive;
} 