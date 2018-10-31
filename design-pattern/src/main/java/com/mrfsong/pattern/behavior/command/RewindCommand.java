package com.mrfsong.pattern.behavior.command;

/**
 * AudioPlayer - Rewind Command
 *
 * @author songfei@xbniao.com
 * @create 2017/12/27 14:28
 **/
public class RewindCommand implements Command {

    private AudioPlayer audioPlayer;

    public RewindCommand(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    @Override
    public void execute() {
        audioPlayer.rewind();
    }
}
