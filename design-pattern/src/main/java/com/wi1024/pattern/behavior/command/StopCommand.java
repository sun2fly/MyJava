package com.wi1024.pattern.behavior.command;

/**
 * AudioPlayer - Stop Command
 *
 * @author songfei@xbniao.com
 * @create 2017/12/27 14:29
 **/
public class StopCommand implements Command {

    private AudioPlayer audioPlayer;

    public StopCommand(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    @Override
    public void execute() {
        audioPlayer.stop();

    }
}
