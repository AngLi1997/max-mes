package com.bmos.mes.service.dataset.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bmos.mes.service.dataset.common.enums.DatasetTransValueDataType;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.logging.org.slf4j.Logger;
import org.apache.rocketmq.logging.org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 数据点组装值
 * @author liang
 * @version 1.0.0
 * @date 2024/8/20 15:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatasetTransValueData {

    Logger log = LoggerFactory.getLogger(DatasetTransValueData.class);

    public static final Integer DEFAULT_PROCESS_CHANGE_IDX = 0;

    public static final Integer DEFAULT_PROCEDURE_CHANGE_IDX = 1;

    public static final Integer DEFAULT_COPY_VERSION_IDX = 2;

    /**
     * 数据点组装值类型
     */
    private DatasetTransValueDataType type;

    /**
     * 工序换班次数
     */
    private Integer procedureChangeNumber;

    /**
     * 工艺换班次数
     */
    private Integer processChangeNumber;

    /**
     * 复制版本
     */
    private Long copyVersion;

    /**
     * 工艺名称
     */
    private String processName;

    /**
     * 工序名称
     */
    private String procedureName;

    /**
     * 批次顺序
     */
    private Integer batchIndex;

    /**
     * 是否是录入的空值
     */
    private boolean isEmpty;

    /**
     * 数据点组装值
     * 若type为IMAGE则为地址, type为EXCEL也为地址, type为text则为字符串文本，type为List则为List
     */
    private String value;

    public DatasetTransValueData(DatasetTransValueDataType type, String value) {
        this.type = type;
        this.value = value;
    }

    public DatasetTransValueData(DatasetTransValueDataType type, String value, boolean empty) {
        this.type = type;
        this.value = value;
        this.isEmpty = empty;
    }

    public List<Long> getSerial(){
        return Lists.newArrayList(processChangeNumber.longValue(), procedureChangeNumber.longValue(), copyVersion);
    }

    public static DatasetTransValueData ERROR = new DatasetTransValueData(DatasetTransValueDataType.ERROR, "ERROR");

    /**
     * 这里会将工艺名称/工序名称/批次顺序/工艺换班/工序换班添加到value的对象中后返回
     * @return
     */
    public String getExtStringValue() {
        if (StringUtils.startsWith(value, "{") && StringUtils.endsWith(value, "}")){
            JSONObject jsonObject = JSON.parseObject(value);
            extracted(jsonObject);
            return jsonObject.toJSONString();
        }else if (StringUtils.startsWith(value, "[") && StringUtils.endsWith(value, "]")){
            List<JSONObject> list = JSON.parseArray(value, JSONObject.class);
            for (JSONObject jsonObject : list) {
                extracted(jsonObject);
            }
            return JSON.toJSONString(list);
        }
        return value;
    }

    private void extracted(JSONObject jsonObject) {

        log.info("==========归档数据比对==========");
        log.info("jsonObject: {}", jsonObject);
        log.info("==========归档数据比对==========");
        log.info("processName: {}", processName);
        log.info("procedureName: {}", procedureName);
        log.info("batchIndex: {}", batchIndex);
        log.info("processChangeNumber: {}", processChangeNumber);
        log.info("procedureChangeNumber: {}", procedureChangeNumber);
        log.info("==========归档数据比对==========");

        if (jsonObject.getString("processName") == null){
            jsonObject.put("processName", processName);
        }
        if (jsonObject.getString("procedureName") == null){
            jsonObject.put("procedureName", procedureName);
        }
        if (jsonObject.getString("batchIndex") == null){
            jsonObject.put("batchIndex", batchIndex);
        }
        if (jsonObject.getString("processChangeNumber") == null){
            jsonObject.put("processChangeNumber", processChangeNumber);
        }
        if (jsonObject.getString("procedureChangeNumber") == null){
            jsonObject.put("procedureChangeNumber", procedureChangeNumber);
        }
    }
}
