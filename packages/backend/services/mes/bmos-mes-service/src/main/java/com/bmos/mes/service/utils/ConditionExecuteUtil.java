package com.bmos.mes.service.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.process.dto.task.CheckoutConditionDTO;
import com.bmos.mes.service.process.dto.task.CheckoutExpressionDTO;
import com.bmos.mes.service.process.model.task.ProcedureConditionInstance;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author renjinguang
 */
@Slf4j
public class ConditionExecuteUtil {

    public final static int START = 'A';

    public final static int END = 'Z';

    private final static String LOGIC_OR = "||";

    private final static String LOGIC_AND = "&&";


    public static Boolean calculateExpression(CheckoutExpressionDTO dto) {
        try {
            String expression = dto.getExpression();
            if (expression.contains(LOGIC_OR) || expression.contains(LOGIC_AND)) {
                throw new BmosException(MesResponseCode.CHECKOUT_EXPRESSION_ERROR);
            }
            String expressionStr = dto.getExpression();
            if (StringUtils.isEmpty(expressionStr) || CollectionUtil.isEmpty(dto.getConditionList())) {
                log.info("执行表达式为空,表达式结果为true");
                return true;
            }
            expressionStr = replaceExpression(dto);
            log.info("执行的表达式【{}】", expressionStr);
            EvaluationContext expressionContext = getExpressionContext(dto.getConditionList());
            ExpressionParser parser = new SpelExpressionParser();
            Boolean res = Optional.ofNullable(parser.parseExpression(expressionStr).getValue(expressionContext,
                    Boolean.class)).orElse(false);
            log.info("表达式【{}】执行结果【{}】",expressionStr,res);
            return res;
        } catch (Exception e) {
            throw new BmosException(MesResponseCode.CHECKOUT_EXPRESSION_ERROR);
        }
    }

    private static String replaceExpression(CheckoutExpressionDTO checkoutExpressionDTO) {
        String expressionStr = checkoutExpressionDTO.getExpression();
        long upperCaseCount = expressionStr.chars()
                .filter(ch -> Character.isUpperCase((char) ch))
                .count();
        List<CheckoutConditionDTO> conditionList = checkoutExpressionDTO.getConditionList();
        //校验表达式与条件数量是否对等
        if (upperCaseCount != conditionList.size()){
            throw new BmosException(MesResponseCode.CHECKOUT_EXPRESSION_ERROR);
        }
        String replace = expressionStr.replace("&", " and ").replace("|", " or ");
        for (CheckoutConditionDTO condition : conditionList) {
            replace = StrUtil.replace(replace, condition.getCode(), "#" + condition.getCode());
        }
        return replace;
    }

    private static EvaluationContext getExpressionContext(List<CheckoutConditionDTO> conditionInstances) {
        EvaluationContext standardEvaluationContext = new StandardEvaluationContext();
        conditionInstances.forEach(conditionInstance -> {
            log.info("设置表达式执行上下文变量【{}】,【{}】",conditionInstance.getCode(), conditionInstance.getResult());
            standardEvaluationContext.setVariable(conditionInstance.getCode(), conditionInstance.getResult());
        });
        return standardEvaluationContext;
    }
}
