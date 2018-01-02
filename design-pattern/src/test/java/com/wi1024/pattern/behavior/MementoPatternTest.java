package com.wi1024.pattern.behavior;

import com.wi1024.pattern.TestBase;
import com.wi1024.pattern.behavior.memento.Caretaker;
import com.wi1024.pattern.behavior.memento.Originator;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * <p>
 *     在不破坏封装性的前提下，捕获一个对象的内部状态，并在该对象之外保存这个状态。这样就可以将该对象恢复到原先保存的状态
 * </p>
 *
 * <p>
 *      <strong>适用场景</strong>
 *      如果有需要提供回滚操作的需求，使用备忘录模式非常适合，比如jdbc的事务操作，文本编辑器的Ctrl+Z恢复等
 * </p>
 *
 * <p>
 *      <strong>优缺点</strong>
 *      备忘录模式的优点有：
 *          1. 当发起人角色中的状态改变时，有可能这是个错误的改变，我们使用备忘录模式就可以把这个错误的改变还原。
 *          2. 备份的状态是保存在发起人角色之外的，这样，发起人角色就不需要对各个备份的状态进行管理。
 *      备忘录模式的缺点：
 *          在实际应用中，备忘录模式都是多状态和多备份的，发起人角色的状态需要存储到备忘录对象中，对资源的消耗是比较严重的。
 * </p>
 *
 * @author lysongfei@gmail.com
 * @create 02/01/2018 21:08
 */
@Slf4j
public class MementoPatternTest extends TestBase {

    @Test
    public void exec() throws Exception {

        Originator originator = new Originator();
        Caretaker caretaker = new Caretaker();
        originator.setStateA("1");
        originator.setStateB("1");
        originator.setStateC("1");
        log.info("========== Initial:{} ==========" , originator);

        caretaker.setMemento("001",originator.createMemento());
        originator.setStateA("A");
        originator.setStateB("B");
        originator.setStateC("C");
        log.info("========== After Modify:{} ==========" , originator);


        originator.restoreMemento(caretaker.getMemento("001"));
        log.info("========== After Restore:{} ==========" , originator);


    }

}
