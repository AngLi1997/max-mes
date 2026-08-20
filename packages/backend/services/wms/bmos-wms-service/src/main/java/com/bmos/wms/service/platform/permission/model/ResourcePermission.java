package com.bmos.wms.service.platform.permission.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 数据权限实体对象
 */
@TableName("bw_resource_permission")
@Getter
@Setter
@ToString
public class ResourcePermission {

    /**
     * 资源id
     */
    private Long resourceId;


    /**
     * 部门id
     */
    private Long deptId;
}
