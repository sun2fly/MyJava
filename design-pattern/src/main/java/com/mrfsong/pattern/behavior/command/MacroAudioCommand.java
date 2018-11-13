package com.mrfsong.pattern.behavior.command;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

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
