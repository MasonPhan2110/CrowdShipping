package com.Duong.crowdshipping.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.Duong.crowdshipping.R;

import java.util.HashMap;
import java.util.List;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> expandableList;
    private HashMap<String, List<String>> expandListDetail;
    private String park_name = "";
    public CustomExpandableListAdapter(Context context, List<String> expandableList, HashMap<String,List<String>> expandListDetail){
        this.context = context;
        this.expandableList = expandableList;
        this.expandListDetail = expandListDetail;
    }
    @Override
    public int getGroupCount() {
        return this.expandableList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.expandListDetail.get(this.expandableList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.expandableList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return  this.expandListDetail.get(this.expandableList.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(groupPosition);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group, null);
        }
        TextView titleTextView = convertView.findViewById(R.id.list_title);
        titleTextView.setTypeface(null, Typeface.BOLD);
        if(park_name == "") {
            titleTextView.setText(listTitle);
        }else {
            titleTextView.setText(park_name);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String listText = (String) getChild(groupPosition,childPosition);
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item,null);
        }
        TextView childTextView = convertView.findViewById(R.id.list_child);
        childTextView.setText(listText);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
