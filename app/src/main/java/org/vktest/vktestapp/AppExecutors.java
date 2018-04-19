package org.vktest.vktestapp;

import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

@Singleton
public class AppExecutors {

    private static final int THREAD_COUNT = 3;

    private Executor diskIO;
    private Executor networkIO;
    private Executor mainThread;

    public AppExecutors() {
        diskIO = Executors.newSingleThreadExecutor();
        networkIO = Executors.newFixedThreadPool(THREAD_COUNT);

        mainThread = new MainThreadExecutor();
    }

    public Executor getDiskIO() {
        return diskIO;
    }

    public Executor getNetworkIO() {
        return networkIO;
    }

    public Executor getMainThread() {
        return mainThread;
    }

    private static class MainThreadExecutor implements Executor {
        private android.os.Handler mainThreadHandler = new android.os.Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
