package com.bmos.mes.service.record.business.model;

import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ProductFormulaInfo {

    /**
     * 配方批量
     */
    private BigDecimal batchQuantity;


    /**
     * 配方物料信息
     */
    private List<ProductFormulaMaterial> materials = new ArrayList<>();

    /**
     * 配方物料map
     */
    private Map<Long, ProductFormulaMaterial> materialMap = new HashMap<>();

    /**
     * 物料id 配方物料
     */
    private Map<Long, ProductFormulaMaterial> materialIdMap = new HashMap<>();

    public void setMaterials(List<ProductFormulaMaterial> materials) {
        this.materials = materials;
        this.materialMap = CollectionUtils.convertMap(materials, ProductFormulaMaterial::getId);
        this.materialIdMap = CollectionUtils.convertMap(materials, ProductFormulaMaterial::getMaterialId);
    }
}
