package com.bmos.platform.facade.factory.vo;

import lombok.Data;

/**
 * 工位是否有权限
 */
@Data
public class StationPermissionVO {

    /**
     * 工位id
     */
    private Long stationId;

    /**
     * 权限
     */
    private boolean permission;

}
