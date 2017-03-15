package com.fudd.activity;

import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
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
import com.fudd.service.TimeService;
import com.fudd.utils.Util;
import com.fudd.adapter.TimeListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 此工程用于记录 《Android UI 基础教程》 学习代码
 */
public class TimeTrackerActivity extends AppCompatActivity implements View.OnClickListener, ServiceConnection {
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
    public static final String ACTION_TIME_UPDATE = "TimeUpdate";
    public static final String ACTION_TIMER_FINISHED = "TimeFinished";
    private TimeService timeService;


    private long start = 0;
    private long time = 0;
    private TimeListAdapter timeListAdapter;



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
        // 注册接收器
        // Register the TimeReceiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_TIME_UPDATE);
        filter.addAction(ACTION_TIMER_FINISHED);
        registerReceiver(receiver, filter);


        //createNotification();
//        createFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindService(new Intent(this,TimeService.class),this,Context.BIND_AUTO_CREATE);
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null)
            unregisterReceiver(receiver);

        if (timeService != null) {
            unbindService(this);
            timeService = null;
        }
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.start_stop:
               if (timeService == null){
                   mStart.setText(R.string.stop);
                   startService(new Intent(this,TimeService.class));
               }else if (!timeService.isTimerRunning()){
                   mStart.setText(R.string.stop);
                   timeService.startService(new Intent(this,TimeService.class));
               }else {
                   mStart.setText(R.string.start);
                   timeService.stopTimer();
               }
               break;
           case R.id.reset:
               if (timeService != null){
                   timeService.resetTimer();
               }
               counter.setText(DateUtils.formatElapsedTime(0));
               mStart.setText(R.string.start);
               break;
       }

    }

    // create bora
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            long time = intent.getLongExtra("time", 0);
            // 根据收到的广播 判断
            if (action == ACTION_TIME_UPDATE){
                counter.setText(DateUtils.formatElapsedTime(time/1000));
            }else if (action == ACTION_TIMER_FINISHED){
                if (timeListAdapter != null && time > 0){
                    timeListAdapter.add(time/1000);
                }
            }

        }
    };

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        timeService = ((TimeService.MyBinder) service).getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        timeService = null;
    }
}
