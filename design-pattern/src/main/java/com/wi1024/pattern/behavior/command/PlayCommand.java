package com.wi1024.pattern.behavior.command;

/**
 * AudioPlayer - Play Command
 *
 * @author songfei@xbniao.com
 * @create 2017/12/27 14:28
 **/
public class PlayCommand implements Command {
    private AudioPlayer audioPlayer;

    public PlayCommand(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    @Override
    public void execute() {
        audioPlayer.play();
    }
}
