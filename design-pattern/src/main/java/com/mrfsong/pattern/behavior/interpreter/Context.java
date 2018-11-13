package com.mrfsong.pattern.behavior.interpreter;

/**
 * ${DESCRIPTION}
 *
 * @author lysongfei@gmail.com
 * @create 02/01/2018 20:05
 */
public class Context {

    private int input ;
    private int output;

    public Context(int input) {
        this.input = input;
    }

    public int getInput() {
        return input;
    }

    public void setInput(int input) {
        this.input = input;
    }

    public int getOutput() {
        return output;
    }

    public void setOutput(int output) {
        this.output = output;
    }
}
