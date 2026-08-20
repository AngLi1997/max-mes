package com.bmos.platform.service.log.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

@TableName("bp_login_log")
@Getter
@Setter
public class LoginLogModel extends BaseDO {

    /**
     * 登录账号
     */
    private String loginName;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 描述代码
     */
    private Integer descriptionCode;

    /**
     * 操作状态
     */
    private Boolean operationState;

    /**
     * 操作动作
     */
    private Integer operationAction;

    /**
     * ip
     */
    private String ip;

    /**
     * user_id
     */
    private String userId;

    /**
     * 描述参数
     */
    private String descriptionParam;

}
