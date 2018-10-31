package com.mrfsong.pattern.behavior;

import com.mrfsong.pattern.TestBase;
import com.mrfsong.pattern.behavior.state.ConcreteStateA;
import com.mrfsong.pattern.behavior.state.ConcreteStateB;
import com.mrfsong.pattern.behavior.state.Context;
import com.mrfsong.pattern.behavior.state.State;
import org.junit.Test;

/**
 * <p>
 *     状态模式用于解决系统中复杂对象的状态转换以及不同状态下行为的封装问题。当系统中某个对象存在多个状态，这些状态之间可以进行转换，而且对象在不同状态下行为不相同时可以使用状态模式。状态模式将一个对象的状态从该对象中分离出来，封装到专门的状态类中，使得对象状态可以灵活变化，对于客户端而言，无须关心对象状态的转换以及对象所处的当前状态，无论对于何种状态的对象，客户端都可以一致处理
 * </p>
 *
 * <p>
 *     <strong>使用场景</strong>
 *     对象的行为依赖于它的某些属性值，状态的改变将导致行为的变化。
 *     在代码中包含大量与对象状态有关的条件语句，这些条件语句的出现，会导致代码的可维护性和灵活性变差，不能方便地增加和删除状态，并且导致客户类与类库之间的耦合增强
 * </p>
 *
 * <p>
 *     <strong>优缺点</strong>
 *     状态模式优点：
 *      1. 封装了状态的转换规则，在状态模式中可以将状态的转换代码封装在环境类或者具体状态类中，可以对状态转换代码进行集中管理，而不是分散在一个个业务方法中。
 *      2. 将所有与某个状态有关的行为放到一个类中，只需要注入一个不同的状态对象即可使环境对象拥有不同的行为。
 *      3. 允许状态转换逻辑与状态对象合成一体，而不是提供一个巨大的条件语句块，状态模式可以让我们避免使用庞大的条件语句来将业务方法和状态转换代码交织在一起。
 *      4. 可以让多个环境对象共享一个状态对象，从而减少系统中对象的个数。
 *    状态模式缺点：
 *      1. 状态模式的使用必然会增加系统中类和对象的个数，导致系统运行开销增大。
 *      2. 状态模式的结构与实现都较为复杂，如果使用不当将导致程序结构和代码的混乱，增加系统设计的难度。
 *      3. 状态模式对“开闭原则”的支持并不太好，增加新的状态类需要修改那些负责状态转换的源代码，否则无法转换到新增状态；而且修改某个状态类的行为也需修改对应类的源代码。
 * </p>
 *
 * @author lysongfei@gmail.com
 * @create 02/01/2018 21:42
 */
public class StatePatternTest extends TestBase{

    @Test
    public void exec() throws Exception {
        State state = new ConcreteStateB();
        //创建环境
        Context context = new Context();
        //将状态设置到环境中
        context.setState(state);
        //请求
        context.request("test");

        state = new ConcreteStateA();
        context.setState(state);
        context.request("hello");

    }

}
