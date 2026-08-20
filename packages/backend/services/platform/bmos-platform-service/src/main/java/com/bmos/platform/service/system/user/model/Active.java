package com.bmos.platform.service.system.user.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseUserDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@TableName("bp_active")
@Getter
@Setter
@ToString
public class Active  {
    private String activeCode;

    public Active(String activeCode) {
        this.activeCode = activeCode;
    }
}
