package com.example.cquedu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public class StudentInfoAdapter extends ArrayAdapter<StudentInfoPair> {
    private int resID;
    public StudentInfoAdapter(Context context, int resource, List<StudentInfoPair> objects){
        super(context, resource, objects);
        resID = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        StudentInfoPair studentinfo = getItem(position);
        View view;
        StudentInfoHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resID, parent, false);
            viewHolder = new StudentInfoHolder();
            viewHolder.field = view.findViewById(R.id.ii_tv_field);
            viewHolder.value = view.findViewById(R.id.ii_tv_value);
            view.setTag(viewHolder);
        }
        else {
            view = convertView;
            viewHolder = (StudentInfoHolder) view.getTag();
        }
        viewHolder.field.setText(studentinfo.getField());
        viewHolder.value.setText(studentinfo.getValue());
        return view;
    }
}
