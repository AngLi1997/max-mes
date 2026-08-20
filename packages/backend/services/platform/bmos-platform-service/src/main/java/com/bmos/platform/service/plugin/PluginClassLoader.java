package com.bmos.platform.service.plugin;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

/**
 * 插件 类加载器
 * @author liang
 * @version 1.0.0
 * @date 2024/11/29 16:53
 */
public class PluginClassLoader extends URLClassLoader {

    public PluginClassLoader(URL[] urls){
        // 打断双亲委派 直接重新从jar中加载
        super(urls, null);

        Arrays.stream(urls).forEach(url -> {
            System.out.println("加载插件: " + url);
        });
    }
}
