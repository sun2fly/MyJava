package com.wi1024.pattern.behavior.command;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2017/12/27 14:38
 **/
@Slf4j
public class MacroAudioCommand implements MacroCommand {

    private List<Command> commandList = new ArrayList<>();

    @Override
    public void add(Command command) {
        commandList.add(command);

    }

    @Override
    public void remove(Command command) {
        commandList.remove(command);
    }

    @Override
    public void execute() {
        log.info("--- execute macro command ---");
        for(Command command : commandList) {
            command.execute();
        }
    }
}
