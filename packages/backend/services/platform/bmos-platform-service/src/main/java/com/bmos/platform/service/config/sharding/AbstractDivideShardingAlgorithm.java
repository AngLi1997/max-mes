package com.bmos.platform.service.config.sharding;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.bmos.platform.common.utils.TimeUtils;
import com.google.common.collect.Lists;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractDivideShardingAlgorithm {

     protected static final DateTimeFormatter MONTH_FORMAT = DateTimeFormatter.ofPattern("MM");

     protected static final int YEAR_RULE = 5;

     protected static final int MONTH_RULE = 12;

     protected static final List<String> allMoths = Lists.newArrayList("01","02","03","04","05","06","07","08","09","10","11","12");

    protected String[] divideRule(LocalDateTime dateTime){
        int year = dateTime.getYear();
        int cycle = year % 5;
        return new String[]{Integer.toString(cycle), LocalDateTimeUtil.format(dateTime, MONTH_FORMAT)};
    }

    /**
     * 获取最小日期搜索条件与最大搜索条件所要遍历的所有表
     * 总共两个规则
     * 规则1 ： 当前年于5取余
     * 规则2：在规则1下在取所在月
     * @param lowerEndpoint
     * @param upperEndpoint
     * @return
     */
    protected List<String> getRuleRange(LocalDateTime lowerEndpoint, LocalDateTime upperEndpoint) {
        // 计算年差
        long betweenYear = LocalDateTimeUtil.between(lowerEndpoint, upperEndpoint, ChronoUnit.YEARS);
        if (betweenYear >= YEAR_RULE){
            // 代表跨度超过5年
            List<String> yearRange = Lists.newArrayList("0", "1", "2", "3", "4");
            return mergeRule(yearRange, allMoths);
        }
        int lowerYear = lowerEndpoint.getYear();
        int upperYear = upperEndpoint.getYear();
        if (lowerYear == upperYear){
            // 代表同年
            return mergeRule(Lists.newArrayList(Integer.toString(lowerYear % YEAR_RULE)), getRuleMonthRange(lowerEndpoint, upperEndpoint));
        }
        // 后面代表不在同一年
        List<String> result = new ArrayList<>();
        // lowerDate那一年需要的规则
        result.addAll(mergeRule(Lists.newArrayList(Integer.toString(lowerYear % YEAR_RULE)), getRuleMonthRange(lowerEndpoint, TimeUtils.endOfYear(lowerEndpoint))));
        // upperDate那一年需要的规则
        result.addAll(mergeRule(Lists.newArrayList(Integer.toString(upperYear % YEAR_RULE)), getRuleMonthRange(TimeUtils.beginOfYear(upperEndpoint), upperEndpoint)));
        // 获取中间年的规则
        List<String> middleYear = Lists.newArrayList();
        while (++lowerYear < upperYear) {
            middleYear.add(Integer.toString(lowerYear % YEAR_RULE));
        }
        result.addAll(mergeRule(middleYear, allMoths));
        return result;
    }

    protected List<String> getRuleMonthRange(LocalDateTime lowerEndpoint, LocalDateTime upperEndpoint) {
        // 计算相差月数
        long betweenMonth = LocalDateTimeUtil.between(lowerEndpoint, upperEndpoint, ChronoUnit.MONTHS);
        if (betweenMonth >= MONTH_RULE){
            // 代表跨度为1年
            // 返回 1-12的字符串列表
            return allMoths;
        }
        List<String> result = new ArrayList<>();
        int lowerMonth = lowerEndpoint.getMonthValue();
        int upperMonth = upperEndpoint.getMonthValue();
        if (lowerMonth == upperMonth) {
            // 同年同月
            result.add(LocalDateTimeUtil.format(lowerEndpoint, MONTH_FORMAT));
            return result;
        }
        if (lowerMonth > upperMonth){
            // 跨年
            result.addAll(getMonthRange(lowerMonth, 12));
            result.addAll(getMonthRange(1, upperMonth));
        } else {
            result.addAll(getMonthRange(lowerMonth, upperMonth));
        }
        return result;
    }

    private Collection<String> getMonthRange(int lowerMonth, int endMonth) {
        List<String> result = new ArrayList<>();
        for (int i = lowerMonth; i <= endMonth; i++) {
            if (i < 10){
                result.add("0" + i);
            } else {
                result.add(Integer.toString(i));
            }
        }
        return result;
    }

    private List<String> mergeRule(List<String> yearRange, List<String> monthRange){
        List<String> res = new ArrayList<>();
        for (String year : yearRange) {
            for (String month : monthRange) {
                res.add(year + "_" + month);
            }
        }
        return res;
    }

    public static void main(String[] args) {
        System.out.println(LocalDateTimeUtil.between(LocalDateTime.now(), LocalDateTime.now().plusMonths(1), ChronoUnit.YEARS));
    }

}
