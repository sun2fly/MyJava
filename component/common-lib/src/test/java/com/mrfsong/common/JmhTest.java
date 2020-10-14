package com.mrfsong.common;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@Measurement(iterations = 3, time = 100, timeUnit = TimeUnit.SECONDS)
@Warmup(iterations = 1, time = 100, timeUnit = TimeUnit.SECONDS)
@Threads(8)
@Fork(value = 2,
        jvmArgsAppend = {"-server", "-Xms1g", "-Xmx1g", "-Xmn512m", "-XX:CMSInitiatingOccupancyFraction=82", "-Xss256k",
                "-XX:+DisableExplicitGC", "-XX:+UseConcMarkSweepGC", "-XX:+CMSParallelRemarkEnabled",
                "-XX:LargePageSizeInBytes=128m", "-XX:+UseFastAccessorMethods",
                "-XX:+UseCMSInitiatingOccupancyOnly", "-XX:+CMSClassUnloadingEnabled"})
public class JmhTest {

    @Benchmark
    public void constructStringByAssignment() {
        String s = "Hello world!";
        for (int i = 0; i < 10; i++) {
            s += s;
        }
    }

    @Benchmark
    public void constructStringByConstructor() {
        String s = new String("Hello world!");
        for (int i = 0; i < 10; i++) {
            s += s;
        }
    }

    @Benchmark
    public void constructStringByStringBuilder() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(i);
        }
    }

    @Benchmark
    public void constructStringByStringBuffer() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 10; i++) {
            sb.append(i);
        }
    }


    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JmhTest.class.getSimpleName())
//                .forks(1)
//                .threads(Runtime.getRuntime().availableProcessors() * 2)
//                .mode(Mode.SingleShotTime)
//                .timeUnit(TimeUnit.MILLISECONDS)
//                .output("/tmp/jmh.log")
                .build();


        new Runner(opt).run();
    }

}
