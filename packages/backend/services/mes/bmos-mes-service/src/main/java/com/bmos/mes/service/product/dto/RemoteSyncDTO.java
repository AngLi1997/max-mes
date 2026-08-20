package com.bmos.mes.service.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RemoteSyncDTO {

    /**
     * 物料列表
     */
    private List<MaterialIssueDTO> materialList;

    /**
     * 分类列表
     */
    private List<CategoryIssueDTO> categoryList;

}
