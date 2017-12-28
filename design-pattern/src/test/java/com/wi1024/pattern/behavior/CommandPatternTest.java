package com.wi1024.pattern.behavior;

import com.wi1024.pattern.TestBase;
import com.wi1024.pattern.behavior.command.AudioPlayer;
import com.wi1024.pattern.behavior.command.Command;
import com.wi1024.pattern.behavior.command.ConcreteCommand;
import com.wi1024.pattern.behavior.command.Invoker;
import com.wi1024.pattern.behavior.command.Keypad;
import com.wi1024.pattern.behavior.command.MacroAudioCommand;
import com.wi1024.pattern.behavior.command.MacroCommand;
import com.wi1024.pattern.behavior.command.PlayCommand;
import com.wi1024.pattern.behavior.command.Receiver;
import com.wi1024.pattern.behavior.command.RewindCommand;
import com.wi1024.pattern.behavior.command.StopCommand;

import org.junit.Test;

/**
 * Command Design Pattern Test Case
 *
 * @author songfei@xbniao.com
 * @create 2017/12/27 14:32
 **/
public class CommandPatternTest extends TestBase {

    @Test
    public void guide() throws Exception {

        Receiver receiver = new Receiver();
        Command command = new ConcreteCommand(receiver);

        Invoker invoker = new Invoker(command);
        invoker.aaction();
    }

    @Test
    public void exec() throws Exception{
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
