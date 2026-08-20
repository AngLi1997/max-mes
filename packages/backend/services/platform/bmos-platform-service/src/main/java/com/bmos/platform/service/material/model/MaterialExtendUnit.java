package com.bmos.platform.service.material.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("bp_material_extend_unit")
public class MaterialExtendUnit {

    private Long materialId;

    private Long extendUnitId;

}
