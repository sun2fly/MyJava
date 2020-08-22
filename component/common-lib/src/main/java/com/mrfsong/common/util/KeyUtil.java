package com.mrfsong.common.util;

import java.util.UUID;

public class KeyUtil {


    public static String uuidString() {

        return UUID.randomUUID().toString().replaceAll("-","").toLowerCase();
    }

}
