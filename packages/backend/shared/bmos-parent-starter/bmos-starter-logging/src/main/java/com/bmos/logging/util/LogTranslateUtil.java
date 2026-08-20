package com.bmos.logging.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.logging.config.LogPropertiesConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.Map;

@Slf4j
@Component
public class LogTranslateUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static LogTranslateUtil util;

    @Autowired
    private LogPropertiesConfig logPropertiesConfig;

    @PostConstruct
    public void init() {
        util = this;
        util.logPropertiesConfig = this.logPropertiesConfig;
    }

    /**
     * @param operationObj 操作对象
     * @return 翻译后的json
     */
    public static String translateJson(String operationObj) {
        if (util.logPropertiesConfig == null) {
            log.error("日志翻译初始化失败,请检查配置");
            return operationObj;
        }
        LogPropertiesConfig config = util.logPropertiesConfig;
        Map<String, String> localMenuMap = config.getLocalMenuMap();
        return translateJson(operationObj, localMenuMap);
    }


    public static String translateJson(String operationObj, Map<String, String> translationMap) {
        if (CollUtil.isEmpty(translationMap) || !JSONUtil.isTypeJSON(operationObj)) {
            return operationObj;
        }
        try {
            JsonNode jsonNode = objectMapper.readTree(operationObj);
            JsonNode translatedNode = translateJson(jsonNode, translationMap, StrUtil.EMPTY);
            return JsonUtils.toJsonString(translatedNode);
        } catch (JsonProcessingException e) {
            log.error("日志翻译异常:{}", e.getCause() + e.getMessage());
        }
        return operationObj;
    }

    private static JsonNode translateJson(JsonNode jsonNode, Map<String, String> translationMap, String currentPath) throws JsonProcessingException {
        if (jsonNode.isObject()) {
            ObjectNode objectNode = (ObjectNode) jsonNode;
            ObjectNode translatedNode = objectMapper.createObjectNode();
            Iterator<Map.Entry<String, JsonNode>> fields = objectNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                String fieldName = entry.getKey();
                String fullPath = buildPath(currentPath, fieldName);
                String translatedFieldName = translationMap.get(fullPath);
                if (translatedFieldName != null) {
                    translatedNode.set(translatedFieldName, translateJson(entry.getValue(), translationMap, fullPath));
                } else {
                    translatedNode.set(fieldName, translateJson(entry.getValue(), translationMap, fullPath));
                }
            }
            return translatedNode;
        } else if (jsonNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) jsonNode;
            for (int i = 0; i < jsonNode.size(); i++) {
                arrayNode.set(i, translateJson(jsonNode.get(i), translationMap, currentPath));
            }
        }
        return jsonNode;
    }

    private static String buildPath(String currentPath, String fieldName) {
        if (currentPath.isEmpty()) {
            return fieldName;
        }
        return currentPath + "." + fieldName;
    }

}
