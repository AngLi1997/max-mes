package com.bmos.platform.service.equipment.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;

/**
 * 设备类型使用日志模板
 *
 * @author yigaohui
 * @date 2024/8/13
 **/
@TableName("bp_equipment_tag_use_template")
@Data
public class EquipmentTagUseTemplate extends BaseDO {
    private Long tagId;

    private String template;

    private String operateName;
}
