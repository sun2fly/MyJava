package com.mrfsong.pattern.behavior;

import com.mrfsong.pattern.TestBase;
import com.mrfsong.pattern.behavior.visitor.Element;
import com.mrfsong.pattern.behavior.visitor.ObjectStruture;
import com.mrfsong.pattern.behavior.visitor.Visitor;
import org.junit.Test;

import java.util.List;


/**
 * <p>
 *     封装某些作用于某种数据结构中各元素的操作，它可以在不改变数据结构的前提下定义作用于这些元素的新的操作
 * </p>
 *
 * <p>
 *     <strong>访问者模式的优点</strong>
 *     1. 符合单一职责原则：凡是适用访问者模式的场景中，元素类中需要封装在访问者中的操作必定是与元素类本身关系不大且是易变的操作，使用访问者模式一方面符合单一职责原则，另一方面，因为被封装的操作通常来说都是易变的，所以当发生变化时，就可以在不改变元素类本身的前提下，实现对变化部分的扩展。
 *     2.扩展性良好：元素类可以通过接受不同的访问者来实现对不同操作的扩展。
 * </p>
 *
 * <p>
 *     <strong>访问者模式的适用场景</strong>
 *      1. 假如一个对象中存在着一些与本对象不相干（或者关系较弱）的操作，为了避免这些操作污染这个对象，则可以使用访问者模式来把这些操作封装到访问者中去。
 *      2. 假如一组对象中，存在着相似的操作，为了避免出现大量重复的代码，也可以将这些重复的操作封装到访问者中去。
 *      但是，访问者模式并不是那么完美，它也有着致命的缺陷：增加新的元素类比较困难。通过访问者模式的代码可以看到，在访问者类中，每一个元素类都有它对应的处理方法，也就是说，每增加一个元素类都需要修改访问者类（也包括访问者类的子类或者实现类），修改起来相当麻烦。也就是说，
 *      在元素类数目不确定的情况下，应该慎用访问者模式。所以，访问者模式比较适用于对已有功能的重构，比如说，一个项目的基本功能已经确定下来，元素类的数据已经基本确定下来不会变了，会变的只是这些元素内的相关操作，这时候，我们可以使用访问者模式对原有的代码进行重构一遍，这样一来，就可以在不修改各个元素类的情况下，对原有功能进行修改。
 * </p>
 *
 * @author songfei@xbniao.com
 * @create 2018/01/02 14:56
 **/
public class VisitorPatternTest extends TestBase {

    @Test
    public void exec() throws Exception {

        List<Element> list = ObjectStruture.getList();
        for(Element element : list){
            element.accept(new Visitor());
        }

    }

}
