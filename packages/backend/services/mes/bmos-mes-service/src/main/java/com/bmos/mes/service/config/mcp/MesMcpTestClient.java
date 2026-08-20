package com.bmos.mes.service.config.mcp;

import org.noear.solon.ai.mcp.client.McpClientToolProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liang
 * @version 1.0.0
 * @date 2025/4/24 13:35
 */
public class MesMcpTestClient {
    public static void main(String[] args) {
        McpClientToolProvider toolProvider = McpClientToolProvider.builder()
                .apiUrl("http://localhost:60300/api/app/mes/mcp/sse")
                .build();

        Map<String, Object> map = new HashMap<>();
//        map.put("bomName", "");
//        map.put("bomVersion", "");
//        map.put("productName", "");
//        map.put("productCode", "");
        String rst = toolProvider.callToolAsText("getWmsStorageInventoryData", map);
        System.out.println(rst);
    }
}
