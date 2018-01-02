package com.wi1024.pattern.behavior.chain;

/**
 * ${DESCRIPTION}
 *
 * @author lysongfei@gmail.com
 * @create 02/01/2018 21:32
 */
public abstract class Leader {

    protected String name;
    protected Leader nextLeader; //责任链上的后继对象，即这个对象无法处理，就转移给下一个Leader

    public Leader(String name) {
        super();
        this.name = name;
    }
    // 设定责任链上的后继对象
    public void setNextLeader(Leader nextLeader) {
        this.nextLeader = nextLeader;
    }

    /**
     * 处理请求的核心的业务方法
     * 需要不同继承该类的领导自己实现
     */
    public abstract void handleRequest(LeaveRequest request);

}
