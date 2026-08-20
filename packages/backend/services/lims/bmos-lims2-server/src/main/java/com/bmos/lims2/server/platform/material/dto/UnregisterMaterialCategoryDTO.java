package com.bmos.lims2.server.platform.material.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**‘
 * 物料分类取消注册DTO
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UnregisterMaterialCategoryDTO {

    /**
     * 平台名称 必传
     */
    private String platformName;

    /**
     * 子业务码
     */
    private Integer childCode;

    /**
     * 物料分类id
     */
    private Long categoryId;

}
