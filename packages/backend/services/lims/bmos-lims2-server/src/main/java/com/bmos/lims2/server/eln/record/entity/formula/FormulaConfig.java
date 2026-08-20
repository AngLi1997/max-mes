package com.bmos.lims2.server.eln.record.entity.formula;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.lims2.common.model.execute.ExecuteFormDataBaseExtInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface FormulaConfig {

    /**
     * 校验配置是否齐全
     * @return
     */
    boolean configIsComplete();

    /**
     * 根据输入值获取当前配置下的计算结果
     * @param input 输入值
     * @return
     */
    default String calculateResult(String input){
        return null;
    }

    /**
     * 根据多个输入值获取当前配置下的计算结果
     * @param inputs
     * @return
     */
    default String calculateResult(List<String> inputs) {
        return null;
    }

    /**
     * 传入拓展信息获取时间戳
     * @param extInfo 拓展信息json字符串
     * @return
     */
    default LocalDateTime getExtLocalDateTime(String extInfo) {
        if (StrUtil.isEmpty(extInfo)) {
            return null;
        }
        ExecuteFormDataBaseExtInfo info = JsonUtils.parseObject(extInfo,
                ExecuteFormDataBaseExtInfo.class);
        return getLocalDateTime(info.getTimeStamp());
    }
    static LocalDateTime getLocalDateTime(String input) {
        Long timeStamp = Long.valueOf(input);
        LocalDateTime time = LocalDateTimeUtil.of(timeStamp, ZoneId.systemDefault());
        return time;
    }


}
