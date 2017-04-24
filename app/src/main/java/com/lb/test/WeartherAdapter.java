package com.lb.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiongmai.lb.test.R;

import java.util.List;

/**
 * Created by Administrator on 2017-04-21.
 */

public class WeartherAdapter extends BaseAdapter {

    private List<String> dataList;
    private Context mContext;

    public WeartherAdapter(Context context , List<String> data){
        this.mContext = context;
        dataList = data;
    }

    public void updateLisy(List<String> data){
        dataList = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoler holer;
        if(convertView == null ){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_wearth , null);
            holer = new ViewHoler();
            holer.textView = (TextView) convertView.findViewById(R.id.wearther_info);
            convertView.setTag(holer);
        } else {
            holer = (ViewHoler) convertView.getTag();
        }
        holer.textView.setText(dataList.get(position));
        return convertView;
    }

    class ViewHoler {
        public TextView textView;
    }
}
