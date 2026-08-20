package com.bmos.cache.redis.objects;

import com.bmos.common.base.user.SysUser;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/3/26 19:49
 */
@Data
public class BaseUser implements Serializable, SysUser {
    public BaseUser() {
    }

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 登录名
     */
    private String loginName;

    /**
     * 密码
     */
    private String password;


    private String userName;

    private Integer activeStatus;

    private Integer state;

    /**
     * 登录成功后返回 token
     */
    private String token;

    private Integer terminalType;

    private String serviceType;

    private Long loginTime;

    private Boolean activated;

    private Long id;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 最后更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 创建者，目前使用 SysUser 的 id 编号
     * <p>
     * 使用 String 类型的原因是，未来可能会存在非数值的情况，留好拓展性。
     */
    private String createBy;
    /**
     * 更新者，目前使用 SysUser 的 id 编号
     * <p>
     * 使用 String 类型的原因是，未来可能会存在非数值的情况，留好拓展性。
     */
    private String updateBy;

    /**
     * 删除标志
     */
    private Boolean deleted;
}
