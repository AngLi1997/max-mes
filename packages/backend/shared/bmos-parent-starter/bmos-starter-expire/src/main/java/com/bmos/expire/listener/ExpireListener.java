package com.bmos.expire.listener;

import com.bmos.expire.properties.ExpireMessageProperty;

/**
 * 到期时间到达后 需要做处理的监听器
 */

public interface ExpireListener {

    /**
     * 消息到期后，执行到此方法
     * @param message
     */
    public void onExpire(ExpireMessageProperty message);

}
