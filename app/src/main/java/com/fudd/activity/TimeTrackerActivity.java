package com.fudd.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.ListView;
import android.widget.TextView;

import com.fudd.adapter.TimeListAdapter;

import butterknife.BindView;

/**
 * 此工程用于记录 《Android UI 基础教程》 学习代码
 */
public class TimeTrackerActivity extends AppCompatActivity {

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

    private TimeListAdapter timeListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        counter.setText(DateUtils.formatElapsedTime(0));
        if ( timeListAdapter == null ){
            timeListAdapter = new TimeListAdapter(this,0);
        }
        listView.setAdapter(timeListAdapter);

    }
}
