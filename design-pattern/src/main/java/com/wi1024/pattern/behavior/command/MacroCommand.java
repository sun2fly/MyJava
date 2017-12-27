package com.wi1024.pattern.behavior.command;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2017/12/27 14:38
 **/
public interface MacroCommand extends Command {

    void add(Command command);

    void remove(Command command);

}
