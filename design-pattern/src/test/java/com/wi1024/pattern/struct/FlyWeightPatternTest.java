package com.wi1024.pattern.struct;

import com.wi1024.pattern.TestBase;
import com.wi1024.pattern.struct.flyweight.FlyWeight;
import com.wi1024.pattern.struct.flyweight.FlyWeightFactory;

import org.junit.Test;

/**
 * <p>
 *     享元模式是设计模式中少数几个以提高系统性能为目的的模式之一。它的核心思想是：如果在一个系统中存在多个相同的对象，那么只需要共享一份对象的拷贝,而不必为每一次使用都创建新的对象。
 *     在享元模式中，由于需要构造和维护这些可以共享的对象，因此，常会出现一个工厂类，用于维护和创建对象。
 *     FlyWeight模式的结构
 *     1. 抽象享元角色（Flyweight）：描述一个接口，通过这个接口可以Flyweight可以接受并作用于外部状态
 *     2. 具体享元(ConcreteFlyweight)角色：实现Flyweight接口，并为内部状态（如果有的话）增加存储空间。ConcreteFlyweight对象必须是可共享的。它所存储的状态必须是内部的；即，它必须独立于ConcreteFlyweight对象的场景。
 *     3. 复合享元(UnsharableFlyweight)角色：复合享元角色所代表的对象是不可以共享的，但是一个复合享元对象可以分解成为多个本身是单纯享元对象的组合。复合享元角色又称做不可共享的享元对象。这个角色一般很少使用。
 *     4. 享元工厂(FlyweightFactoiy)角色：本角色负责创建和管理享元角色。本角色必须保证享元对象可以被系统适当地共享。当一个客户端对象请求一个享元对象的时候，享元工厂角色需要检查系统中是否已经有一个符合要求的享元对象，如果已经有了，享元工厂角色就应当提供这个已有的享元对象；如果系统中没有一个适当的享元对象的话，享元工厂角色就应当创建一个新的合适的享元对象。
 *     5. 客户端(Client)角色：维护一个对Flyweight的引用，计算或存储一个（多个）Flyweight的外部状态。
 *
 *     享元对象的所有状态分成两类：
 *     1. 内部状态(Internal State) 它不会随环境改变而改变，存储在享元对象内部，因此内蕴状态是可以共享的，对于任何一个享元对象来讲，它的值是完全相同的。我们例子中Character类的symbol属性，它代表的状态就是内蕴状态。
 *     2. 外部状态(External State) 它会随环境的改变而改变，因此是不可以共享的状态，对于不同的享元对象来讲，它的值可能是不同的。享元对象的外蕴状态必须由客户端保存，在享元对象被创建之后，需要使用的时候再传入到享元对象内部。
 * </p>
 *
 * <p>
 *    <strong>适用场景</strong>
 *    1. 一个应用程序使用了大量的对象
 *    2. 完全由于使用大量的对象，造成很大的存储开销
 *    3. 对象的大多数状态都可变为外部状态
 *    4. 如果删除对象的外部状态，那么可以用相对较少的共享对象取代很多组对象
 *    5. 应用程序不依赖对象标识
 *
 *    <strong>注意事项</strong>
 *    享元模式采用对象共享的做法来降低系统中对象的个数，从而降低细粒度对象给系统带来的内存压力。
 *    在具体实现方面，我们要注意对象状态的处理，一定要正确地区分对象的内部状态和外部状态，这是实现享元模式的关键所在。
 *    享元模式的优点在于它大幅度地降低内存中对象的数量。为了做到这一点，享元模式也付出了一定的代价：
 *    1. 享元模式为了使对象可以共享，它需要将部分状态外部化，这使得系统的逻辑变得复杂。
 *    2. 享元模式将享元对象的部分状态外部化，而读取外部状态使得运行时间会有所加长
 *
 * </p>
 *
 * @author songfei@xbniao.com
 * @create 2018/01/08 16:20
 **/
public class FlyWeightPatternTest extends TestBase {

    @Test
    @Override
    public void exec() throws Exception {
        FlyWeight flyWeight1 = FlyWeightFactory.getFlyWeight("hello");
        FlyWeight flyWeight2 = FlyWeightFactory.getFlyWeight("hello");
        FlyWeight flyWeight3 = FlyWeightFactory.getFlyWeight("hello");

        flyWeight1.action("start");
        flyWeight2.action("stop");
        flyWeight3.action("boost");
    }
}
