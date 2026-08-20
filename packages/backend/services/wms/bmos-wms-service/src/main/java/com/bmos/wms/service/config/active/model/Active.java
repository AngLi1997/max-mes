package com.bmos.wms.service.config.active.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@TableName("bw_active")
@Getter
@Setter
@ToString
public class Active {

    private String activeCode;

    public Active(String activeCode) {
        this.activeCode = activeCode;
    }
}
