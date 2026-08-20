package com.bmos.platform.service.system.menu.constant;

public interface MenuConstant {

    /**
     * 父级ID
     */
    Long PARENT_ID = 0L;

    /**
     * 是否是菜单（是）
     */
    Integer IS_MENU = 1;

    /**
     * 是否是菜单（否）
     */
    Integer NOT_MENU = 0;

    /**
     * 根节点菜单id最大值
     */
    Long MAX_ROOT_ID = 999L;

    /**
     * 一级菜单id最大值
     */
    Long MAX_ONE_LEVEL_MENU_ID = 999999L;

    /**
     * 二级菜单id最大值
     */
    Long MAX_TWO_LEVEL_MENU_ID = 99999999L;

}
