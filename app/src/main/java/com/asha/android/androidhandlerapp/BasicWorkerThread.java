package com.asha.android.androidhandlerapp;

import android.os.Handler;
import android.os.HandlerThread;

public class BasicWorkerThread extends HandlerThread {
    private Handler mWorkerHandler;

    public BasicWorkerThread(String name) {
        super(name);
    }

    public void postTask(Runnable task){
        mWorkerHandler.post(task);
    }

    public void prepareHandler(){
        mWorkerHandler = new Handler(getLooper());
    }
}
