package com.mrfsong.pattern.struct.composite;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author songfei@xbniao.com
 * @create 2018/01/05 16:35
 **/
@Slf4j
public class Folder extends AbstractFile {


    private List<AbstractFile> files;

    public Folder(String name) {
        super(name);
        files = new ArrayList<>();
    }

    @Override
    public void display() {
        log.info("========== This is Folder#{} ==========" , name);
        for(AbstractFile file : files){
            file.display();
        }
    }

    public void add(AbstractFile file) {
        files.add(file);
    }

    public void remove(AbstractFile file) {
        files.remove(file);
    }
}
