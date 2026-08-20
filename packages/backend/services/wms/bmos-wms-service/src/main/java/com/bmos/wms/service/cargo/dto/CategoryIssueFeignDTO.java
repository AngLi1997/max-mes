package com.bmos.wms.service.cargo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryIssueFeignDTO {
    /**
     * 需要和平台关联 指定id新增
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
     * 父节点id
     */
    private Long parentId;

    /**
     * 分类类型 0：原辅包 1：中间品 2：产品信息
     */
    private Integer categoryType;
}
