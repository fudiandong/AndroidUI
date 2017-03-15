package com.fudd.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;

import com.fudd.activity.R;
import com.fudd.activity.TimeTrackerActivity;

/**
 * Created by fudd-office on 2017-3-15 11:36.
 * Email: 5036175@qq.com
 * QQ: 5036175
 * Description:
 */

public class TimeService extends Service {
    private long start = 0;
    private long time = 0;

    private class MyBinder extends Binder{
        //  获取service 实例
        TimeService getService(){
            return  TimeService.this;
        }
    }
    private final IBinder mBinder = new MyBinder();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }



    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            long current = System.currentTimeMillis();
            time += current - start;
            start = current;
//            counter.setText(DateUtils.formatElapsedTime(time/1000));
            sendEmptyMessageDelayed(0,250);
        }
    };
}
