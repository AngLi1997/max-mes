package com.bmos.platform.service.factory.mapper.param;

import com.bmos.platform.common.enums.StatusEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * 房间查询参数
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomParam {

    /**
     * 房间编码
     */
    private String code;

    /**
     * 房间启停状态
     */
    private Boolean enable;

    /**
     * 房间名称
     */
    private String name;

    /**
     * 房间模型id
     */
    private Long moduleId;

    /**
     * 房间模型id集合
     */
    private List<Long> moduleIdList;

    /**
     * 部门id 数据权限
     */
    private List<Long> deptIdList;

    /**
     * 是否app端请求
     */
    private Boolean mobile;

    private Integer status;

}
