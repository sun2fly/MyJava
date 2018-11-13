package com.mrfsong.pattern.behavior.command;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2017/12/27 14:30
 **/
public class Keypad {

    private Command playCommand;
    private Command rewindCommand;
    private Command stopCommand;

    public void setPlayCommand(Command playCommand) {
        this.playCommand = playCommand;
    }

    public void setRewindCommand(Command rewindCommand) {
        this.rewindCommand = rewindCommand;
    }

    public void setStopCommand(Command stopCommand) {
        this.stopCommand = stopCommand;
    }

    public void play() {
        playCommand.execute();
    }

    public void rewind() {
        rewindCommand.execute();
    }

    public void stop() {
        stopCommand.execute();
    }
}
