package com.bmos.wms.service.position.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.wms.service.storage.model.Storage;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 货位信息
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/5 11:04
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "bw_cargo_position")
@Data
public class CargoPosition extends BaseDO {

    /**
     * 所属区域
     * {@link Storage} 的id
     */
    private Long storageId;

    /**
     * 暂存货位
     */
    private String position;

    /**
     * 货位编码
     */
    private String code;

    /**
     * 所属区域id路径
     */
    private String idPath;

    /**
     * 备注
     */
    private String remark;

    /**
     * 启停
     */
    private Boolean enable;
}
