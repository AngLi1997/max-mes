package com.bmos.lims2.server.inspect.scheme.service;

import com.bmos.lims2.server.eln.record.dto.DateCalculateVO;
import com.bmos.lims2.common.enums.RoundingRuleEnum;

/**
 * @Description: 时长计算服务
 * @Author: yigaohui
 * @Date: 2025/10/31 10:00
 */
public interface TimeDurationService {

    /**
     * 预览两个时间的时长差
     * @param startTime 开始时间，格式 yyyy-MM-dd HH:mm:ss
     * @param endTime 结束时间，格式 yyyy-MM-dd HH:mm:ss
     * @param calculateType 显示格式值（如：dd HH:mm:ss、HH:mm:ss、mm:ss、ss、dd HH:mm、dd HH、dd、HH:mm、HH、mm）
     * @param roundingUp 舍入规则枚举
     * @return 计算结果（格式化字符串与秒值）
     */
    DateCalculateVO preview(String startTime, String endTime, String calculateType, RoundingRuleEnum roundingUp);
}


