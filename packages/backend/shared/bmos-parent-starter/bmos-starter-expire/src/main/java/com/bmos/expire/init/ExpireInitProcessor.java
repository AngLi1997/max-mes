package com.bmos.expire.init;

import com.bmos.expire.properties.ExpireMessageProperty;

import java.util.List;

/**
 * 对过期数据进行初始化 接口 业务方将需要初始化的消息数据加载到初始队列中
 */
public interface ExpireInitProcessor {

    List<ExpireMessageProperty> init();

}
