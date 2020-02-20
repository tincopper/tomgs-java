package com.tomgs.guice.test;

import com.google.common.util.concurrent.*;

import javax.annotation.Nullable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *  
 *
 * @author tomgs
 * @version 2019/12/23 1.0 
 */
public class GuavaFutureTask {

    public static void main(String[] args) {
        // Java线程池
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        // guava线程池
        ListeningExecutorService pool = MoreExecutors.listeningDecorator(executorService);
        ListenableFuture<?> future = pool.submit(() -> {

        });
        // Deprecated
        /*Futures.addCallback(future, new FutureCallback<Object>() {
            @Override
            public void onSuccess(@Nullable Object result) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });*/
        Futures.addCallback(future, new FutureCallback<Object>() {
            @Override
            public void onSuccess(@Nullable Object result) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        }, Executors.newSingleThreadExecutor());
    }
    
}
