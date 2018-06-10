package org.androidtown.hyme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Master on 2017-12-11.
 */

public class ParticipantList extends BaseAdapter {
    private Context context;
    private ArrayList<ItemData> itemList = new ArrayList<>();

    ParticipantList(Context context){
        this.context = context;
    }

    @Override
    public int getCount(){
        return itemList.size();
    }

    @Override
    public Object getItem(int position){
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder vh;
        if(convertView == null){
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.list_participant, parent, false);
            vh.number =  (TextView) convertView.findViewById(R.id.tv_participant_number);
            vh.name = (TextView) convertView.findViewById(R.id.tv_participant_name);
            vh.authority = (TextView) convertView.findViewById(R.id.tv_participant_authority);
            convertView.setTag(vh);
        }
        else{
            vh = (ViewHolder) convertView.getTag();
        }

        ItemData itemData = itemList.get(position);
        vh.number.setText(position + 1 + "");
        vh.name.setText(itemData.getName);
        vh.authority.setText(itemData.getAuth);
        return convertView;
    }

    void addItem(int number, String name, String auth){
        ItemData itemData = new ItemData();
        itemData.getNumber = number;
        itemData.getName = name;
        itemData.getAuth = auth;
        itemList.add(itemData);
    }

    String printName(int i){
        return itemList.get(i).getName;
    }

    String printAuth(int i){
        return itemList.get(i).getAuth;
    }

    private class ViewHolder {
        TextView number;
        TextView name;
        TextView authority;
    }

    private class ItemData{
        int getNumber;
        String getName;
        String getAuth;
    }

}
