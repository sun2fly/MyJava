package com.mrfsong.pattern.behavior;

import com.mrfsong.pattern.TestBase;
import com.mrfsong.pattern.behavior.template.AbstractSort;
import com.mrfsong.pattern.behavior.template.ConcreteSort;
import org.junit.Test;

/**
 * <p>
 *     定义一个操作中算法的框架，而将一些步骤延迟到子类中，使得子类可以不改变算法的结构即可重定义该算法中的某些特定步骤
 * </p>
 * <p>
 *     <strong>适用场景</strong>
 *     在多个子类拥有相同的方法，并且这些方法逻辑相同时，可以考虑使用模版方法模式。在程序的主框架相同，细节不同的场合下，也比较适合使用这种模式
 * </p>
 *
 * <p>
 *     <strong>优缺点</strong>
 *     容易扩展。一般来说，抽象类中的模版方法是不易反生改变的部分，而抽象方法是容易反生变化的部分，因此通过增加实现类一般可以很容易实现功能的扩展，符合开闭原则。
 *     便于维护。对于模版方法模式来说，正是由于他们的主要逻辑相同，才使用了模版方法，假如不使用模版方法，任由这些相同的代码散乱的分布在不同的类中，维护起来是非常不方便的。
 *     比较灵活。因为有钩子方法，因此，子类的实现也可以影响父类中主逻辑的运行。但是，在灵活的同时，由于子类影响到了父类，违反了里氏替换原则，也会给程序带来风险。这就对抽象类的设计有了更高的要求
 * </p>
 *
 * @author lysongfei@gmail.com
 * @create 02/01/2018 21:22
 */
public class TemplateMethodPatternTest extends TestBase {

    @Test
    public void exec(){
        int[] array = { 10, 32, 1, 9, 5, 7, 12, 0, 4, 3 };
        AbstractSort sort = new ConcreteSort();
        sort.showSortResult(array);
    }

}
