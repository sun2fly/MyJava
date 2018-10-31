package com.mrfsong.pattern.behavior;

import com.mrfsong.pattern.TestBase;
import com.mrfsong.pattern.behavior.command.*;
import org.junit.Test;

/**
 * <p>
 *     <strong>命令模式的优缺点</strong>
 *      首先，命令模式的封装性很好：每个命令都被封装起来，对于客户端来说，需要什么功能就去调用相应的命令，而无需知道命令具体是怎么执行的。（比如有一组文件操作的命令：新建文件、复制文件、删除文件。如果把这三个操作都封装成一个命令类，客户端只需要知道有这三个命令类即可，至于命令类中封装好的逻辑，客户端则无需知道）
 *      其次，命令模式的扩展性很好，在命令模式中，在接收者类中一般会对操作进行最基本的封装，命令类则通过对这些基本的操作进行二次封装，当增加新命令的时候，对命令类的编写一般不是从零开始的，有大量的接收者类可供调用，也有大量的命令类可供调用，代码的复用性很好。（比如，文件的操作中，我们需要增加一个剪切文件的命令，则只需要把复制文件和删除文件这两个命令组合一下就行了，非常方便）
 *      最后说一下命令模式的缺点，那就是命令如果很多，开发起来就要头疼了。特别是很多简单的命令，实现起来就几行代码的事，而使用命令模式的话，不用管命令多简单，都需要写一个命令类来封装。
 * </p>
 * <p>
 *     <strong>命令模式的适用场景</strong>
 *     对于大多数请求-响应模式的功能，比较适合使用命令模式，正如命令模式定义说的那样，命令模式对实现记录日志、撤销操作等功能比较方便。
 * </p>
 *
 * @author songfei@xbniao.com
 * @create 2017/12/27 14:32
 **/
public class CommandPatternTest extends TestBase {

    @Test
    public void guide() throws Exception {

        Receiver receiver = new Receiver();
        Command command = new ConcreteCommand(receiver);
        command.execute();

        Invoker invoker = new Invoker(command);
        invoker.action();
    }

    @Test
    public void exec() throws Exception {
        AudioPlayer audioPlayer = new AudioPlayer();
        Command playCommand = new PlayCommand(audioPlayer);
        Command rewindCommand = new RewindCommand(audioPlayer);
        Command stopCommand = new StopCommand(audioPlayer);

        Keypad keypad = new Keypad();
        keypad.setPlayCommand(playCommand);
        keypad.setRewindCommand(rewindCommand);
        keypad.setStopCommand(stopCommand);

        keypad.play();
        keypad.rewind();
        keypad.stop();
        keypad.play();
    }

    @Test
    public void macroExec() throws Exception {
        AudioPlayer audioPlayer = new AudioPlayer();
        Command playCommand = new PlayCommand(audioPlayer);
        Command rewindCommand = new RewindCommand(audioPlayer);
        Command stopCommand = new StopCommand(audioPlayer);
        MacroCommand macroCommand = new MacroAudioCommand();
        macroCommand.add(playCommand);
        macroCommand.add(rewindCommand);
        macroCommand.add(stopCommand);

        macroCommand.execute();
    }

}
