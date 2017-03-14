package com.fudd.activity;

import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.fudd.fragment.ConfirmClearDialogFragment;
import com.fudd.utils.Util;
import com.fudd.adapter.TimeListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 此工程用于记录 《Android UI 基础教程》 学习代码
 */
public class TimeTrackerActivity extends AppCompatActivity implements View.OnClickListener {

    /*
    添加butterknife框架 File --> Pro Struct --> moudles(选中)--> dependencies 右边最后--> 点 ‘+’号
    然后搜索 butterknife  添加
    compile 'com.jakewharton:butterknife:8.5.1'
    compile 'com.jakewharton:butterknife-compiler:8.5.1'
     */
    @BindView(R.id.counter)
    TextView counter;
    @BindView(R.id.time_list)
    ListView listView;
    @BindView(R.id.start_stop)
    Button mStart;
    @BindView(R.id.reset)
    Button mReset;


    private long start = 0;
    private long time = 0;
    private TimeListAdapter timeListAdapter;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            long current = System.currentTimeMillis();
            time += current - start;
            start = current;
            counter.setText(DateUtils.formatElapsedTime(time/1000));
            sendEmptyMessageDelayed(0,250);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mStart.setOnClickListener(this);
        mReset.setOnClickListener(this);

        counter.setText(DateUtils.formatElapsedTime(0));
        if ( timeListAdapter == null ){
            timeListAdapter = new TimeListAdapter(this,0);
        }
        listView.setAdapter(timeListAdapter);
        if (Util.useStrictMode(this)){
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .penaltyDialog()
                .build());
        }
        //createNotification();
//        createFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.clear_all:
                createFragment();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

    private void createFragment() {
        FragmentManager fm = getFragmentManager();
        if (fm.findFragmentByTag("dialog") == null){
            ConfirmClearDialogFragment dialogFragment = ConfirmClearDialogFragment.instance(timeListAdapter);
            dialogFragment.show(fm,"dialog");
        }

    }

    private boolean isTimerRunning(){
        return  handler.hasMessages(0);
    }

    private void startTimer(){
        start = System.currentTimeMillis();
        handler.removeMessages(0);
        handler.sendEmptyMessage(0);
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.start_stop:
               if (!isTimerRunning()){
                   startTimer();
                   mStart.setText(R.string.stop);
               }else {
                   stopTimer();
                   mStart.setText(R.string.start);
               }
               break;
           case R.id.reset:
               resetTimer();
               counter.setText(DateUtils.formatElapsedTime(0));
               mStart.setText(R.string.start);
               break;
       }

    }

    private void resetTimer() {
        stopTimer();
        if (timeListAdapter != null){
            timeListAdapter.add(time/1000);
        }
        time = 0;
    }

    private void stopTimer() {
        handler.removeMessages(0);
    }

    private void createNotification(){
        Intent intent = new Intent(this,NotificationActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this,0,intent,0);
        Notification notification = new Notification.Builder(this).setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("hi")
                .setContentText("hhh5555")
                .setContentIntent(pi)
                .getNotification();
//        Notification notifi = new Notification(R.mipmap.ic_launcher,"hi!!",System.currentTimeMillis());
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        nm.notify(1,notification);
    }

}
