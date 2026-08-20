package com.bmos.logging.model;

import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogModel extends BaseDO {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 操作人id
     */
    private String userId;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 操作类型
     */
    private Integer operationType;

    /**
     * 菜单id
     */
    private Long menuId;

    /**
     * 业务操作
     */
    private String operationBusiness;

    /**
     * 操作对象
     */
    private String operationObject;

    /**
     * 备注
     */
    private String remark;

    /**
     * 登录账号
     */
    private String loginName;

}
