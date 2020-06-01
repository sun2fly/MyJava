package com.mrfsong.common.lib.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * <p>
 *
 * </p>
 *
 * @Author: Felix
 * @Created: 2020/06/01 10:01
 */
public class Printer {

    public static String getException(Throwable e){
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw,true));
        return sw.toString();
    }

}
