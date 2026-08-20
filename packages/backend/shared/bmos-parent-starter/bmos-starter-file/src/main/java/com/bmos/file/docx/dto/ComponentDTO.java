package com.bmos.file.docx.dto;

import lombok.Data;

@Data
public class ComponentDTO {

    /**
     * 组件id
     */
    private Long id;

    /**
     * 组件类型
     */
    private String componentType;

    /**
     * fieldId
     */
    private Long fieldId;

    /**
     * 组件详情
     */
    private String componentDetail;

    /**
     * 记录版本id
     */
    private Long recordVersionId;

    /**
     * 父级id
     */
    private Long parentId;

    /**
     * 记录项id
     */
    private Long recordItemId;

}
