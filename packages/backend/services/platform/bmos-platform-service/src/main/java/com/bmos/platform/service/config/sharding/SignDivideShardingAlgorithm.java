package com.bmos.platform.service.config.sharding;

import com.google.common.collect.Range;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SignDivideShardingAlgorithm extends AbstractDivideShardingAlgorithm implements StandardShardingAlgorithm<LocalDateTime> {

    private static final String tableName = "bp_signature_log";

    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<LocalDateTime> preciseShardingValue) {
        String[] rules = divideRule(preciseShardingValue.getValue());
        return tableName + "_" + rules[0] + "_" +  rules[1];
    }

    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<LocalDateTime> rangeShardingValue) {
        // range到分表字段时策略
        Range<LocalDateTime> range = rangeShardingValue.getValueRange();
        LocalDateTime lowerEndpoint = range.lowerEndpoint();
        LocalDateTime upperEndpoint = range.upperEndpoint();
        List<String> monthRange = getRuleRange(lowerEndpoint, upperEndpoint);
        Collection<String> result = new ArrayList<>();
        for (String month : monthRange) {
            result.add(tableName + "_" + month);
        }
        return result;
    }

    @Override
    public String getType() {
        return "SIGN_LOG_TYPE"; //算法名称，可以自己定义。注意yaml中要用这个名字
    }
}
