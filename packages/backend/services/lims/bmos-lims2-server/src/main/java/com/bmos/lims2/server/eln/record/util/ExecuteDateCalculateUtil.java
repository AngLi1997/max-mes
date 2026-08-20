package com.bmos.lims2.server.eln.record.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.bmos.common.exception.BaseResponseCode;
import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.expression.enums.RoundingEnum;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.eln.record.dto.DateCalculateVO;
import com.bmos.lims2.server.eln.record.entity.BatchRecordComponent;
import com.bmos.lims2.server.eln.record.enums.CalculateTypeEnum;
import com.bmos.lims2.server.eln.record.enums.DateTypeEnum;
import com.bmos.lims2.server.eln.record.vo.BusinessParameterVO;
import com.bmos.lims2.server.platform.util.FeignUtils;
import com.bmos.platform.facade.system.execute.parameter.constants.BusinessParameterCodeConstants;
import com.bmos.platform.facade.system.execute.parameter.feign.BusinessParameterFeign;
import com.bmos.platform.facade.system.execute.parameter.vo.BusinessParameterDetailFeignVO;
import com.google.common.collect.Maps;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 时间公式计算工具类
 *
 * @author renjinguang
 */
public class ExecuteDateCalculateUtil {

    private static final SimpleDateFormat SLF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final Map<String, Long> MAP = Maps.newHashMap();
    private static final String START = "开始时间";
    private static final String END = "结束时间";
    private static final String DEFAULT = "0";

    private static final List<String> TIME_HANDLE = Arrays.asList(CalculateTypeEnum.HH_MM.getName(),
            CalculateTypeEnum.HH.getName(), CalculateTypeEnum.HH_MM_SS.getName());

    private static final List<String> MINUTE_HANDLE = Arrays.asList(CalculateTypeEnum.MINUTE_SS.getName(),
            CalculateTypeEnum.MINUTE.getName());

    private static BusinessParameterFeign businessParameterFeign;

    public static void init(BusinessParameterFeign businessParameterFeign){
        ExecuteDateCalculateUtil.businessParameterFeign = businessParameterFeign;
    }

    /**
     * 计算两个时间的时间差
     *
     * @param startTime     开始时间
     * @param endTime       结束时间
     * @param calculateType 计算类型
     * @param roundingRule  舍入规则 true 向上 false 向下
     * @return
     */
    public static DateCalculateVO getExecuteDateCalculate(String startTime, String endTime, String calculateType, Boolean roundingRule) {
        try {
            if (StrUtil.isBlank(calculateType)) {
                calculateType = CalculateTypeEnum.DD_HH_MM_SS.getName();
            }
            ResponseInfo<BusinessParameterDetailFeignVO> businessParameter = FeignUtils.handleRequest(
                    data -> businessParameterFeign.detailByCode(data), BusinessParameterCodeConstants.PLATFORM_SYS_TIME_FORMAT_CONFIGURATION);
            Map<String, BusinessParameterVO> businessParameterMap = CollectionUtils.convertMap(
                    JSONUtil.toList(businessParameter.getData().getValue(), BusinessParameterVO.class),
                    BusinessParameterVO::getLabel);
            CalculateTypeEnum typeEnum = CalculateTypeEnum.getByType(calculateType);
            DateCalculateVO vo = new DateCalculateVO();
            Integer secondCount = Integer.valueOf(DEFAULT);
            if (startTime.equals(endTime)) {
                vo.setCalculateResult(DEFAULT +businessParameterMap.get(Collections.max(typeEnum.getType())).getValue());
                vo.setTimeSeconds(String.valueOf(secondCount));
                return vo;
            }
            long second = SLF.parse(endTime).getTime() / 1000 - SLF.parse(startTime).getTime() / 1000;
            long minute = second / 60;
            long time = minute / 60;
            long day = time / 24;
            MAP.put(DateTypeEnum.SECOND.getName(), StrUtil.equals(CalculateTypeEnum.SECOND.getName(), calculateType) ? second : second % 60);
            MAP.put(DateTypeEnum.MINUTE.getName(), MINUTE_HANDLE.contains(calculateType) ? minute : minute % 60);
            MAP.put(DateTypeEnum.TIME.getName(), TIME_HANDLE.contains(calculateType) ? time : time % 24);
            MAP.put(DateTypeEnum.DAY.getName(), day);
            StringBuilder paramBuilder = new StringBuilder();
            //修约计算
            if (StrUtil.isNotBlank(typeEnum.getFetch()) && StrUtil.isNotBlank(typeEnum.getDeposit())) {
                Long value = MAP.get(typeEnum.getFetch());
                if (roundingRule) {
                    MAP.put(typeEnum.getDeposit(),
                            value > 0 ? MAP.get(typeEnum.getDeposit()) + 1 : MAP.get(typeEnum.getDeposit()));
                }
            }
            //进位计算
            if (typeEnum.getType().size() > 1){
                String firstKey = CollectionUtils.getFirst(typeEnum.getType());
                carryCalculation(firstKey);
            }

            for (String item : typeEnum.getType()) {
                Long value = MAP.get(item);
                if (0L != value || StrUtil.isNotBlank(paramBuilder)) {
                    String secondFormula = DateTypeEnum.getFormula(item);
                    ExpressionParser parser = new SpelExpressionParser();
                    Integer result = parser.parseExpression(value + secondFormula).getValue(Integer.class);
                    secondCount = secondCount + Optional.ofNullable(result).orElse(0);
                    paramBuilder.append(value).append(businessParameterMap.get(item).getValue());
                }
            }
            if (StrUtil.isBlank(paramBuilder.toString())) {
                vo.setCalculateResult(DEFAULT +businessParameterMap.get(Collections.max(typeEnum.getType())).getValue());
                vo.setTimeSeconds(String.valueOf(secondCount));
                return vo;
            }
            vo.setCalculateResult(paramBuilder.toString());
            vo.setTimeSeconds(String.valueOf(secondCount));
            return vo;
        } catch (Exception e) {
            throw new BmosException(LimsResponseCode.CODE_RULE_TYPE_ERROR);
        }
    }

    private static void carryCalculation(String firstKey){
        List<String> labels = DateTypeEnum.getNames();
        for (int i = 0; i < labels.size(); i++) {
            String beginValue = labels.get(i);
            if (StrUtil.equals(beginValue,firstKey)){
                return;
            }
            if (DateTypeEnum.thresholdCompare(beginValue,MAP.get(beginValue))){
                MAP.put(beginValue,0L);
                String nextValue = labels.get(i + 1);
                MAP.put(nextValue,MAP.get(nextValue) + 1);
            }
        }
    }
    /**
     * @param component
     * @param key       参数key a,b
     * @param value     参数值
     * @return
     */
    public static DateCalculateVO handelDateFormula(BatchRecordComponent component, List<String> key, List<String> value) {
        Map<String, String> map = new HashMap<>();
        if (key.size() != value.size()){
            return null;
        }
        try {
            for (int i = 0; i < key.size(); ++i) {
                map.put(key.get(i), value.get(i));
            }
        } catch (Exception var10) {
            throw new BmosException(BaseResponseCode.EVALUATE_WRONG_PARAM_EXCEPTION);
        }
        Boolean roundingRule = true;
        if (StrUtil.equals(component.getRoundCode(), RoundingEnum.ROUNDING_DOWN.getCode())) {
            roundingRule = false;
        }
        Long start = Long.valueOf(map.get(START));
        Long end = Long.valueOf(map.get(END));
        if (ObjectUtil.isNull(start) || ObjectUtil.isNull(end)) {
            return null;
        }
        DateCalculateVO vo = getExecuteDateCalculate(SLF.format(new Date(start)), SLF.format(new Date(end)), component.getDateType(), roundingRule);
        return vo;
    }
}
