package com.wi1024.pattern.behavior;

import com.wi1024.pattern.TestBase;
import com.wi1024.pattern.behavior.observer.AConcreteObserver;
import com.wi1024.pattern.behavior.observer.BConcreteObserver;
import com.wi1024.pattern.behavior.observer.ConcreteSubject;
import com.wi1024.pattern.behavior.observer.Subject;

import org.junit.Test;

/**
 * <p>
 *     定义对象间一种一对多的依赖关系，使得当每一个对象改变状态，则所有依赖于它的对象都会得到通知并自动更新。
 * </p>
 *
 * <p>
 *     <strong>观察者模式的优点</strong>
 *      观察者与被观察者之间是属于轻度的关联关系，并且是抽象耦合的，这样，对于两者来说都比较容易进行扩展。观察者模式是一种常用的触发机制，它形成一条触发链，依次对各个观察者的方法进行处理。
 *      但同时，这也算是观察者模式一个缺点，由于是链式触发，当观察者比较多的时候，性能问题是比较令人担忧的。并且，在链式结构中，比较容易出现循环引用的错误，造成系统假死
 * </p>
 *
 * @author songfei@xbniao.com
 * @create 2018/01/02 15:57
 **/
public class ObserverPatternTest extends TestBase {

    @Test
    public void exec() throws Exception {
        Subject subject = new ConcreteSubject();
        subject.addObserver(new AConcreteObserver());
        subject.addObserver(new BConcreteObserver());
        subject.doSomething();
    }
}
