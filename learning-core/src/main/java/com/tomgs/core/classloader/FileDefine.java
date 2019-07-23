package com.tomgs.core.classloader;

/**
 * @author tangzhongyuan
 * @since 2019-07-16 14:36
 **/
public class FileDefine {

    public static final String WATCH_PACKAGE = "E:\\workspace\\sourcecode\\tomgs-java\\learning-core" +
            "\\target\\classes\\com\\tomgs\\core\\classloader\\watchfile";
    private long lastDefine;

    public long getLastDefine() {
        return lastDefine;
    }

    public void setLastDefine(long newTime) {
        this.lastDefine = newTime;
    }
}
