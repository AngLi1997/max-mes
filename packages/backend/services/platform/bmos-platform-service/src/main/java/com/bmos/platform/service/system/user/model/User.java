package com.bmos.platform.service.system.user.model;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseUserDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@TableName("bp_user")
@Getter
@Setter
@ToString
public class User extends BaseUserDO {

    private String phone;

    private String email;

    private Integer gender;

    private String remark;

    private Long validTime;

    private Integer pwdErrorCount;

    /**
     * 密码锁定之前的账户状态
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private Integer lockPreviewStatus;

    /**
     * 账户锁定后的解锁时间
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private LocalDateTime unlockTime;
}
