package org.androidtown.hyme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ParticipantCreateList extends BaseAdapter {
    private Context context;
    private ArrayList<ItemData> itemList = new ArrayList<>();

    ParticipantCreateList(Context context){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.list_participant_create, parent, false);
            vh.image =  (ImageView) convertView.findViewById(R.id.iv_create_participant_image);
            vh.name = (TextView) convertView.findViewById(R.id.tv_create_participant_name);
            vh.delete = (ImageView) convertView.findViewById(R.id.iv_participant_delete);
            convertView.setTag(vh);
        }
        else{
            vh = (ViewHolder) convertView.getTag();
        }

        ItemData itemData = itemList.get(position);
        vh.name.setText(itemData.getName);
        return convertView;
    }

    void addItem(String name){
        ItemData itemData = new ItemData();
        itemData.getName = name;
        itemList.add(itemData);
    }

    String printName(int i){
        return itemList.get(i).getName;
    }

    private class ViewHolder {
        ImageView image;
        TextView name;
        ImageView delete;
    }

    private class ItemData{
        String getName;
    }

}
