package com.fudd.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.fudd.activity.R;

/**
 * Created by fudd-office on 2017-3-10 16:19.
 * Email: 5036175@qq.com
 * QQ: 5036175
 * Description:
 */

public class TimeListAdapter extends ArrayAdapter<Long> implements View.OnLongClickListener {

    public TimeListAdapter(Context context, int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.time_row,null);
        }
        view.setOnLongClickListener(this);
        long time = getItem(position);
        TextView name = (TextView) view.findViewById(R.id.lap_name);
        String taskStr = getContext().getResources().getString(R.string.task_name);
        name.setText(String.format(taskStr,position + 1));
        TextView lapTime = (TextView) view.findViewById(R.id.lap_time);
        lapTime.setText(DateUtils.formatElapsedTime(time));
        return view;
    }

    @Override
    public boolean onLongClick(View v) {

        Toast.makeText(this.getContext(),v.getId()+"",Toast.LENGTH_SHORT).show();
        return false;
    }
}
