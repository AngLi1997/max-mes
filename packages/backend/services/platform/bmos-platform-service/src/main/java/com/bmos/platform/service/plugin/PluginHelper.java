package com.bmos.platform.service.plugin;

import cn.hutool.core.io.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 插件管理器(todo 后面可以扩展成动态加载插件)
 * @author liang
 * @version 1.0.0
 * @date 2024/11/29 17:37
 */
public class PluginHelper {

    private static final Logger log = LoggerFactory.getLogger(PluginHelper.class);

    private static final Map<String, PluginClassLoader> classLoaders = new HashMap<>();

    static {
        try {
            // 加载resource/plugin下所有的插件
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath*:/plugin/*.jar");
            for (Resource resource : resources) {
                InputStream inputStream = resource.getInputStream();
                File jar = FileUtil.writeFromStream(inputStream, File.createTempFile(Objects.requireNonNull(resource.getFilename()), ".jar"));
                URL url = jar.toURI().toURL();
                log.info("加载插件:url:{}", url);
                PluginClassLoader pluginClassLoader = new PluginClassLoader(new URL[]{url});
                classLoaders.put(jar.getName().substring(0, jar.getName().indexOf(".jar")), pluginClassLoader);
            }
        }catch (Exception e){
            log.error("加载插件失败", e);
        }
    }

    /**
     * 加载插件类
     * @param pluginName 插件名称
     * @param className 类名
     * @return 插件类
     * @throws ClassNotFoundException
     */
    public static Class<?> loadClass(String pluginName, String className) throws ClassNotFoundException {
        ClassLoader originClassLoader = Thread.currentThread().getContextClassLoader();
        PluginClassLoader pluginCLassLoader = classLoaders.get(pluginName);
        if (pluginCLassLoader == null){
            throw new RuntimeException("插件" + pluginName + "不存在");
        }
        Thread.currentThread().setContextClassLoader(pluginCLassLoader);
        Class<?> aClass = pluginCLassLoader.loadClass(className);
        Thread.currentThread().setContextClassLoader(originClassLoader);
        if (aClass == null){
            throw new RuntimeException("插件类" + className + "不存在");
        }
        return aClass;
    }
}
