package com.mrfsong.pattern.struct.decorator;

import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/05 17:43
 **/
@Slf4j
public class ConcreteComponent extends Component {
    @Override
    public void operation() {
        log.info("========== This is ConcreteComponent#Operation() ==========");
    }
}
