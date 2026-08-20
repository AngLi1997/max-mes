package com.bmos.lims2.server.inspect.scheme.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * 检验方案实体类
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Getter
@Setter
@TableName("lm_inspection_scheme")
public class InspectionScheme extends BaseDO {



    /**
     * 方案名称
     */
    private String name;

    /**
     * 物料ID
     */
    private Long materialId;

    /**
     * 物料编码
     */
    private String materialCode;

    /**
     * 实验包ID
     */
    private Long packageId;

    /**
     * 实验包编码
     */
    private String packageCode;

    /**
     * 当前生效版本号（冗余字段，便于列表快速判断和展示）
     */
    private String activeVersionNo;
}
 