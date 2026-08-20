package com.bmos.platform.service.plugin;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/11/29 17:49
 */
@Data
@AllArgsConstructor
public class PluginDefine {

    private String pluginName;

    private String className;
}
