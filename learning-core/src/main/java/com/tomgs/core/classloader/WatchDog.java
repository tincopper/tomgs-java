package com.tomgs.core.classloader;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author tangzhongyuan
 * @since 2019-07-16 14:35
 **/
public class WatchDog implements Runnable {

    private Map<String, FileDefine> fileDefineMap;

    public WatchDog(Map<String, FileDefine> fileDefineMap) {
        this.fileDefineMap = fileDefineMap;
    }

    @Override
    public void run() {
        File file = new File(FileDefine.WATCH_PACKAGE);
        File[] files = file.listFiles();
        if (files == null) {
            System.out.println("files is null.");
            return;
        }
        for (File watchFile : files) {
            long newTime = watchFile.lastModified();
            FileDefine fileDefine = fileDefineMap.get(watchFile.getName());
            long oldTime = fileDefine.getLastDefine();
            //如果文件被修改了,那么重新生成累加载器加载新文件
            if (newTime != oldTime) {
                fileDefine.setLastDefine(newTime);
                loadMyClass();
            }
        }
    }

    // CustomerClassLoader customClassLoader = new CustomerClassLoader();
    public void loadMyClass() {
        // 自定义加载器需要放在这里，不能用成成员变量
        CustomerClassLoader customClassLoader = new CustomerClassLoader();
        try {
            Class<?> cls = customClassLoader.loadClass("com.tomgs.core.classloader.watchfile.TestUpdateClass", false);
            Object test = cls.newInstance();
            Method method = cls.getMethod("test");
            method.invoke(test);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        final Map<String, FileDefine> fileDefineMap = new HashMap<>();
        FileDefine fileDefine = new FileDefine();
        fileDefineMap.put("TestUpdateClass.class", fileDefine);

        final ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(1);
        scheduled.scheduleAtFixedRate(new WatchDog(fileDefineMap), 3, 5, TimeUnit.SECONDS);
    }
}
