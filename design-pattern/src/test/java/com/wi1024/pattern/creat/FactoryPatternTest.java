package com.wi1024.pattern.creat;

import com.wi1024.pattern.LogUtil;
import com.wi1024.pattern.TestBase;
import com.wi1024.pattern.creat.factory.abst.AbstractFactory;
import com.wi1024.pattern.creat.factory.abst.AmdFactory;
import com.wi1024.pattern.creat.factory.abst.IntelFactory;
import com.wi1024.pattern.creat.factory.method.GifReaderFactory;
import com.wi1024.pattern.creat.factory.method.JpgReaderFactory;
import com.wi1024.pattern.creat.factory.method.PngReaderFactory;
import com.wi1024.pattern.creat.factory.method.Reader;
import com.wi1024.pattern.creat.factory.method.ReaderFactory;
import com.wi1024.pattern.creat.factory.simple.Shape;
import com.wi1024.pattern.creat.factory.simple.SimpleShapeFactory;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/09 14:33
 **/
@Slf4j
public class FactoryPatternTest extends TestBase {


    @Test
    public void exec() throws Exception {
        log.info(LogUtil.print("Simple Factory pattern start ."));
        simple();
        log.info(LogUtil.print("Simple Factory pattern end ."));

        log.info(LogUtil.print("Factory Method pattern start ."));
        method();
        log.info(LogUtil.print("Factory Method pattern end ."));

        log.info(LogUtil.print("Abstract Factory pattern start ."));
        abst();
        log.info(LogUtil.print("Abstract Factory pattern end ."));
    }

    /**
     * <p>
     *     <strong>优点</strong>
     *     (1) 工厂类包含必要的判断逻辑，可以决定在什么时候创建哪一个产品类的实例，客户端可以免除直接创建产品对象的职责，而仅仅“消费”产品，简单工厂模式实现了对象创建和使用的分离。
     *     (2) 客户端无须知道所创建的具体产品类的类名，只需要知道具体产品类所对应的参数即可，对于一些复杂的类名，通过简单工厂模式可以在一定程度减少使用者的记忆量。
     *     (3) 通过引入配置文件，可以在不修改任何客户端代码的情况下更换和增加新的具体产品类，在一定程度上提高了系统的灵活性
     *
     *     <strong>缺点</strong>
     *     (1) 由于工厂类集中了所有产品的创建逻辑，职责过重，一旦不能正常工作，整个系统都要受到影响。
     *     (2) 使用简单工厂模式势必会增加系统中类的个数（引入了新的工厂类），增加了系统的复杂度和理解难度。
     *     (3) 系统扩展困难，一旦添加新产品就不得不修改工厂逻辑，在产品类型较多时，有可能造成工厂逻辑过于复杂，不利于系统的扩展和维护。
     *     (4) 简单工厂模式由于使用了静态工厂方法，造成工厂角色无法形成基于继承的等级结构。
     *
     *     <strong>适用场景</strong>
     *     (1) 工厂类负责创建的对象比较少，由于创建的对象较少，不会造成工厂方法中的业务逻辑太过复杂。
     *     (2) 客户端只知道传入工厂类的参数，对于如何创建对象并不关心。
     * </p>
     */
    private void simple(){
        Shape circleShape = SimpleShapeFactory.getShape("circle");
        circleShape.draw();
        Shape rectShape = SimpleShapeFactory.getShape("rect");
        rectShape.draw();
        Shape triangleShape = SimpleShapeFactory.getShape("triangle");
        triangleShape.draw();
    }

    /**
     * <p>
     *     <strong>优点</strong>
     *     (1) 在工厂方法模式中，工厂方法用来创建客户所需要的产品，同时还向客户隐藏了哪种具体产品类将被实例化这一细节，用户只需要关心所需产品对应的工厂，无须关心创建细节，甚至无须知道具体产品类的类名。
     *     (2) 基于工厂角色和产品角色的多态性设计是工厂方法模式的关键。它能够让工厂可以自主确定创建何种产品对象，而如何创建这个对象的细节则完全封装在具体工厂内部。工厂方法模式之所以又被称为多态工厂模式，就正是因为所有的具体工厂类都具有同一抽象父类。
     *     (3) 使用工厂方法模式的另一个优点是在系统中加入新产品时，无须修改抽象工厂和抽象产品提供的接口，无须修改客户端，也无须修改其他的具体工厂和具体产品，而只要添加一个具体工厂和具体产品就可以了，这样，系统的可扩展性也就变得非常好，完全符合“开闭原则”。
     *
     *     <strong>缺点</strong>
     *     (1) 在添加新产品时，需要编写新的具体产品类，而且还要提供与之对应的具体工厂类，系统中类的个数将成对增加，在一定程度上增加了系统的复杂度，有更多的类需要编译和运行，会给系统带来一些额外的开销。
     *     (2) 由于考虑到系统的可扩展性，需要引入抽象层，在客户端代码中均使用抽象层进行定义，增加了系统的抽象性和理解难度，且在实现时可能需要用到DOM、反射等技术，增加了系统的实现难度。
     *
     *     <strong>适用场景</strong>
     *     (1) 客户端不知道它所需要的对象的类。在工厂方法模式中，客户端不需要知道具体产品类的类名，只需要知道所对应的工厂即可，具体的产品对象由具体工厂类创建，可将具体工厂类的类名存储在配置文件或数据库中。
     *     (2) 抽象工厂类通过其子类来指定创建哪个对象。在工厂方法模式中，对于抽象工厂类只需要提供一个创建产品的接口，而由其子类来确定具体要创建的对象，利用面向对象的多态性和里氏代换原则，在程序运行时，子类对象将覆盖父类对象，从而使得系统更容易扩展。
     * </p>
     */
    private void method(){
        ReaderFactory factory = new GifReaderFactory();
        Reader reader = factory.getReader();
        reader.read();

        factory = new JpgReaderFactory();
        reader = factory.getReader();
        reader.read();

        factory = new PngReaderFactory();
        reader = factory.getReader();
        reader.read();
    }


    /**
     * <p>
     *     <strong>优点</strong>
     *     (1) 抽象工厂模式隔离了具体类的生成，使得客户并不需要知道什么被创建。由于这种隔离，更换一个具体工厂就变得相对容易，所有的具体工厂都实现了抽象工厂中定义的那些公共接口，因此只需改变具体工厂的实例，就可以在某种程度上改变整个软件系统的行为。
     *     (2) 当一个产品族中的多个对象被设计成一起工作时，它能够保证客户端始终只使用同一个产品族中的对象。
     *     (3) 增加新的产品族很方便，无须修改已有系统，符合“开闭原则”。
     *
     *     <strong>缺点</strong>
     *     增加新的产品等级结构麻烦，需要对原有系统进行较大的修改，甚至需要修改抽象层代码，这显然会带来较大的不便，违背了“开闭原则”
     *
     *     <strong>适用场景</strong>
     *     (1) 一个系统不应当依赖于产品类实例如何被创建、组合和表达的细节，这对于所有类型的工厂模式都是很重要的，用户无须关心对象的创建过程，将对象的创建和使用解耦。
     *     (2) 系统中有多于一个的产品族，而每次只使用其中某一产品族。可以通过配置文件等方式来使得用户可以动态改变产品族，也可以很方便地增加新的产品族。
     *     (3) 属于同一个产品族的产品将在一起使用，这一约束必须在系统的设计中体现出来。同一个产品族中的产品可以是没有任何关系的对象，但是它们都具有一些共同的约束，如同一操作系统下的按钮和文本框，按钮与文本框之间没有直接关系，但它们都是属于某一操作系统的，此时具有一个共同的约束条件：操作系统的类型。
     *     (4) 产品等级结构稳定，设计完成之后，不会向系统中增加新的产品等级结构或者删除已有的产品等级结构。+
     * </p>
     */
    private void abst(){
        AbstractFactory factory = new AmdFactory();
        factory.createCpu();
        factory.createMemory();

        factory = new IntelFactory();
        factory.createCpu();
        factory.createMemory();
    }


}
