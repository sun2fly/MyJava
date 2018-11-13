package com.mrfsong.pattern.behavior.interpreter;

import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author lysongfei@gmail.com
 * @create 02/01/2018 20:09
 */
@Slf4j
public class PlusExpression extends AbstractExpression {
    @Override
    public void interpret(Context context) {
        log.info("========== PlusExpression#interpret ==========");
        int intInput = context.getInput();
        intInput++;
        context.setInput(intInput);
        context.setOutput(intInput);
    }
}
