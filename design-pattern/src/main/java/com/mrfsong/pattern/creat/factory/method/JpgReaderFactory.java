package com.mrfsong.pattern.creat.factory.method;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/09 14:16
 **/
public class JpgReaderFactory implements ReaderFactory {
    @Override
    public Reader getReader() {
        return new JpgReader();
    }
}
