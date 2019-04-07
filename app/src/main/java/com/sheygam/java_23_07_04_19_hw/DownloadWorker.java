package com.sheygam.java_23_07_04_19_hw;

import android.os.Handler;
import android.os.Message;

import java.util.Random;


public class DownloadWorker implements Runnable {
    public static final int STATUS_START = 0x01;
    public static final int STATUS_END = 0x02;
    public static final int STATUS_COUNT = 0x03;
    public static final int STATUS_CURRENT = 0x04;
    public static final int STATUS_PROGRESS = 0x05;

    private Handler handler;

    public DownloadWorker(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        handler.sendEmptyMessage(STATUS_START);
        Random rnd = new Random();
        int count = rnd.nextInt(15) + 1;
        Message msg = handler.obtainMessage();
        msg.what = STATUS_COUNT;
        msg.arg1 = count;
        handler.sendMessage(msg);
        for (int i = 0; i < count; i++) {
            msg = handler.obtainMessage();
            msg.what = STATUS_CURRENT;
            msg.arg1 = i+1;
            msg.arg2 = count;
            handler.sendMessage(msg);
            for(int j = 0; j < 100; j++){
                msg = handler.obtainMessage();
                msg.what = STATUS_PROGRESS;
                msg.arg1 = j+1;
                handler.sendMessage(msg);
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        handler.sendEmptyMessage(STATUS_END);
    }
}
