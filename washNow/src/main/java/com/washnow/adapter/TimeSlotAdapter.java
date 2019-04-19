package com.washnow.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.washnow.R;
import com.washnow.utils.ZTypeface;
import com.washnow.vo.PickupSlot;

import java.util.ArrayList;

public class TimeSlotAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<PickupSlot> pList;

    public TimeSlotAdapter(ArrayList<PickupSlot> pList, Context context) {
        this.pList = pList;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return pList.size();
    }

    @Override
    public PickupSlot getItem(int position) {
        // TODO Auto-generated method stub
        return pList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder v;
        if (convertView != null && convertView.getTag() != null) {
            v = (ViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(R.layout.timeslot_item, null);
            v = new ViewHolder();
            v.tvName = (TextView) convertView.findViewById(R.id.tvSlot);

            convertView.setTag(v);

        }

        v.tvName.setTypeface(ZTypeface.UbuntuR(context));
        if(position%2==0){
            v.tvName.setBackgroundColor(ContextCompat.getColor(context,R.color.silver));
        }else {
            v.tvName.setBackgroundColor(ContextCompat.getColor(context,R.color.white));

        }
        v.tvName.setText(pList.get(position).time);

        return convertView;
    }


    public class ViewHolder {
        TextView tvName;

    }
}
