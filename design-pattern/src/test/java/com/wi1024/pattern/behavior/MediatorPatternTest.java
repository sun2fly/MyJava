package com.wi1024.pattern.behavior;

import com.wi1024.pattern.TestBase;
import com.wi1024.pattern.behavior.mediator.AbstractColleague;
import com.wi1024.pattern.behavior.mediator.AbstractMediator;
import com.wi1024.pattern.behavior.mediator.ColleagueA;
import com.wi1024.pattern.behavior.mediator.ColleagueB;
import com.wi1024.pattern.behavior.mediator.Mediator;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *     用一个中介者对象封装一系列的对象交互，中介者使各对象不需要显示地相互作用，从而使耦合松散，而且可以独立地改变它们之间的交互
 * </p>
 *
 * <p>
 *     <strong>中介者模式的优点</strong>
 *     1. 适当地使用中介者模式可以避免同事类之间的过度耦合，使得各同事类之间可以相对独立地使用。
 *     2. 使用中介者模式可以将对象间一对多的关联转变为一对一的关联，使对象间的关系易于理解和维护。
 *     3. 使用中介者模式可以将对象的行为和协作进行抽象，能够比较灵活的处理对象间的相互作用
 * </p>
 *
 * <p>
 *     <strong>适用场景</strong>
 *     在面向对象编程中，一个类必然会与其他的类发生依赖关系，完全独立的类是没有意义的。
 *     一个类同时依赖多个类的情况也相当普遍，既然存在这样的情况，说明，一对多的依赖关系有它的合理性，适当的使用中介者模式可以使原本凌乱的对象关系清晰，但是如果滥用，则可能会带来反的效果。
 *     一般来说，只有对于那种同事类之间是网状结构的关系，才会考虑使用中介者模式。可以将网状结构变为星状结构，使同事类之间的关系变的清晰一些
 * </p>
 *
 * @author songfei@xbniao.com
 * @create 2018/01/02 16:21
 **/
@Slf4j
public class MediatorPatternTest extends TestBase {

    @Test
    public void exec() throws Exception {

        AbstractColleague colleagueA = new ColleagueA();
        AbstractColleague colleagueB = new ColleagueB();

        AbstractMediator mediator = new Mediator(colleagueA , colleagueB);
        log.info("========== A affect B ==========");
        colleagueA.setNumber(100 , mediator);
        log.info("========== A#getNumber : {} ==========" , colleagueA.getNumber());
        log.info("========== B#getNumber : {} ==========" , colleagueB.getNumber());

        log.info("========== B affect A ==========");
        colleagueB.setNumber(1000 , mediator);
        log.info("========== B#getNumber : {} ==========" , colleagueB.getNumber());
        log.info("========== A#getNumber : {} ==========" , colleagueA.getNumber());

    }
}
