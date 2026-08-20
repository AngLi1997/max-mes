package com.bmos.common.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import java.util.List;

public class AdminUtil {

    public static final String ADMIN_USER = "1";

    public static final Long ADMIN_ROLE = 1L;

    public static boolean isAdminUser(String userId) {
        if (StrUtil.isEmpty(userId)) {
            return false;
        }
        return ADMIN_USER.equals(userId);
    }

    public static boolean isAdminRole(Long role) {
        if (ObjectUtil.isNull(role)) {
            return false;
        }
        return ADMIN_ROLE.equals(role);
    }

    public static boolean isAdminRole(List<Long> roles) {
        if (CollUtil.isEmpty(roles)) {
            return false;
        }
        return CollUtil.contains(roles, ADMIN_ROLE);
    }

}
