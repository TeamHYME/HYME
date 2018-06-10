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

public class LogList extends BaseAdapter {

    private Context context;
    private ArrayList<ItemData> itemList = new ArrayList<>();

    LogList(Context context){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.list_log, parent, false);
            vh.number =  (TextView) convertView.findViewById(R.id.tv_log_number);
            vh.name = (TextView) convertView.findViewById(R.id.tv_log_name);
            vh.type = (TextView) convertView.findViewById(R.id.tv_log_type);
            vh.content = (TextView) convertView.findViewById(R.id.tv_log_content);
            convertView.setTag(vh);
        }
        else{
            vh = (ViewHolder) convertView.getTag();
        }

        ItemData itemData = itemList.get(position);
        vh.number.setText(position + 1 + "");
        vh.name.setText(itemData.getName);
        vh.type.setText(itemData.getType);
        vh.content.setText(itemData.getContent);
        return convertView;
    }

    void addItem(int number, String name, String type, String content){
        ItemData itemData = new ItemData();
        itemData.getNumber = number;
        itemData.getName = name;
        itemData.getType = type;
        itemData.getContent = content;
        itemList.add(itemData);
    }

    String printName(int i){
        return itemList.get(i).getName;
    }

    String printType(int i){
        return itemList.get(i).getType;
    }

    String printContent(int i){
        return itemList.get(i).getContent;
    }

    private class ViewHolder {
        TextView number;
        TextView name;
        TextView type;
        TextView content;
    }

    private class ItemData{
        int getNumber;
        String getName;
        String getType;
        String getContent;
    }


}
