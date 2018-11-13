package com.mrfsong.pattern.behavior.chain;

import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author lysongfei@gmail.com
 * @create 02/01/2018 21:35
 */
@Slf4j
public class GeneralManager extends Leader {
    public GeneralManager(String name) {
        super(name);
    }

    @Override
    public void handleRequest(LeaveRequest request) {
        int days = request.getLeaveDays(); //获取请假天数
        String name = request.getName(); //获取请假人姓名
        String reason = request.getReason(); // 获取请假理由

        if(days <= 30) { //如果满足30天内的要求，总经理直接审批
            log.info("员工" + name + "请假" + days + "天，理由：" + reason);
            log.info("总经理" + this.name + "审批通过");
        } else {
            log.info("请假天数过多，总经理" + this.name + "没法处理");
            if(this.nextLeader != null) { //否则，如果链上存在下一个Leader，就让他处理
                this.nextLeader.handleRequest(request);
            } else {
                log.info("请假不成功");
            }
        }
    }
}
