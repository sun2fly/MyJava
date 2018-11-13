package com.mrfsong.pattern.struct;

import com.mrfsong.pattern.TestBase;
import com.mrfsong.pattern.struct.proxy.*;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * <p>
 *     <strong>优点</strong>
 *     (1) 能够协调调用者和被调用者，在一定程度上降低了系统的耦合度。
 *     (2) 客户端可以针对抽象主题角色进行编程，增加和更换代理类无须修改源代码，符合开闭原则，系统具有较好的灵活性和可扩展性。
 *     此外，不同类型的代理模式也具有独特的优点，例如：
 *     (1) 远程代理为位于两个不同地址空间对象的访问提供了一种实现机制，可以将一些消耗资源较多的对象和操作移至性能更好的计算机上，提高系统的整体运行效率。
 *     (2) 虚拟代理通过一个消耗资源较少的对象来代表一个消耗资源较多的对象，可以在一定程度上节省系统的运行开销。
 *     (3) 缓冲代理为某一个操作的结果提供临时的缓存存储空间，以便在后续使用中能够共享这些结果，优化系统性能，缩短执行时间。
 *     (4) 保护代理可以控制对一个对象的访问权限，为不同用户提供不同级别的使用权限。
 *
 *     <strong>缺点</strong>
 *     (1) 由于在客户端和真实主题之间增加了代理对象，因此有些类型的代理模式可能会造成请求的处理速度变慢，例如保护代理。
 *     (2) 实现代理模式需要额外的工作，而且有些代理模式的实现过程较为复杂，例如远程代理。
 * </p>
 *
 * @author songfei@xbniao.com
 * @create 2018/01/08 17:00
 **/
@Slf4j
public class ProxyPatternTest extends TestBase {

    @Test
    @Override
    public void exec() throws Exception {
        log.info("========== JDK Proxy Start ==========");
        jdkProxy();
        log.info("========== JDK Proxy End ==========");

        log.info("========== Cglib Proxy Start ==========");
        cglibProxy();
        log.info("========== Cglib Proxy End ==========");

        log.info("========== Static Proxy Start ==========");
        staticProxy();
        log.info("========== Static Proxy End ==========");

    }


    private void jdkProxy() throws Exception {
        InvocationHandler handler = new JDKSubjectProxyHandler(ConcreteSubject.class);
        ISubject subject =
                (ISubject) Proxy.newProxyInstance(this.getClass().getClassLoader(),
                        new Class[] {ISubject.class}, handler);
        subject.action();
        log.info("Proxy class name {}", subject.getClass().getName());
        /*byte[] classFile = ProxyGenerator.generateProxyClass("$Proxy18", ConcreteSubject.class.getInterfaces());
        Files.write(classFile, new File("$Proxy18.class"));*/
    }

    private void cglibProxy() throws Exception {
        MethodInterceptor interceptor = new CglibSubjectInterceptor();
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(ConcreteSubject.class);
        enhancer.setCallback(interceptor);
        ISubject subject = (ISubject)enhancer.create();
        subject.action();
    }

    private void staticProxy() throws Exception {
        ISubject subject = new ProxySubject();
        subject.action();
    }
}
