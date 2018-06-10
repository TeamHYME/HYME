package org.androidtown.hyme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Master on 2017-12-12.
 */

public class RoomList extends BaseAdapter {
    private Context context;
    private ArrayList<ItemData> itemList = new ArrayList<>();

    RoomList(Context context){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.list_meeting, parent, false);
            vh.roomImage =  (ImageView) convertView.findViewById(R.id.iv_room_image);
            vh.roomTitle = (TextView) convertView.findViewById(R.id.tv_room_title);
            vh.roomMember = (TextView) convertView.findViewById(R.id.tv_room_member);
            vh.roomTime = (TextView) convertView.findViewById(R.id.tv_room_time);
            vh.roomImageIdk = (ImageView) convertView.findViewById(R.id.tv_room_image_idk);
            convertView.setTag(vh);
        }
        else{
            vh = (ViewHolder) convertView.getTag();
        }

        ItemData itemData = itemList.get(position);
        vh.roomImage.setImageResource(R.drawable.speaker);
        vh.roomTitle.setText(itemData.getTitle);
        vh.roomMember.setText(itemData.getMember);
        vh.roomTime.setText(itemData.getTime);
        vh.roomImageIdk.setImageResource(R.drawable.points);
        return convertView;
    }

    void addItem(String title, String member, String time){
        ItemData itemData = new ItemData();
        itemData.getTitle = title;
        itemData.getMember = member;
        itemData.getTime = time;
        itemList.add(itemData);
    }

    String printTitle(int i){
        return itemList.get(i).getTitle;
    }

    String printMember(int i){
        return itemList.get(i).getMember;
    }

    String printTime(int i){
        return itemList.get(i).getTime;
    }

    private class ViewHolder {
        ImageView roomImage;
        TextView roomTitle;
        TextView roomMember;
        TextView roomTime;
        ImageView roomImageIdk;
    }

    private class ItemData{
        String getTitle;
        String getMember;
        String getTime;
    }

}
