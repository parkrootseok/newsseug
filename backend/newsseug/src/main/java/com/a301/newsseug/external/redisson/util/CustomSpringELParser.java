package com.a301.newsseug.external.redisson.util;

import java.util.Objects;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class CustomSpringELParser {

    private static final SpelExpressionParser parser = new SpelExpressionParser();

    public static String getDynamicValue(String[] paramNames, Object[] paramValues, String expression) {

        StandardEvaluationContext context = new StandardEvaluationContext();

        for (int index = 0; index < paramNames.length; index++) {
            context.setVariable(paramNames[index], paramValues[index]);
        }

        String value = parser.parseExpression(expression).getValue(context, String.class);
        if (Objects.isNull(value) || value.isBlank()) {
            throw new IllegalArgumentException("Lock Key dynamic value is null or blank. Check SpEL Expression: " + expression);
        }

        return value;
    }

}
