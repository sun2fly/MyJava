package com.mrfsong.pattern.behavior.command;

/**
 * AudioPlayer - Compound Command
 *
 * @author songfei@xbniao.com
 * @create 2017/12/27 14:38
 **/
public interface MacroCommand extends Command {

    void add(Command command);

    void remove(Command command);

}
