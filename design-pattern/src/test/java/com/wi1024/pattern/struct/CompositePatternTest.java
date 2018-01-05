package com.wi1024.pattern.struct;

import com.wi1024.pattern.TestBase;
import com.wi1024.pattern.struct.composite.Folder;
import com.wi1024.pattern.struct.composite.ImageFile;
import com.wi1024.pattern.struct.composite.TextFile;
import com.wi1024.pattern.struct.composite.VideoFile;

import org.junit.Test;

/**
 *
 * <p>
 *     对于树形结构，当容器对象（如文件夹）的某一个方法被调用时，将遍历整个树形结构，寻找也包含这个方法的成员对象（可以是容器对象，也可以是叶子对象）并调用执行，牵一而动百，其中使用了递归调用的机制来对整个结构进行处理。
 *     由于容器对象和叶子对象在功能上的区别，在使用这些对象的代码中必须有区别地对待容器对象和叶子对象，而实际上大多数情况下我们希望一致地处理它们，因为对于这些对象的区别对待将会使得程序非常复杂。
 *     组合模式为解决此类问题而诞生，它可以让叶子对象和容器对象的使用具有一致性。
 * </p>
 * <p>
 *
 *  <strong>优点</strong>
 *  (1) 组合模式可以清楚地定义分层次的复杂对象，表示对象的全部或部分层次，它让客户端忽略了层次的差异，方便对整个层次结构进行控制。
 *  (2) 客户端可以一致地使用一个组合结构或其中单个对象，不必关心处理的是单个对象还是整个组合结构，简化了客户端代码。
 *  (3) 在组合模式中增加新的容器构件和叶子构件都很方便，无须对现有类库进行任何修改，符合“开闭原则”。
 *  (4) 组合模式为树形结构的面向对象实现提供了一种灵活的解决方案，通过叶子对象和容器对象的递归组合，可以形成复杂的树形结构，但对树形结构的控制却非常简单
 *
 *  <strong>缺点</strong>
 *  在增加新构件时很难对容器中的构件类型进行限制。有时候我们希望一个容器中只能有某些特定类型的对象，例如在某个文件夹中只能包含文本文件，使用组合模式时，不能依赖类型系统来施加这些约束，因为它们都来自于相同的抽象层，
 *  在这种情况下，必须通过在运行时进行类型检查来实现，这个实现过程较为复杂。
 *
 *
 *  <strong>适用场景</strong>
 *  (1) 在具有整体和部分的层次结构中，希望通过一种方式忽略整体与部分的差异，客户端可以一致地对待它们。
 *  (2) 在一个使用面向对象语言开发的系统中需要处理一个树形结构。
 *  (3) 在一个系统中能够分离出叶子对象和容器对象，而且它们的类型不固定，需要增加一些新的类型
 *
 * </p>
 *
 * @author songfei@xbniao.com
 * @create 2018/01/05 16:42
 **/
public class CompositePatternTest extends TestBase {

    @Test
    public void exec() throws Exception {
        Folder folder = new Folder("Root Folder");

        TextFile textFile = new TextFile("a.txt");
        ImageFile imageFile = new ImageFile("a.jpg");
        VideoFile videoFile = new VideoFile("a.mp4");
        Folder subFoler = new Folder("Sub Folder");

        folder.add(textFile);
        folder.add(imageFile);
        folder.add(videoFile);
        folder.add(subFoler);

        VideoFile bVideoFile ;
        subFoler.add(new ImageFile("b.png"));
        subFoler.add(new TextFile("b.txt"));
        subFoler.add(bVideoFile = new VideoFile("b.rmvb"));


        subFoler.display();

        subFoler.remove(bVideoFile);

        folder.display();



    }


}
