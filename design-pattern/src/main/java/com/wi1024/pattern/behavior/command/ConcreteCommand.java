package com.wi1024.pattern.behavior.command;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2017/12/27 13:48
 **/
public class ConcreteCommand implements Command {

    private Receiver receiver ;

    public ConcreteCommand(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        receiver.action();
    }
}
