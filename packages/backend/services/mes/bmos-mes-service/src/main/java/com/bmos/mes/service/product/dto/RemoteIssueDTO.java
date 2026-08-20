package com.bmos.mes.service.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RemoteIssueDTO {

    /**
     * 下发的物料列表
     */
    private List<MaterialIssueDTO> materialList;

    /**
     * 下发的分类列表
     */
    private List<CategoryIssueDTO> categoryList;

    /**
     * 下发的业务类型列表
     */
    private List<Integer> businesses;

}
