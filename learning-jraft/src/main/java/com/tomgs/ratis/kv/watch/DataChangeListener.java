package com.tomgs.ratis.kv.watch;

/**
 * 数据变化监听器
 */
public interface DataChangeListener {

    void dataChanged(DataChangeEvent event);

}
