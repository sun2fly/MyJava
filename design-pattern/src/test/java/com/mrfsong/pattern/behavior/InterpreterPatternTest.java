package com.mrfsong.pattern.behavior;

import com.mrfsong.pattern.TestBase;
import com.mrfsong.pattern.behavior.interpreter.AbstractExpression;
import com.mrfsong.pattern.behavior.interpreter.Context;
import com.mrfsong.pattern.behavior.interpreter.MinusExpression;
import com.mrfsong.pattern.behavior.interpreter.PlusExpression;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *     给定一种语言，定义他的文法的一种表示，并定义一个解释器，该解释器使用该表示来解释语言中句子
 * </p>
 *
 * <p>
 *     <strong>解释器模式适用场景</strong>
 *
 *     在以下情况下可以使用解释器模式：
 *     1. 有一个简单的语法规则，比如一个sql语句，如果我们需要根据sql语句进行rm转换，就可以使用解释器模式来对语句进行解释。
 *     2. 一些重复发生的问题，比如加减乘除四则运算，但是公式每次都不同，有时是a+b-c*d，有时是a*b+c-d，等等等等个，公式千变万化，但是都是由加减乘除四个非终结符来连接的，这时我们就可以使用解释器模式。
 * </p>
 *
 * <p>
 *     <strong>解释器模式优缺点</strong>
 *     解释器是一个简单的语法分析工具，它最显著的优点就是扩展性，修改语法规则只需要修改相应的非终结符就可以了，若扩展语法，只需要增加非终结符类就可以了。
 *     但是，解释器模式会引起类的膨胀，每个语法都需要产生一个非终结符表达式，语法规则比较复杂时，就可能产生大量的类文件，为维护带来非常多的麻烦。
 *     同时，由于采用递归调用方法，每个非终结符表达式只关心与自己相关的表达式，每个表达式需要知道最终的结果，必须通过递归方式，无论是面向对象的语言还是面向过程的语言，递归都是一个不推荐的方式。由于使用了大量的循环和递归，效率是一个不容忽视的问题。特别是用于解释一个解析复杂、冗长的语法时，效率是难以忍受的
 * </p>
 *
 * @author lysongfei@gmail.com
 * @create 02/01/2018 20:12
 */
@Slf4j
public class InterpreterPatternTest extends TestBase {

    @Test
    public void exec() throws Exception {
        Context context = new Context(20);
        List<AbstractExpression> list = new ArrayList<>();
        list.add(new PlusExpression());
        list.add(new PlusExpression());
        list.add(new MinusExpression());
        list.add(new MinusExpression());
        list.add(new PlusExpression());
        list.add(new PlusExpression());
        for(AbstractExpression expression : list){
            expression.interpret(context);
            log.info("========== Output:{} ==========" , context.getOutput());
        }

    }
}
