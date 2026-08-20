package com.bmos.lims2.feign.material.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RemoteIssueFeignDTO {

    /**
     * 下发的物料列表
     */
    private List<MaterialIssueFeignDTO> materialList;

    /**
     * 下发的分类列表
     */
    private List<CategoryIssueFeignDTO> categoryList;

}

