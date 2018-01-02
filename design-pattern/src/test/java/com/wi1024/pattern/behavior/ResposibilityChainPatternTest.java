package com.wi1024.pattern.behavior;

import com.wi1024.pattern.TestBase;
import com.wi1024.pattern.behavior.chain.*;
import org.junit.Test;

/**
 * <P>
 *     使多个对象都有机会处理请求，从而避免了请求的发送者和接收者之间的耦合关系。将这些对象连成一条链，并沿着这条链传递该请求，直到有对象处理它为止
 * </P>
 * <P>
 *     <STRONG>使用场景</STRONG>
 *     假如使用if…else…语句来组织一个责任链时感到力不从心，代码看上去很糟糕时，就可以使用责任链模式来进行重构
 * </P>
 *
 * <P>
 *     <STRONG>优缺点</STRONG>
 *     责任链模式与if…else…相比，他的耦合性要低一些，因为它把条件判定都分散到了各个处理类中，并且这些处理类的优先处理顺序可以随意设定。责任链模式也有缺点，这与if…else…语句的缺点是一样的，那就是在找到正确的处理类之前，所有的判定条件都要被执行一遍，当责任链比较长时，性能问题比较严重
 * </P>
 *
 * @author lysongfei@gmail.com
 * @create 02/01/2018 21:36
 */
public class ResposibilityChainPatternTest extends TestBase {

    @Test
    public void exec() throws Exception {
        Leader director = new Director("张三");
        Leader manager = new Manager("李四");
        Leader gManager = new GeneralManager("王五");

        // 组织好责任链对象的关系
        director.setNextLeader(manager);
        manager.setNextLeader(gManager);

        // 开始请假操作
        LeaveRequest request = new LeaveRequest("倪升武", 15, "在家睡觉");
        director.handleRequest(request);
    }

}
