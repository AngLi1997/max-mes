package com.bmos.platform.service.system.menu.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@TableName("bp_menu")
@Getter
@Setter
@ToString
public class Menu extends BaseDO {
    private String code;
    private String name;
    private Long parentId;
    private Integer isMenu;
    private Long sort;
    private Integer isOutside;
    private String outsideUrl;

    private String alias;

    private String icon;
}
