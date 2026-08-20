package com.bmos.platform.service.factory.service.dto;

import lombok.Data;

/**
 * 楼宇(BpFactoryTenement)实体类
 *
 * @author makejava
 * @since 2024-12-30 11:54:58
 */
@Data
public class FactoryTenementDTO {
    private static final long serialVersionUID = 949045974991511772L;

    private Long id;

    /**
     * 楼宇编码
     */
    private String code;
    /**
     * 楼宇名称
     */
    private String name;

    /**
     * 父级id
     */
    private Long parentId;
}

