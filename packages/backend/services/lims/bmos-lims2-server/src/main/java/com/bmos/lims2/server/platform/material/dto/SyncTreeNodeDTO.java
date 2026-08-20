package com.bmos.lims2.server.platform.material.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 同步物料分类DTO
 */
@Getter
@Setter
public class SyncTreeNodeDTO {

    /**
     * id
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 编码
     */
    private String code;

    /**
     * 合并编码
     */
    private String mergeCode;

    /**
     * 展示名
     */
    private String showName;

    /**
     * 是否为分类节点
     */
    private boolean categoryFlag;

    /**
     * 子集
     */
    private List<SyncTreeNodeDTO> children;
}
