package com.fudd.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.util.Log;

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
    private NotificationManager notificationManager;
    private Notification notification;
    public static int TIMER_NOTIFICATION = 0;

    public class MyBinder extends Binder{
        //  获取service 实例
        public TimeService getService(){
            return  TimeService.this;
        }
    }
    private final IBinder mBinder = new MyBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("fudd","sev onCreate");
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showNotification();
        start = System.currentTimeMillis();
        handler.removeMessages(0);
        handler.sendEmptyMessage(0);
        return START_STICKY;
    }

    private void showNotification() {
        notification = new Notification.Builder(this).setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .getNotification();
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        startForeground(TIMER_NOTIFICATION,notification);
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
            updateTime();
//            counter.setText(DateUtils.formatElapsedTime(time/1000));
            sendEmptyMessageDelayed(0,250);
        }
    };

    private void updateTime() {
        Intent intent = new Intent(TimeTrackerActivity.ACTION_TIME_UPDATE);
        intent.putExtra("time",time);

        sendBroadcast(intent);
        updateNotification(time);
    }

    private void updateNotification(long time) {
        Intent intent = new Intent(this,TimeTrackerActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,0);
        notification = new  Notification.Builder(this).setContentTitle("定时器")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(DateUtils.formatElapsedTime(time/1000))
                .setContentIntent(pendingIntent)
                .getNotification();
        notificationManager.notify(TIMER_NOTIFICATION,notification);
    }

    public void startTimer(){
        start = System.currentTimeMillis();
        handler.removeMessages(0);
        handler.sendEmptyMessage(0);
    }

    public boolean isTimerRunning(){
        return  handler.hasMessages(0);
    }

    public void resetTimer() {
        stopTimer();
//        if (timeListAdapter != null){
//            timeListAdapter.add(time/1000);
//        }
        timerStopped();
        time = 0;
    }

    private void timerStopped() {
        Intent intent = new Intent(TimeTrackerActivity.ACTION_TIMER_FINISHED);
        intent.putExtra("time", time);
        sendBroadcast(intent);

        // Stop the notification
        stopForeground(true);
    }

    public void stopTimer() {
        handler.removeMessages(0);
        stopSelf();
        notificationManager.cancel(TIMER_NOTIFICATION);
    }


}
