package com.bmos.platform.service.material.dto;

import com.bmos.platform.service.material.model.Material;
import com.bmos.platform.service.material.model.MaterialCategory;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("远程调用同步物料返回值")
public class RemoteSyncDTO {

    /**
     * 物料列表
     */
    private List<Material> materialList;

    /**
     * 分类列表
     */
    private List<MaterialCategory> categoryList;

}
